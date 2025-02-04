import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class JokesService {
  private apiUrl = 'http://localhost:8080/api/jokes';
  private flagsUrl = 'http://localhost:8080/api/flags';
  private languagesUrl = 'http://localhost:8080/api/languages';
  private categoriesUrl = 'http://localhost:8080/api/categories';
  private typesUrl = 'http://localhost:8080/api/types';

  constructor(private http: HttpClient) {}

  getJokes(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getJokeById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  getJokesWithPrimeraVez(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/primeravez`);
  }

  createJoke(joke: any): Observable<any> {
    return this.http.post(this.apiUrl, joke);
  }

  updateJoke(id: number, joke: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, joke);
  }

  deleteJoke(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  canDeleteJoke(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/deleteSoft/${id}`);
  }

 /* getFlags(): Observable<any> {
    return this.http.get(this.flagsUrl);
  }

  getLanguages(): Observable<any> {
    return this.http.get(this.languagesUrl);
  }

  getCategories(): Observable<any> {
    return this.http.get(this.categoriesUrl);
  }

  getTypes(): Observable<any> {
    return this.http.get(this.typesUrl);
  }*/
}