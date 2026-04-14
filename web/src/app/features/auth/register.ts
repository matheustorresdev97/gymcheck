import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="min-h-[calc(100vh-80px)] flex items-center justify-center p-6 pb-20">
      <div class="w-full max-w-[440px] bg-neutral-900 border border-neutral-800 rounded-3xl p-8 shadow-2xl animate-in fade-in zoom-in-95 duration-500">
        <div class="text-center mb-10">
          <h1 class="text-4xl font-extrabold text-white tracking-tight mb-2 uppercase">GYM<span class="text-emerald-500">CHECK</span></h1>
          <p class="text-neutral-400 font-bold uppercase tracking-widest text-xs">O seu passe para a forma</p>
        </div>

        <div class="h-px bg-gradient-to-r from-transparent via-emerald-500/50 to-transparent mb-10"></div>

        <h2 class="text-2xl font-bold text-white mb-8 uppercase tracking-tight">Criar Conta</h2>

        <form [formGroup]="registerForm" (ngSubmit)="onSubmit()" class="space-y-6">
          <div class="space-y-2">
            <label for="name" class="text-xs font-bold text-neutral-500 uppercase tracking-widest ml-1">Nome de Atleta</label>
            <input 
              id="name"
              type="text" 
              formControlName="name"
              placeholder="Ex: John Doe"
              class="w-full bg-neutral-950 border border-neutral-800 rounded-xl p-4 text-white placeholder-neutral-700 outline-none focus:border-emerald-500/50 focus:ring-4 focus:ring-emerald-500/5 transition-all duration-300"
            />
          </div>

          <div class="space-y-2">
            <label for="email" class="text-xs font-bold text-neutral-500 uppercase tracking-widest ml-1">E-mail</label>
            <input 
              id="email"
              type="email" 
              formControlName="email"
              placeholder="atleta&#64;exemplo.com"
              class="w-full bg-neutral-950 border border-neutral-800 rounded-xl p-4 text-white placeholder-neutral-700 outline-none focus:border-emerald-500/50 focus:ring-4 focus:ring-emerald-500/5 transition-all duration-300"
            />
          </div>

          <div class="space-y-2">
            <label for="password" class="text-xs font-bold text-neutral-500 uppercase tracking-widest ml-1">Palavra-passe</label>
            <input 
              id="password"
              type="password" 
              formControlName="password"
              placeholder="••••••••"
              class="w-full bg-neutral-950 border border-neutral-800 rounded-xl p-4 text-white placeholder-neutral-700 outline-none focus:border-emerald-500/50 focus:ring-4 focus:ring-emerald-500/5 transition-all duration-300"
            />
          </div>

          <button 
            type="submit"
            [disabled]="registerForm.invalid || isLoading()"
            class="w-full bg-emerald-600 hover:bg-emerald-500 disabled:opacity-50 disabled:cursor-not-allowed text-white font-extrabold uppercase py-4 rounded-xl transition-all duration-300 shadow-[0_0_20px_-5px_rgba(16,185,129,0.3)] active:scale-95"
          >
            <span *ngIf="!isLoading()">Registar</span>
            <span *ngIf="isLoading()" class="flex items-center justify-center gap-2">
              <svg class="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
              Criando conta...
            </span>
          </button>
        </form>

        <p class="text-center mt-10 text-neutral-500 text-sm font-medium">
          Já tem conta? 
          <a routerLink="/login" class="text-emerald-500 font-bold hover:underline underline-offset-4">Inicie sessão</a>
        </p>
      </div>
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
          this.toastService.success('Conta criada com sucesso! Faça login para começar.');
          this.router.navigate(['/login']);
        },
        error: (err) => {
          this.isLoading.set(false);
          this.toastService.error(err.error?.message || 'Erro ao criar conta. Tente outro e-mail.');
        }
      });
    }
  }
}
