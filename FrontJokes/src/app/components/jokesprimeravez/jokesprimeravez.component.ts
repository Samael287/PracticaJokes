import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { JokesService } from '../../services/jokes.service';
import { PrimeravezService } from '../../services/primeravez.service';
import { CommonModule } from '@angular/common';

interface PrimeraVez {
  id: number;
  programa: string;
  fechaEmision: string;
  joke: { id: number; text1: string; text2: string } | null;
  telefonos: { numero: string }[];
}

interface Jokes {
  id: number;
  categories: { id: number };
  language: { id: number };
  types: { id: number };
  text1: string;
  text2: string;
  flagses: { id: number }[];
  primeraVez: PrimeraVez;
}

@Component({
  selector: 'app-jokesprimeravez',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './jokesprimeravez.component.html',
  styleUrl: './jokesprimeravez.component.css'
})

/*export class JokesprimeravezComponent implements OnInit {
  jokes: Jokes[] = [];
  searchText: string = '';

  constructor(private jokesService: JokesService) {}

  ngOnInit(): void {
    this.cargarJokes();
  }

  cargarJokes(): void {
    this.jokesService.getJokesWithPrimeraVez().subscribe(
      (data: Jokes[]) => {
        this.jokes = data;
      },
      (error) => {
        console.error('Error al obtener los jokes', error);
      }
    );
  }

  filteredJokes(): Jokes[] {
    if (!this.searchText) {
      return this.jokes;
    }
    return this.jokes.filter(joke => 
      joke.text1.toLowerCase().includes(this.searchText.toLowerCase())
    );
  }
}*/

/*export class JokesprimeravezComponent implements OnInit {
  jokes: Jokes[] = [];
  filteredJokes: Jokes[] = [];
  searchText: string = '';

  constructor(private jokesService: JokesService) {}

  ngOnInit(): void {
    this.cargarJokes();
  }

  cargarJokes(): void {
    this.jokesService.getJokesWithPrimeraVez().subscribe(
      (data: Jokes[]) => {
        this.jokes = data;
        this.filteredJokes = [...data]; // Inicializamos con todos los datos
        console.log('Jokes', this.filteredJokes);
      },
      (error) => {
        console.error('Error al obtener los jokes', error);
      }
    );
  }

  // Filtrar en tiempo real
  filtrarJokes(): void {
    if (!this.searchText.trim()) {
      this.filteredJokes = [...this.jokes]; // Copiamos los datos originales
      return;
    }

    const textoBuscado = this.searchText.toLowerCase();
    this.filteredJokes = this.jokes.filter(joke =>
      joke.text1 && joke.text1.toLowerCase().includes(textoBuscado)
    );
  }
}*/
/*export class JokesprimeravezComponent implements OnInit {
  jokes: Jokes[] = [];
  filteredJokes: Jokes[] = [];
  searchText: string = '';

  constructor(private jokesService: JokesService) {}

  ngOnInit(): void {
    this.cargarJokes();
  }

  cargarJokes(): void {
    this.jokesService.getJokesWithPrimeraVez().subscribe(
      (data: Jokes[]) => {
        this.jokes = data;
        this.filteredJokes = [...data]; // Inicializamos con todos los datos
        console.log('Jokes cargados:', this.filteredJokes);
      },
      (error) => {
        console.error('Error al obtener los jokes', error);
      }
    );
  }

  // Filtrar en tiempo real
  filtrarJokes(): void {
    if (!this.searchText.trim()) {
      this.filteredJokes = [...this.jokes]; // Copiamos los datos originales
      return;
    }

    const textoBuscado = this.searchText.toLowerCase();
    this.filteredJokes = this.jokes.filter(joke =>
      joke.text1 && joke.text1.toLowerCase().includes(textoBuscado)
    );
  }
}*/

export class JokesprimeravezComponent implements OnInit {
  jokes: Jokes[] = [];
  filteredJokes: Jokes[] = [];
  searchText: string = '';
  filterOption: string = 'todos'; // Opción seleccionada por defecto

  constructor(private jokesService: JokesService) {}

  ngOnInit(): void {
    this.cargarJokes();
  }

  cargarJokes(): void {
    this.jokesService.getJokesWithPrimeraVez().subscribe(
      (data: Jokes[]) => {
        this.jokes = data;
        this.filteredJokes = [...data]; // Inicializamos con todos los datos
      },
      (error) => {
        console.error('Error al obtener los jokes', error);
      }
    );
  }

  // Filtrar en tiempo real
  filtrarJokes(): void {
    let jokesFiltrados = [...this.jokes];

    if (this.searchText.trim()) {
      const textoBuscado = this.searchText.toLowerCase();
      jokesFiltrados = jokesFiltrados.filter(joke =>
        joke.text1 && joke.text1.toLowerCase().includes(textoBuscado)
      );
    }

    this.aplicarFiltro(jokesFiltrados);
  }

  // Filtrar por Primera Vez (con/sin/todos)
  filtrarPorPrimeraVez(opcion: string): void {
    this.filterOption = opcion;
    this.aplicarFiltro(this.jokes);
  }

  aplicarFiltro(jokes: Jokes[]): void {
    switch (this.filterOption) {
      case 'conPrimeraVez':
        this.filteredJokes = jokes.filter(joke => joke.primeraVez !== null);
        break;
      case 'sinPrimeraVez':
        this.filteredJokes = jokes.filter(joke => joke.primeraVez === null);
        break;
      default:
        this.filteredJokes = [...jokes];
        break;
    }
  }
}
