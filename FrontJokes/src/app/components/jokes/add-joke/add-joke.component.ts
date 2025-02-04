import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormsModule } from '@angular/forms';
import { JokesService } from '../../../services/jokes.service';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CategoriesService } from '../../../services/categories.service';
import { LanguagesService } from '../../../services/languages.service';
import { TypesService } from '../../../services/types.service';
import { FlagsService } from '../../../services/flags.service';


@Component({
  selector: 'app-add-joke',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule],
  templateUrl: './add-joke.component.html',
  styleUrl: './add-joke.component.css'
})

export class AddJokeComponent implements OnInit {
  jokeForm: FormGroup;
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
    private router: Router
  ) {
    this.jokeForm = this.fb.group({
      language: [null],
      categories: [null],
      type: [null],
      text1: ['', [Validators.required, Validators.maxLength(200)]],
      text2: ['', [Validators.maxLength(200)]],
      flags: [[]]
    });
  }

  ngOnInit(): void {
    this.loadFlags();
    this.loadLanguages();
    this.loadCategories();
    this.loadTypes();

    this.jokeForm.get('type')?.valueChanges.subscribe((typeId) => {
      this.onTypeChange(typeId);
    });
  
    // Deshabilitar por defecto los textos hasta que se seleccione un tipo
    this.jokeForm.get('text1')?.disable();
    this.jokeForm.get('text2')?.disable();
  }

  loadFlags(): void {
    this.flagsService.getFlags().subscribe({
      next: (data) => {
        this.flags = data;
      },
      error: (err) => console.error('Error loading flags:', err)
    });
  }

  loadLanguages(): void {
    this.languagesService.getLanguages().subscribe({
      next: (data) => {
        this.languages = data;
      },
      error: (err) => console.error('Error loading languages:', err)
    });
  }

  loadCategories(): void {
    this.categoriesService.getCategories().subscribe({
      next: (data) => {
        this.categories = data;
      },
      error: (err) => console.error('Error loading categories:', err)
    });
  }

  loadTypes(): void {
    this.typesService.getTypes().subscribe({
      next: (data) => {
        this.types = data;
      },
      error: (err) => console.error('Error loading types:', err)
    });
  }

  submitJoke(): void {
    if (this.jokeForm.invalid) return;

    const formValue = this.jokeForm.value;

    const jokePayload = {
      language: formValue.language ? { id: formValue.language } : null,
      categories: formValue.categories ? { id: formValue.categories } : null,
      types: formValue.type ? { id: formValue.type } : null,
      text1: formValue.text1 || null,
      text2: formValue.text2 || null,
      flagses: formValue.flags.map((id: number) => ({ id }))
    };

    this.jokesService.createJoke(jokePayload).subscribe({
      next: () => {
        alert('Joke added successfully!');
        this.router.navigate(['/jokes']);
      },
      error: (err) => {
        console.error('Error adding joke:', err);
        alert('An error occurred while adding the joke.');
      }
    });
  }
  onFlagChange(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    const flagsControl = this.jokeForm.get('flags');
    const currentFlags = flagsControl?.value || [];

    if (checkbox.checked) {
      flagsControl?.setValue([...currentFlags, checkbox.value]);
    } else {
      const updatedFlags = currentFlags.filter((flag: string) => flag !== checkbox.value);
      flagsControl?.setValue(updatedFlags);
    }
  }

  onTypeChange(typeId: string | null): void {
    const text1Control = this.jokeForm.get('text1');
    const text2Control = this.jokeForm.get('text2');
  
    // Convertir `id` en `type`
    const selectedType = this.types.find(type => type.id === Number(typeId));
  
    if (!selectedType) {
      // Si no hay type seleccionado, deshabilitar ambos textos
      text1Control?.setValue('');
      text1Control?.disable();
      text2Control?.setValue('');
      text2Control?.disable();
      return;
    }
  
    if (selectedType.type === 'single') {
      // Si el tipo es "single", solo se habilita text1
      text1Control?.enable();
      text2Control?.setValue('');
      text2Control?.disable();
    } else if (selectedType.type === 'twopart') {
      // Si el tipo es "twopart", se habilitan ambos textos
      text1Control?.enable();
      text2Control?.enable();
    }
  }

  onSelectionChange(controlName: string, value: any): void {
    if (!value) {
      this.jokeForm.get(controlName)?.setValue(null); // Asegura que se guarde como null
    }
  }
    
  public navigateToJokes() {

    this.router.navigate(['/jokes']);

  }
}