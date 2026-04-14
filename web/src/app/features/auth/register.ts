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
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, AppInputComponent, AppButtonComponent, AppCardComponent],
  template: `
    <div class="min-h-[calc(100vh-80px)] flex items-center justify-center p-6 pb-20 overflow-hidden relative">
      <!-- Decorative Background Elements -->
      <div class="absolute top-1/4 -right-20 w-80 h-80 bg-emerald-500/10 rounded-full blur-[120px] animate-pulse"></div>
      <div class="absolute bottom-1/4 -left-20 w-80 h-80 bg-emerald-500/5 rounded-full blur-[120px] animate-pulse delay-700"></div>

      <app-card [glass]="true" padding="lg" class="w-full max-w-[440px] shadow-2xl animate-in fade-in zoom-in-95 duration-700 relative z-10">
        <div class="text-center mb-10">
          <div class="inline-flex items-center justify-center w-16 h-16 bg-emerald-500/10 rounded-2xl mb-6 group transition-all duration-500 hover:scale-110">
             <svg xmlns="http://www.w3.org/2000/svg" class="w-8 h-8 text-emerald-500" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><line x1="19" y1="8" x2="19" y2="14"/><line x1="16" y1="11" x2="22" y2="11"/></svg>
          </div>
          <h2 class="text-4xl font-extrabold text-white tracking-tighter mb-2 uppercase italic">Junte-se à <span class="text-emerald-500">Elite</span></h2>
          <p class="text-neutral-500 font-bold uppercase tracking-[0.3em] text-[10px]">Novo Perfil de Atleta</p>
        </div>

        <div class="h-px bg-gradient-to-r from-transparent via-neutral-800 to-transparent mb-10"></div>

        <form [formGroup]="registerForm" (ngSubmit)="onSubmit()" class="space-y-6">
          <app-input 
            label="Nome Completo"
            id="name"
            type="text" 
            formControlName="name"
            placeholder="Seu nome aqui"
            [icon]="true"
            [error]="registerForm.get('name')?.touched && registerForm.get('name')?.invalid ? 'Nome é obrigatório' : ''"
          >
            <svg icon xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
          </app-input>

          <app-input 
            label="E-mail"
            id="email"
            type="email" 
            formControlName="email"
            placeholder="atleta&#64;exemplo.com"
            [icon]="true"
            [error]="registerForm.get('email')?.touched && registerForm.get('email')?.invalid ? 'E-mail inválido' : ''"
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
            [error]="registerForm.get('password')?.touched && registerForm.get('password')?.invalid ? 'Mínimo 6 caracteres' : ''"
          >
            <svg icon xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><rect width="18" height="11" x="3" y="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
          </app-input>

          <div class="pt-4">
            <app-button 
              type="submit"
              [disabled]="registerForm.invalid"
              [isLoading]="isLoading()"
              [fullWidth]="true"
              size="lg"
            >
              Criar Perfil
            </app-button>
          </div>
        </form>

        <p class="text-center mt-10 text-neutral-500 text-xs font-semibold tracking-tight">
          Já faz parte da rede? 
          <a routerLink="/login" class="text-emerald-500 font-bold hover:text-emerald-400 transition-colors border-b border-emerald-500/20 hover:border-emerald-500 ml-1">Inicie sessão</a>
        </p>
      </app-card>
    </div>
  `
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private toastService = inject(ToastService);
  private router = inject(Router);

  isLoading = signal(false);

  registerForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  onSubmit() {
    if (this.registerForm.valid) {
      this.isLoading.set(true);
      this.authService.register(this.registerForm.value as any).subscribe({
        next: () => {
          this.toastService.success('Boas-vindas ao Gymcheck! Agora é só entrar.');
          this.router.navigate(['/login']);
        },
        error: () => {
          this.isLoading.set(false);
          // Errors are handled by the interceptor
        }
      });
    }
  }
}
