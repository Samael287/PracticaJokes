import { Component, OnInit } from '@angular/core';
import { FlagsService } from '../../../services/flags.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-flag',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule],
  templateUrl: './edit-flag.component.html',
  styleUrls: ['./edit-flag.component.css']
})
/*export class EditFlagComponent implements OnInit {
  editFlagForm!: FormGroup;
  id!: number;

  constructor(
    private fb: FormBuilder,
    private flagsService: FlagsService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.editFlagForm = this.fb.group({
      flag: ['', Validators.required], // Asegurarse de que coincida con el campo en el HTML
    });

    this.id = +this.route.snapshot.params['id']; // Obtener el ID como número
    if (this.id) {
      this.loadFlag(); // Cargar los datos de la flag seleccionada
    }
  }

  loadFlag(): void {
    console.log('Cargando flag con ID:', this.id);
    this.flagsService.getFlagById(this.id).subscribe({
      next: (flag) => {
        if (flag) {
          console.log('Flag cargado:', flag);
          this.editFlagForm.patchValue({
            flag: flag.flag, // Actualizar el campo del formulario
          });
        } else {
          console.error('Flag no encontrado');
        }
      },
      error: (err) => {
        console.error('Error al cargar el flag:', err);
        alert('Error al cargar el flag.');
      },
    });
  }

  updateFlag(): void {
    if (this.editFlagForm.invalid) {
      console.warn('Formulario inválido:', this.editFlagForm.value);
      return;
    }

    const updatedFlag = {
      flag: this.editFlagForm.value.flag,
    };

    console.log('Actualizando flag con ID:', this.id, 'Datos:', updatedFlag);

    this.flagsService.updateFlag(this.id, updatedFlag).subscribe({
      next: () => {
        alert('Flag actualizado correctamente.');
        this.router.navigate(['/flags']);
      },
      error: (err) => {
        console.error('Error al actualizar el flag:', err);
        alert('No se pudo actualizar el flag.');
      },
    });
  }

  goBack(): void {
    this.router.navigate(['/flags']);
  }
}*/
export class EditFlagComponent implements OnInit {
  id!: number;
  flagName: string = ''; // Variable para el nombre de la flag

  constructor(
    private flagsService: FlagsService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.id = +this.route.snapshot.params['id'];
    if (this.id) {
      this.loadFlag();
    }
  }

  loadFlag(): void {
    console.log('Cargando flag con ID:', this.id);
    this.flagsService.getFlagById(this.id).subscribe({
      next: (flag) => {
        if (flag) {
          console.log('Flag cargado:', flag);
          this.flagName = flag.flag; // Asignar el valor cargado a la variable
        } else {
          console.error('Flag no encontrado');
        }
      },
      error: (err) => {
        console.error('Error al cargar el flag:', err);
        alert('Error al cargar el flag.');
      },
    });
  }

  updateFlag(): void {
    if (!this.flagName) {
      console.warn('Nombre del flag no válido');
      return;
    }

    const updatedFlag = {
      flag: this.flagName,
    };

    console.log('Actualizando flag con ID:', this.id, 'Datos:', updatedFlag);

    this.flagsService.updateFlag(this.id, updatedFlag).subscribe({
      next: () => {
        alert('Flag actualizado correctamente.');
        this.router.navigate(['/flags']);
      },
      error: (err) => {
        console.error('Error al actualizar el flag:', err);
        alert('No se pudo actualizar el flag.');
      },
    });
  }

  goBack(): void {
    this.router.navigate(['/flags']);
  }
}