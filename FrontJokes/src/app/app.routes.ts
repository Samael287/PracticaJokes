import { Routes } from '@angular/router';
import { JokesComponent } from './components/jokes/jokes.component';
import { FlagsComponent } from './components/flags/flags.component';
import { AddJokeComponent } from './components/jokes/add-joke/add-joke.component';
import { EditJokeComponent } from './components/jokes/edit-joke/edit-joke.component';
import { AddFlagComponent } from './components/flags/add-flag/add-flag.component';
import { EditFlagComponent } from './components/flags/edit-flag/edit-flag.component';
import { CategoriesComponent } from './components/categories/categories.component';
import { LanguagesComponent } from './components/languages/languages.component';
import { TypesComponent } from './components/types/types.component';
import { PrimeraVezComponent } from './components/primeravez/primeravez.component';
import { JokesprimeravezComponent } from './components/jokesprimeravez/jokesprimeravez.component';
import { JokesflagsComponent } from './components/jokesflags/jokesflags.component';

export const routes: Routes = [
  { path: 'jokes', component: JokesComponent },
  { path: 'flags', component: FlagsComponent },
  { path: 'add-joke', component: AddJokeComponent },
  { path: 'edit-joke/:id', component: EditJokeComponent },
  { path: 'add-flag', component: AddFlagComponent },
  { path: 'edit-flag/:id', component: EditFlagComponent },
  { path: 'categories', component: CategoriesComponent },
  { path: 'languages', component: LanguagesComponent },
  { path: 'types', component: TypesComponent },
  { path: 'primera-vez', component: PrimeraVezComponent },
  { path: 'jokesprimeravez', component: JokesprimeravezComponent },
  { path: 'jokesflags', component: JokesflagsComponent },
  { path: '', redirectTo: '/jokes', pathMatch: 'full' },
];
