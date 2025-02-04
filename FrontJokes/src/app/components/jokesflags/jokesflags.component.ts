import { Component, OnInit } from '@angular/core';
import { FlagsService } from '../../services/flags.service';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

interface Joke {
  id: number;
  text1: string;
  language: string;
}

interface Flag {
  id: number;
  flag: string;
  jokes: Joke[];
}

@Component({
  selector: 'app-jokesflags',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './jokesflags.component.html',
  styleUrl: './jokesflags.component.css'
})

/*xport class JokesflagsComponent implements OnInit {
  flags: Flag[] = [];
  filteredFlags: Flag[] = [];
  searchText: string = '';

  constructor(private flagsService: FlagsService) {}

  ngOnInit(): void {
    this.cargarFlags();
  }

  cargarFlags(): void {
    this.flagsService.getFlagsWithJokes().subscribe(
      (data: Flag[]) => {
        this.flags = data;
        this.filteredFlags = [...data]; // Copiamos los datos originales
        console.log('Flags cargadas:', this.filteredFlags);
      },
      (error) => {
        console.error('Error al obtener los flags', error);
      }
    );
  }

  // Filtrar en tiempo real por nombre de Flag
  filterFlags(): void {
    if (!this.searchText.trim()) {
      this.filteredFlags = [...this.flags];
      return;
    }

    const textoBuscado = this.searchText.toLowerCase();
    this.filteredFlags = this.flags.filter(flag =>
      flag.flag.toLowerCase().includes(textoBuscado)
    );
  }
}*/

export class JokesflagsComponent implements OnInit {
  flags: Flag[] = [];
  filteredFlags: Flag[] = [];
  searchText: string = '';

  constructor(private flagsService: FlagsService) {}

  ngOnInit(): void {
    this.cargarFlags();
  }

  cargarFlags(): void {
    this.flagsService.getFlagsWithJokes().subscribe(
      (data: any[]) => {
        this.flags = data.map(flag => ({
          ...flag,
          jokes: flag.jokeses.map((jokeString: string) => {
            if (typeof jokeString !== 'string') {
              console.warn("jokeString no es un string válido:", jokeString);
              return { id: 0, text1: "Desconocido", language: "Sin idioma" };
            }

            console.log("Procesando jokeString:", jokeString); // ✅ Debugging

            // ✅ Expresión regular para extraer los datos correctamente
            const regex = /ID: (\d+), Texto1: (.*?), Idioma: (.*)/;
            const match = jokeString.match(regex);

            if (!match || match.length < 4) {
              console.warn("⚠️ No se pudo extraer información de:", jokeString);
              return { id: 0, text1: "Desconocido", language: "Sin idioma" };
            }

            return {
              id: parseInt(match[1]),
              text1: match[2].trim() !== "Sin texto" ? match[2].trim() : "Sin texto",
              language: match[3].trim()
            };
          })
        }));

        this.filteredFlags = [...this.flags]; // ✅ Copiar datos corregidos
        console.log('✅ Flags procesadas correctamente:', this.filteredFlags);
      },
      (error) => {
        console.error('❌ Error al obtener los flags:', error);
      }
    );
  }

  // 🔎 Filtrar en tiempo real por nombre de Flag
  filterFlags(): void {
    if (!this.searchText.trim()) {
      this.filteredFlags = [...this.flags];
      return;
    }

    const textoBuscado = this.searchText.toLowerCase();
    this.filteredFlags = this.flags.filter(flag =>
      flag.flag.toLowerCase().includes(textoBuscado)
    );
  }
}
