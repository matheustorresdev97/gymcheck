import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { CommonModule } from '@angular/common';
import { AppInputComponent } from '../../shared/components/input';
import { AppButtonComponent } from '../../shared/components/button';
import { AppCardComponent } from '../../shared/components/card';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, AppInputComponent, AppButtonComponent, AppCardComponent],
  template: `
    <div class="min-h-[calc(100vh-80px)] flex items-center justify-center p-6 pb-20 overflow-hidden relative">
      <!-- Decorative Background Elements -->
      <div class="absolute top-1/4 -left-20 w-80 h-80 bg-emerald-500/10 rounded-full blur-[120px] animate-pulse"></div>
      <div class="absolute bottom-1/4 -right-20 w-80 h-80 bg-emerald-500/5 rounded-full blur-[120px] animate-pulse delay-700"></div>

      <app-card [glass]="true" padding="lg" class="w-full max-w-[440px] shadow-2xl animate-in fade-in zoom-in-95 duration-700 relative z-10">
        <div class="text-center mb-10">
          <div class="inline-flex items-center justify-center w-16 h-16 bg-emerald-500/10 rounded-2xl mb-6 group transition-all duration-500 hover:scale-110">
             <h1 class="text-2xl font-black text-emerald-500 tracking-tighter uppercase">GYM</h1>
          </div>
          <h2 class="text-4xl font-extrabold text-white tracking-tighter mb-2 uppercase italic">GYM<span class="text-emerald-500">CHECK</span></h2>
          <p class="text-neutral-500 font-bold uppercase tracking-[0.3em] text-[10px]">Acesso ao Atleta</p>
        </div>

        <div class="h-px bg-gradient-to-r from-transparent via-neutral-800 to-transparent mb-10"></div>

        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()" class="space-y-6">
          <app-input 
            label="E-mail"
            id="email"
            type="email" 
            formControlName="email"
            placeholder="seu&#64;email.com"
            [icon]="true"
            [error]="loginForm.get('email')?.touched && loginForm.get('email')?.invalid ? 'E-mail inválido' : ''"
          >
            <svg icon xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><rect width="20" height="16" x="2" y="4" rx="2"/><path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/></svg>
          </app-input>

          <app-input 
            label="Palavra-passe"
            id="password"
            type="password" 
            formControlName="password"
            placeholder="••••••••"
            [icon]="true"
            [error]="loginForm.get('password')?.touched && loginForm.get('password')?.invalid ? 'Mínimo 6 caracteres' : ''"
          >
            <svg icon xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><rect width="18" height="11" x="3" y="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
          </app-input>

          <div class="pt-4">
            <app-button 
              type="submit"
              [disabled]="loginForm.invalid"
              [isLoading]="isLoading()"
              [fullWidth]="true"
              size="lg"
            >
              Iniciar Sessão
            </app-button>
          </div>
        </form>

        <p class="text-center mt-10 text-neutral-500 text-xs font-semibold tracking-tight">
          Novo no Gymcheck? 
          <a routerLink="/register" class="text-emerald-500 font-bold hover:text-emerald-400 transition-colors border-b border-emerald-500/20 hover:border-emerald-500 ml-1">Crie sua conta</a>
        </p>
      </app-card>
    </div>
  `
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private toastService = inject(ToastService);
  private router = inject(Router);

  isLoading = signal(false);

  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  onSubmit() {
    if (this.loginForm.valid) {
      this.isLoading.set(true);
      this.authService.login(this.loginForm.value as any).subscribe({
        next: () => {
          this.toastService.success('Bem-vindo ao campo de treino!');
          this.router.navigate(['/gyms']);
        },
        error: () => {
          this.isLoading.set(false);
          // Errors are handled by the interceptor
        }
      });
    }
  }
}
