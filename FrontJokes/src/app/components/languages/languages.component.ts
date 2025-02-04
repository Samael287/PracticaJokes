import { Component, OnInit } from '@angular/core';
import { LanguagesService } from '../../services/languages.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-languages',
  standalone: true,
  imports: [FormsModule, CommonModule],
  providers: [LanguagesService],
  templateUrl: './languages.component.html',
  styleUrl: './languages.component.css'
})
export class LanguagesComponent implements OnInit {
  languages: any[] = [];
  filteredLanguages: any[] = [];
  searchTerm: string = '';
  mostrandoFormulario: boolean = false;
  isEditing: boolean = false;
  idiomaActual: any = { id: null, code: '', language: '' };

  constructor(private languageService: LanguagesService) {}

  ngOnInit(): void {
    this.cargarIdiomas();
  }

  cargarIdiomas(): void {
    this.languageService.getLanguages().subscribe((data) => {
      this.languages = data;
      this.filteredLanguages = data;
    });
  }

  onSearch(): void {
    this.filteredLanguages = this.languages.filter((language) =>
      language.language.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }

  abrirFormularioAnadir(): void {
    this.idiomaActual = { id: null, code: '', language: '' };
    this.isEditing = false;
    this.mostrandoFormulario = true;
  }

  editarIdioma(language: any): void {
    this.idiomaActual = { ...language };
    this.isEditing = true;
    this.mostrandoFormulario = true;
  }

  guardarIdioma(): void {

    if (!this.idiomaActual.language || this.idiomaActual.language.trim() === '') {
      alert('El nombre del language no puede estar vacío.');
      return;
    }

    if (!this.idiomaActual.code || this.idiomaActual.code.trim() === '') {
      alert('El codigo del language no puede estar vacío.');
      return;
    }

    if (this.isEditing) {
      this.languageService.updateLanguage(this.idiomaActual.id, this.idiomaActual).subscribe(() => {
        alert('Idioma actualizado correctamente.');
        this.cargarIdiomas();
        this.cerrarFormulario();
      });
    } else {
      this.languageService.createLanguage(this.idiomaActual).subscribe(() => {
        alert('Idioma añadido correctamente.');
        this.cargarIdiomas();
        this.cerrarFormulario();
      });
    }
  }

  eliminarIdioma(id: number): void {
    // Buscar el language en la lista cargada
    const idioma = this.languages.find(l => l.id === id);
    
    if (!idioma) {
      alert('El idioma no se encontró.');
      return;
    }
    
    // Verificar si tiene jokes asociados.
    // Se asume que la propiedad 'jokeses' viene en el objeto (si no, habría que ajustarlo según la respuesta del backend)
    if (idioma.jokeses && idioma.jokeses.length > 0) {
      // Si tiene chistes asociados, se pide confirmación específica
      if (confirm(`El language tiene ${idioma.jokeses.length} chiste(s) asociado(s). ¿Estás seguro de que quieres eliminar el language y todos sus chistes asociados?`)) {
        this.languageService.deleteLanguage(id).subscribe(() => {
          alert('Idioma eliminado correctamente.');
          this.cargarIdiomas();
        });
      }
    } else {
      // Si no tiene chistes asociados, se elimina directamente
      this.languageService.deleteLanguage(id).subscribe(() => {
        alert('Idioma eliminado correctamente.');
        this.cargarIdiomas();
      });
    }
  }  

  cerrarFormulario(): void {
    this.mostrandoFormulario = false;
  }
}
