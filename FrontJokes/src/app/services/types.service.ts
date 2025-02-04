import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TypesService {

 private baseUrl = 'http://localhost:8080/api/types';
    
      constructor(private http: HttpClient) {}
   
     // Método para obtener todos los types disponibles
     getTypes(): Observable<any[]> {
       return this.http.get<any[]>(this.baseUrl);
     }

      getTypeById(id: number): Observable<any> {
        return this.http.get(`${this.baseUrl}/${id}`);
      }

      createType(type: any): Observable<any> {
        return this.http.post(this.baseUrl, type);
      }

      updateType(id: number, type: any): Observable<any> {
        return this.http.put(`${this.baseUrl}/${id}`, type);
      }

      deleteType(id: number): Observable<any> {
        return this.http.delete(`${this.baseUrl}/${id}`);
      }
}
