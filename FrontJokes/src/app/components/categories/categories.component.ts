import { Component, OnInit } from '@angular/core';
import { CategoriesService } from '../../services/categories.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [FormsModule, CommonModule],
  providers: [CategoriesService],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css'
})
export class CategoriesComponent implements OnInit {
  categorias: any[] = [];
  filteredCategories: any[] = [];
  searchTerm: string = '';
  categoriaActual: any = { id: null, category: '' };
  mostrandoFormulario: boolean = false;
  isEditing: boolean = false;

  constructor(private categoriesService: CategoriesService) {}

  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.categoriesService.getCategories().subscribe((data: any[]) => {
      this.categorias = data;
      this.filteredCategories = data;
    });
  }

  onSearch(): void {
    this.filteredCategories = this.categorias.filter((categoria) =>
      categoria.category.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }

  abrirFormularioAnadir(): void {
    this.categoriaActual = { id: null, category: '' };
    this.isEditing = false;
    this.mostrandoFormulario = true;
  }

  editarCategoria(categoria: any): void {
    this.categoriaActual = { ...categoria };
    this.isEditing = true;
    this.mostrandoFormulario = true;
  }

  guardarCategoria(): void {

    if (!this.categoriaActual.category || this.categoriaActual.category.trim() === '') {
      alert('El nombre de la categoría no puede estar vacío.');
      return;
    }

    if (this.isEditing) {
      // Actualizar categoría existente
      this.categoriesService
        .updateCategory(this.categoriaActual.id, this.categoriaActual)
        .subscribe(() => {
          this.cargarCategorias();
          this.cerrarFormulario();
        });
    } else {
      // Añadir nueva categoría
      this.categoriesService.createCategory(this.categoriaActual).subscribe(() => {
        this.cargarCategorias();
        this.cerrarFormulario();
      });
    }
  }

  eliminarCategoria(id: number): void {

    const categoria = this.categorias.find(c => c.id === id);

    if (!categoria) {
      alert('La categoría no se encontró.');
      return;
    }

    if (categoria.jokeses && categoria.jokeses.length > 0) {
      if (confirm(`La categoría tiene ${categoria.jokeses.length} chiste(s) asociado(s). ¿Estás seguro de que quieres eliminar la categoría y todos sus chistes asociados?`)) {
        this.categoriesService.deleteCategory(id).subscribe(() => {
          this.cargarCategorias();
        });
      }
    } else{
      this.categoriesService.deleteCategory(id).subscribe(() => {
        alert('Category eliminada correctamente.');
        this.cargarCategorias();
      });

    }
  }

  cerrarFormulario(): void {
    this.mostrandoFormulario = false;
  }
}