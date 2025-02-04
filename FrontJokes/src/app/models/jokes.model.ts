import { PrimeraVez } from "./primera-vez.model";

// jokes.model.ts
export class Jokes {
    categories: { id: number }; // Categoría referenciada por ID
    language: { id: number }; // Idioma referenciado por ID
    types: { id: number }; // Tipo referenciado por ID
    text1: string; // Primera parte del texto del chiste
    text2: string; // Segunda parte del texto del chiste
    flagses: { id: number }[]; // Lista de banderas referenciadas por ID
    primeravez: PrimeraVez | undefined;
  
    constructor(
      categories: { id: number },
      language: { id: number },
      types: { id: number },
      text1: string,
      text2: string,
      flagses: { id: number }[],
      primeravez: PrimeraVez
    ) {
      this.categories = categories;
      this.language = language;
      this.types = types;
      this.text1 = text1;
      this.text2 = text2;
      this.flagses = flagses;
      this.primeravez = primeravez;
    }
  }
  