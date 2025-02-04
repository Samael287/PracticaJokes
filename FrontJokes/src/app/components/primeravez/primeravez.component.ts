import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { JokesService } from '../../services/jokes.service';
import { PrimeravezService } from '../../services/primeravez.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-primeravez',
  standalone: true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './primeravez.component.html',
  styleUrl: './primeravez.component.css'
})

export class PrimeraVezComponent implements OnInit {
  primeraVezForm!: FormGroup;
  jokes: any[] = [];
  primeraVezList: any[] = [];
  filteredPrimeraVezList: any[] = [];
  editMode: boolean = false;
  selectedId: number | null = null;
  showPrimeraVez: boolean = false;
  searchTerm: string = '';

  constructor(
    private fb: FormBuilder,
    private jokesService: JokesService,
    private primeraVezService: PrimeravezService
  ) {}

  ngOnInit(): void {
    this.loadJokes();
    this.initializeForm();
    this.loadPrimeraVez();
  }

  initializeForm(): void {
    this.primeraVezForm = this.fb.group({
      joke: [null, Validators.required],
      programa: ['', Validators.required],
      fechaEmision: ['', Validators.required],
      telefonos: this.fb.array([
        this.fb.control('', [
          Validators.required,
          Validators.pattern(/^[6789]\d{8}$/)
        ])
      ])
    });
  }

  get telefonosArray(): FormArray {
    return this.primeraVezForm.get('telefonos') as FormArray;
  }

  agregarTelefono(): void {
    this.telefonosArray.push(
      this.fb.control('', [
        Validators.required,
        Validators.pattern(/^[6789]\d{8}$/)
      ])
    );
  }

  eliminarTelefono(index: number): void {
    if (this.telefonosArray.length > 1) {
      this.telefonosArray.removeAt(index);
    }
  }

  loadJokes(): void {
    this.jokesService.getJokes().subscribe((data) => (this.jokes = data));
  }

  loadPrimeraVez(): void {
    this.primeraVezService.getPrimeravez().subscribe({
      next: (data) => {
        console.log('Datos recibidos desde el backend:', data);

        this.primeraVezList = data.map((item: any) => ({
          id: item.id,
          programa: item.programa,
          fechaEmision: item.fechaEmision,
          joke: item.joke_id ? { id: item.joke_id } : null,  // Corrección aquí
          telefonos: Array.isArray(item.telefonos)
            ? item.telefonos.map((tel: any) => ({ numero: tel.numero ?? tel }))
            : []
        }));

        this.filteredPrimeraVezList = [...this.primeraVezList]; // Inicializa lista filtrada
      },
      error: (err) => console.error('Error cargando primeras veces:', err)
    });
  }

  toggleListaPrimeraVez(): void {
    this.showPrimeraVez = !this.showPrimeraVez;
    if (this.showPrimeraVez) {
      this.loadPrimeraVez();
    }
  }

  filterPrimeraVez(): void {
    const term = this.searchTerm.toLowerCase();
    this.filteredPrimeraVezList = this.primeraVezList.filter(
      (primera) =>
        primera.programa.toLowerCase().includes(term) ||
        primera.fechaEmision.includes(term)
    );
  }

  editarPrimeraVez(primeravez: any): void {
    if (!primeravez) {
      console.error("Error: Datos inválidos en la primera vez", primeravez);
      return;
    }

    this.editMode = true;
    this.selectedId = primeravez.id;

    this.primeraVezForm.patchValue({
      joke: primeravez.joke ? primeravez.joke.id : null,
      programa: primeravez.programa,
      fechaEmision: primeravez.fechaEmision
    });

    this.telefonosArray.clear(); // Limpiar los teléfonos previos antes de agregar los nuevos

    if (Array.isArray(primeravez.telefonos) && primeravez.telefonos.length > 0) {
        // Si hay teléfonos, los agregamos al FormArray
        primeravez.telefonos.forEach((tel: any) => {
            this.telefonosArray.push(
                this.fb.control(tel.numero, [
                    Validators.required,
                    Validators.pattern(/^[6789]\d{8}$/)
                ])
            );
        });
    } else {
        // Si no hay teléfonos, agregar uno vacío
        this.agregarTelefono();
    }
    console.log("Formulario cargado para edición:", this.primeraVezForm.value);
}

  eliminarPrimeraVez(id: number): void {

      const primeraVez = this.primeraVezList.find(p => p.id === id);

      if (!primeraVez) {
        alert('La primera vez no se encontró.');
        return;
      }

      if (primeraVez.telefonos && primeraVez.telefonos.length > 0) {
        if (confirm('¿Seguro que quieres eliminar esta primera vez junto con sus teléfonos?')) {
          this.primeraVezService.deletePrimeravez(id).subscribe(() => {
            alert('Primera vez eliminada correctamente.');
            this.loadPrimeraVez();
          });
        }
      } else {
        this.primeraVezService.deletePrimeravez(id).subscribe(() => {
          alert('Primera vez eliminada correctamente.');
          this.loadPrimeraVez();
      });
    }
  }

  submitPrimeraVez(): void {
    if (this.primeraVezForm.invalid || this.telefonosArray.length === 0) {
        alert("Debe completar todos los campos y agregar al menos un teléfono.");
        return;
    }

    const formValue = this.primeraVezForm.value;

    const fechaEmision = formValue.fechaEmision;
    const fechaActual = new Date().toISOString().split('T')[0];

    if (fechaEmision > fechaActual) {
      alert('❌ La fecha de emisión no puede ser mayor a la fecha actual.');
      return;
    }
    
    const payload = {
        id: this.selectedId,  // Incluir el ID en la actualización
        programa: formValue.programa,
        fechaEmision: formValue.fechaEmision,
        joke: { id: formValue.joke },
        telefonos: this.telefonosArray.controls.map(control => ({
            numero: control.value
        }))
    };

    console.log("Enviando payload:", payload);

    if (this.editMode && this.selectedId) {
        this.primeraVezService.updatePrimeravez(this.selectedId, payload).subscribe({
            next: () => {
                alert('Registro actualizado con éxito.');
                this.resetForm();
                this.loadPrimeraVez();
            },
            error: (err) => console.error('Error al actualizar:', err)
        });
    } else {
        this.primeraVezService.createPrimeravez(payload).subscribe({
            next: () => {
                alert('Registro guardado con éxito.');
                this.resetForm();
                this.loadPrimeraVez();
            },
            error: (err) => console.error('Error al crear:', err)
        });
      }
  }

  resetForm(): void {
    this.primeraVezForm.reset();
    this.telefonosArray.clear();
    this.agregarTelefono();
    this.editMode = false;
    this.selectedId = null;
  }
}
