import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { LoginRequest, LoginResponse, RegisterRequest, RefreshTokenResponse } from '../models/auth.model';
import { TokenService } from './token.service';
import { UserResponse } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly tokenService = inject(TokenService);
  private readonly apiUrl = 'http://localhost:8080';

  currentUser = signal<UserResponse | null>(null);

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, credentials).pipe(
      tap(response => {
        this.tokenService.setToken(response.token);
        this.currentUser.set(response.user);
      })
    );
  }

  register(data: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/auth/register`, data);
  }

  logout(): void {
    this.tokenService.removeToken();
    this.currentUser.set(null);
  }

  refreshToken(): Observable<RefreshTokenResponse> {
    // Note: This relies on the refreshToken cookie being sent automatically by the browser
    return this.http.patch<RefreshTokenResponse>(`${this.apiUrl}/token/refresh`, {}, { withCredentials: true }).pipe(
      tap(response => this.tokenService.setToken(response.token))
    );
  }
}
