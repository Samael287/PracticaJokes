import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LanguagesService {

  private baseUrl = 'http://localhost:8080/api/languages';
   
     constructor(private http: HttpClient) {}
  
    
    // Método para obtener todos los lenguages disponibles
    getLanguages(): Observable<any[]> {
      return this.http.get<any[]>(this.baseUrl);
    }

    getLanguageById(id: number): Observable<any> {
      return this.http.get(`${this.baseUrl}/${id}`);
    }

    createLanguage(language: any): Observable<any> {
      return this.http.post(this.baseUrl, language);
    }

    updateLanguage(id: number, language: any): Observable<any> {
      return this.http.put(`${this.baseUrl}/${id}`, language);
    }

    deleteLanguage(id: number): Observable<any> {
      return this.http.delete(`${this.baseUrl}/${id}`);
    }
}
