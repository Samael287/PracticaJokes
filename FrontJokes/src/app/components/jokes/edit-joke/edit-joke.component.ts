import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { JokesService } from '../../../services/jokes.service';
import { LanguagesService } from '../../../services/languages.service';
import { CategoriesService } from '../../../services/categories.service';
import { TypesService } from '../../../services/types.service';
import { FlagsService } from '../../../services/flags.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-edit-joke',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule],
  templateUrl: './edit-joke.component.html',
  styleUrl: './edit-joke.component.css'
})

export class EditJokeComponent implements OnInit {
  jokeForm!: FormGroup;
  jokeId!: number;
  isEditMode: boolean = false;
  languages: any[] = [];
  categories: any[] = [];
  types: any[] = [];
  flags: any[] = [];

  constructor(
    private fb: FormBuilder,
    private jokesService: JokesService,
    private languagesService: LanguagesService,
    private categoriesService: CategoriesService,
    private typesService: TypesService,
    private flagsService: FlagsService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    // Check if we're in edit mode
    this.jokeId = this.route.snapshot.params['id'];
    this.loadAllData();

     // Escuchar cambios en el campo `type`
     this.jokeForm.get('type')?.valueChanges.subscribe((type) => {
      this.onTypeChange(type);
    });
  }

  initializeForm(): void {
    this.jokeForm = this.fb.group({
      language: [null],
      categories: [null],
      type: [null],
      text1: [''],
      text2: [''],
      flags: [[]],
    });
  }

  loadAllData(): void {
    forkJoin({
      languages: this.languagesService.getLanguages(),
      categories: this.categoriesService.getCategories(),
      types: this.typesService.getTypes(),
      flags: this.flagsService.getFlags(),
    }).subscribe({
      next: (results) => {
        this.languages = results.languages;
        this.categories = results.categories;
        this.types = results.types;
        this.flags = results.flags;

        if (this.jokeId) {
          this.isEditMode = true;
          this.loadJoke();
        }
      },
      error: (err) => console.error('Error loading data:', err),
    });
  }

  public navigateToJokes() {

    this.router.navigate(['/jokes']);

  }

  isFlagSelected(flagId: number): boolean {
    const flags = this.jokeForm.get('flags')?.value || [];
    return flags.includes(flagId);
  }
  
  toggleFlag(flagId: number): void {
    const currentFlags = this.jokeForm.get('flags')?.value || [];
    if (currentFlags.includes(flagId)) {
      this.jokeForm.get('flags')?.setValue(currentFlags.filter((id: number) => id !== flagId));
    } else {
      this.jokeForm.get('flags')?.setValue([...currentFlags, flagId]);
    }
  }

  loadDropdownData(): void {
    this.languagesService.getLanguages().subscribe({
      next: (data) => (this.languages = data),
      error: (err) => console.error('Error loading languages:', err),
    });

    this.categoriesService.getCategories().subscribe({
      next: (data) => (this.categories = data),
      error: (err) => console.error('Error loading categories:', err),
    });

    this.typesService.getTypes().subscribe({
      next: (data) => (this.types = data),
      error: (err) => console.error('Error loading types:', err),
    });

    this.flagsService.getFlags().subscribe({
      next: (data) => (this.flags = data),
      error: (err) => console.error('Error loading flags:', err),
    });
  }

