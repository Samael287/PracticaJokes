import { Component, OnInit } from '@angular/core';
import { JokesService } from '../../services/jokes.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';


interface Joke {
  id: number;
	categories: String;
	language: String;
  types: String;
  text1: String;
  text2: String;
}

@Component({
  selector: 'app-jokes',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './jokes.component.html',
  styleUrl: './jokes.component.css'
})

export class JokesComponent implements OnInit {
  jokes: Joke[] = [];
  searchTerm: string = '';
  selectedJoke: Joke | null = null;

  // Variables de paginación
  currentPage: number = 1;
  itemsPerPage: number = 50;
  totalPages: number = 0;
  showConfirmation: boolean | undefined;

  constructor(private jokesService: JokesService, private router: Router) {}

  ngOnInit(): void {
    this.loadJokes();  // Cargar los chistes cuando se inicializa el componente
  }

  // Cargar los chistes desde el backend
  loadJokes(): void {
    this.jokesService.getJokes().subscribe({
      next: (data) => {
        this.jokes = data;
        this.calculateTotalPages();  // Calcula el total de páginas después de cargar los chistes
      },
      error: (err) => console.error('Error loading jokes:', err)
    });
  }

  // Calcular el total de páginas basado en los chistes filtrados
  calculateTotalPages(): void {
    const filteredJokes = this.filteredJokes();
    this.totalPages = Math.ceil(filteredJokes.length / this.itemsPerPage);
  }

  // Filtrar los chistes según el término de búsqueda
  filteredJokes(): Joke[] {
    // Si no hay término de búsqueda, devolver todos los chistes.
    if (!this.searchTerm || !this.searchTerm.trim()) {
      return this.jokes;  // Devuelve todos los chistes si no hay búsqueda activa.
    }

    // Normaliza el término de búsqueda a minúsculas.
    const searchTermLower = this.searchTerm.toLowerCase();

    // Filtra los chistes basándote en si alguna de sus propiedades contiene el término de búsqueda.
    return this.jokes.filter((joke) => {
      const fields = [
        joke.text1 ?? '',
        joke.text2 ?? '',
        joke.language ?? '',
        joke.categories ?? '',
        joke.types ?? ''
      ];

      // Comprueba si algún campo contiene el término de búsqueda.
      return fields.some((field) => field.toLowerCase().includes(searchTermLower));
    });
  }

  // Paginación: Dividir los chistes en páginas
  paginate(jokes: Joke[]): Joke[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    return jokes.slice(startIndex, endIndex);
  }

  // Cambiar de página
  changePage(newPage: number): void {
    if (newPage >= 1 && newPage <= this.totalPages) {
      this.currentPage = newPage;
    }
  }

  // Obtener los chistes para la página actual
  getCurrentPageJokes(): Joke[] {
    const filtered = this.filteredJokes();  // Aplica la búsqueda
    this.calculateTotalPages();  // Recalcula las páginas con los chistes filtrados
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.paginate(filtered);  // Devuelve los chistes para la página actual
  }

  // Ir a una página específica
  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
  }

  // Seleccionar un chiste
  selectJoke(joke: Joke): void {
    this.selectedJoke = joke;
    console.log('Selected joke:', this.selectedJoke);
    if (!this.selectedJoke.id) {
      console.error('Selected joke has no ID:', this.selectedJoke);
    }
  }

  // Eliminar un chiste
  /*deleteJoke(id: number | undefined): void {
    if (!id) return;
    this.jokesService.deleteJoke(id).subscribe({
      next: () => {
        this.jokes = this.jokes.filter((j) => j.id !== id);
        this.selectedJoke = null;  // Limpiar la selección
        this.calculateTotalPages();  // Recalcular las páginas después de eliminar un chiste
      },
      error: (err) => console.error('Error deleting joke:', err)
    });
  }*/

    deleteJoke(id: number | undefined): void {
      if (!id) return;
    
      // Verifica si se puede eliminar el chiste
      this.jokesService.canDeleteJoke(id).subscribe({
        next: (hasFlags: boolean) => {
          if (hasFlags) {
            // Mostrar confirmación si tiene flags asociados
            this.showConfirmation = true;
            this.selectedJoke = this.jokes.find((joke) => joke.id === id) ?? null;
          } else {
            // Elimina directamente si no tiene flags
            this.confirmDelete();
          }
        },
        error: (err: any) => console.error('Error verificando flags:', err),
      });
    }
    
    // Confirmar eliminación
    confirmDelete(): void {
      if (!this.selectedJoke?.id) return;
    
      this.jokesService.deleteJoke(this.selectedJoke.id).subscribe({
        next: () => {
          this.jokes = this.jokes.filter((j) => j.id !== this.selectedJoke?.id);
          this.selectedJoke = null; // Limpiar la selección
          this.calculateTotalPages(); // Recalcular las páginas después de eliminar un chiste
          this.showConfirmation = false; // Ocultar la ventana de confirmación
          alert('Chiste eliminado correctamente.');
        },
        error: (err) => {
          console.error('Error eliminando el chiste:', err);
          this.showConfirmation = false; // Ocultar la ventana de confirmación
        },
      });
    }
    
    // Cancelar eliminación
    cancelDelete(): void {
      this.showConfirmation = false;
      this.selectedJoke = null;
    }

  // Obtener los números de las páginas
  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i + 1);
  }
}