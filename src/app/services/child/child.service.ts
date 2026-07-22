import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Crianca, ChildCreateRequest, ChildUpdateRequest } from '../../models/crianca.model';

@Injectable({
  providedIn: 'root'
})
export class ChildService {
  private readonly API_URL = `${environment.apiUrl}/children`;
  
  private selectedChildSubject = new BehaviorSubject<string | null>(this.getStoredChildId());
  public selectedChild$ = this.selectedChildSubject.asObservable();

  constructor(private http: HttpClient) {}

  private getStoredChildId(): string | null {
    return localStorage.getItem('vacina_kids_crianca_selecionada');
  }

  public get selectedChildId(): string | null {
    return this.selectedChildSubject.value;
  }

  public selectChild(id: string): void {
    localStorage.setItem('vacina_kids_crianca_selecionada', id);
    this.selectedChildSubject.next(id);
  }

  public getChildren(): Observable<any> {
    // Spring Boot Page<ChildResponse> format expects to be read from response.content
    return this.http.get<any>(this.API_URL);
  }

  public getChild(id: string): Observable<Crianca> {
    return this.http.get<Crianca>(`${this.API_URL}/${id}`);
  }

  public createChild(child: ChildCreateRequest): Observable<Crianca> {
    return this.http.post<Crianca>(this.API_URL, child);
  }

  public updateChild(id: string, child: ChildUpdateRequest): Observable<Crianca> {
    return this.http.put<Crianca>(`${this.API_URL}/${id}`, child);
  }

  public deleteChild(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`).pipe(
      tap(() => {
        if (this.selectedChildId === id) {
          localStorage.removeItem('vacina_kids_crianca_selecionada');
          this.selectedChildSubject.next(null);
        }
      })
    );
  }
}
