import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { JwtResponse, UserResponse } from '../../models/auth/auth.models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/auth`;
  
  private currentUserSubject = new BehaviorSubject<UserResponse | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private accessToken: string | null = null;
  private refreshToken: string | null = null;

  constructor() {}

  public get currentUserValue(): UserResponse | null {
    return this.currentUserSubject.value;
  }

  public get isAuthenticated(): boolean {
    return !!this.accessToken;
  }

  public getAccessToken(): string | null {
    return this.accessToken;
  }

  public login(credentials: any): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${this.API_URL}/login`, credentials).pipe(
      tap(response => this.setSession(response))
    );
  }

  public register(data: any): Observable<any> {
    return this.http.post(`${this.API_URL}/register`, data);
  }

  private setSession(authResult: JwtResponse): void {
    this.accessToken = authResult.accessToken;
    this.refreshToken = authResult.refreshToken;
    this.currentUserSubject.next(authResult.user);
  }

  public logout(): void {
    this.accessToken = null;
    this.refreshToken = null;
    
    // Clean up local vacina service state to prevent data leak
    localStorage.removeItem('vacina_kids_crianca_selecionada');
    
    this.currentUserSubject.next(null);
  }
}
