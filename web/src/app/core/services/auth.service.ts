import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginRequest, LoginResponse, RegisterRequest } from '../models/auth.model';
import { UserResponse } from '../models/user.model';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly tokenService = inject(TokenService);
  private readonly router = inject(Router);
  private readonly apiUrl = 'http://localhost:8080/auth';

  private currentUserSignal = signal<UserResponse | null>(this.tokenService.getUser());
  
  // Computed signal for easy usage across the app
  currentUser = this.currentUserSignal.asReadonly();
  
  isAdmin = computed(() => {
    const user = this.currentUserSignal();
    return !!user?.roles?.includes('ROLE_ADMIN');
  });

  login(data: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, data).pipe(
      tap(response => {
        this.tokenService.saveToken(response.token);
        this.tokenService.saveUser(response.user);
        this.currentUserSignal.set(response.user);
      })
    );
  }

  register(data: RegisterRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${this.apiUrl}/register`, data);
  }

  logout() {
    this.tokenService.clear();
    this.currentUserSignal.set(null);
    this.router.navigate(['/login']);
  }
}
