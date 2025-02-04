import { Component, OnInit } from '@angular/core';
import { Types } from '../../models/types.model';
import { TypesService } from '../../services/types.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-types',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './types.component.html',
  styleUrl: './types.component.css'
})
export class TypesComponent implements OnInit {
  types: any[] = [];
  filteredTypes: any[] = [];
  searchTerm: string = '';
  mostrandoFormulario: boolean = false;
  isEditing: boolean = false;
  tipoActual: any = { id: null, type: '' };

  constructor(private typesService: TypesService) {}

  ngOnInit(): void {
    this.cargarTipos();
  }

  cargarTipos(): void {
    this.typesService.getTypes().subscribe((data: Types[]) => {
      this.types = data;
      this.filteredTypes = data;
    });
  }

  onSearch(): void {
    this.filteredTypes = this.types.filter((type) =>
      type.type?.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }

  abrirFormularioAnadir(): void {
    this.isEditing = false;
    this.tipoActual = { id: 0, type: '' };
    this.mostrandoFormulario = true;
  }

  editarTipo(type: Types): void {
    this.isEditing = true;
    this.tipoActual = { ...type };
    this.mostrandoFormulario = true;
  }

  guardarTipo(): void {
    if (this.isEditing) {
      this.typesService.updateType(this.tipoActual.id, this.tipoActual).subscribe(() => {
        this.cargarTipos();
        this.cerrarFormulario();
      });
    } else {
      this.typesService.createType(this.tipoActual).subscribe(() => {
        this.cargarTipos();
        this.cerrarFormulario();
      });
    }
  }

  eliminarTipo(id: number): void {
    if (confirm('¿Estás seguro de que deseas eliminar este tipo?')) {
      this.typesService.deleteType(id).subscribe(() => {
        this.cargarTipos();
      });
    }
  }

  cerrarFormulario(): void {
    this.mostrandoFormulario = false;
  }
}