  loadJoke(): void {
    this.jokesService.getJokeById(this.jokeId).subscribe({
      next: (joke) => {
        console.log('Loaded Joke:', joke); // Debug: Verifica los datos cargados

        // Verifica que joke.flagses sea un array válido
        const flagses = Array.isArray(joke.flagses) ? joke.flagses : [];
        console.log('Original Joke Flagses:', flagses);
  
        // Asegúrate de que `this.flags` está cargado correctamente
        console.log('Available Flags:', this.flags);
  
        // Map flags names to their corresponding IDs, asegurando que los datos sean válidos
        const selectedFlags = flagses
          .map((flagName: string) => {
            const foundFlag = this.flags.find((flag) => flag.flag === flagName);
            return foundFlag ? foundFlag.id : null; // Devuelve null si no se encuentra
          })
          .filter((id: any): id is number => id !== null); // Filtra valores nulos
  
        console.log('Mapped Selected Flags IDs:', selectedFlags); // Debug: IDs seleccionados

        this.jokeForm.patchValue({
          language: this.languages.find((lang) => lang.language === joke.language)?.id || null,
          categories: this.categories.find((cat) => cat.category === joke.categories)?.id || null,
          type: this.types.find((type) => type.type === joke.types)?.id || null,
          text1: joke.text1,
          text2: joke.text2,
          flags: selectedFlags,
        });

        // Validar `text1` y `text2` según el type cargado
        this.onTypeChange(this.jokeForm.get('type')?.value);
        
        console.log('Loaded joke:', this.jokeForm.value); // Debug
        console.log('Joke Flags:', joke.flagses); // Verifica las flags del joke
        console.log('Selected Flags IDs:', selectedFlags); // Verifica los IDs extraídos
        console.log('Form Value After Patch:', this.jokeForm.value); // Verifica el valor del formulario
      },
      error: (err) => console.error('Error loading joke:', err),
    });
  }

  saveJoke(): void {
    if (this.jokeForm.invalid) return;
  
    const formValue = this.jokeForm.value;
  
    // Construir el payload con la estructura esperada
    const jokePayload = {
      language: formValue.language ? { id: formValue.language } : null,
      categories: formValue.categories ? { id: formValue.categories } : null,
      types: formValue.type ? { id: formValue.type } : null, // Corregido el envío de type
      text1: formValue.text1 || null,
      text2: formValue.text2 || null,
      flagses: formValue.flags.map((flagId: number) => ({ id: flagId })), // Corregido el mapeo de flags
    };
  
    console.log('Payload being sent:', jokePayload);
  
    if (this.isEditMode) {
      this.jokesService.updateJoke(this.jokeId, jokePayload).subscribe({
        next: () => {
          alert('Joke updated successfully!');
          console.log('Joke updated successfully!');
          this.router.navigate(['/jokes']);
        },
        error: (err) => {
          console.error('Error updating joke:', err);
          alert('An error occurred while adding the joke.');
        },
      });
    } else {
      this.jokesService.createJoke(jokePayload).subscribe({
        next: () => {
          console.log('Joke created successfully!');
          this.router.navigate(['/jokes']);
        },
        error: (err) => {
          console.error('Error creating joke:', err);
        },
      });
    }
  }   

  onTypeChange(typeId: string | null): void {
    const text1Control = this.jokeForm.get('text1');
    const text2Control = this.jokeForm.get('text2');
  
    // Convertir `id` en `type`
    const selectedType = this.types.find(type => type.id === Number(typeId));
  
    if (!selectedType) {
      // Si no hay type seleccionado, deshabilitar ambos textos
     // Si no hay type seleccionado, deshabilitar ambos textos y quitar validaciones
     text1Control?.setValue('');
     text1Control?.disable();
     text1Control?.clearValidators();
 
     text2Control?.setValue('');
     text2Control?.disable();
     text2Control?.clearValidators();
 
   } else if (selectedType.type === 'single') {
     // Si el tipo es "single", habilitar text1 y hacerlo obligatorio, desactivar text2
     text1Control?.enable();
     text1Control?.setValidators([Validators.required, Validators.maxLength(200)]);
 
     text2Control?.setValue('');
     text2Control?.disable();
     text2Control?.clearValidators();
 
   } else if (selectedType.type === 'twopart') {
     // Si el tipo es "twopart", habilitar ambos y hacerlos obligatorios
     text1Control?.enable();
     text1Control?.setValidators([Validators.required, Validators.maxLength(200)]);
 
     text2Control?.enable();
     text2Control?.setValidators([Validators.required, Validators.maxLength(200)]);
   }
 
   // Aplicar cambios en validaciones
   text1Control?.updateValueAndValidity();
   text2Control?.updateValueAndValidity();
 }
}