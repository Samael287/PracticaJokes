import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PrimeravezService {

  private baseUrl = 'http://localhost:8080/api/primeravez';

  constructor(private http: HttpClient) { }

  getPrimeravez(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  getPrimeravezById(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createPrimeravez(primeravez: any): Observable<any> {
    return this.http.post(this.baseUrl, primeravez);
  }

  updatePrimeravez(id: number, primeravez: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, primeravez);
  }

  deletePrimeravez(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
