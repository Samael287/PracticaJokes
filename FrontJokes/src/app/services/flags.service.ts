import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FlagsService {
  private baseUrl = 'http://localhost:8080/api/flags';

  constructor(private http: HttpClient) {}

  getFlags(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  getFlagById(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  getFlagsWithJokes(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/jokesflags');
  }

  createFlag(flag: any): Observable<any> {
    return this.http.post(this.baseUrl, flag);
  }

  updateFlag(id: number, flag: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, flag);
  }

  deleteFlag(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
