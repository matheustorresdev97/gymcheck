import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GymResponse } from '../../core/models/gym.model';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-gym-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="bg-neutral-900 border border-neutral-800 rounded-3xl p-6 hover:border-emerald-500/50 transition-all duration-300 group shadow-lg hover:shadow-emerald-500/5">
      <div class="flex justify-between items-start mb-4">
        <h3 class="text-xl font-bold text-white group-hover:text-emerald-400 transition-colors uppercase tracking-tight">{{ gym.title }}</h3>
        <span class="text-xs font-bold bg-neutral-800 text-neutral-400 px-3 py-1.5 rounded-full flex items-center gap-1.5">
          <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0Z"/><circle cx="12" cy="10" r="3"/></svg>
          {{ (Math.random() * 5).toFixed(1) }} km
        </span>
      </div>
      
      <p class="text-neutral-500 text-sm mb-8 line-clamp-2 min-h-[40px]">
        {{ gym.description || 'Nenhuma descrição fornecida para esta academia.' }}
      </p>

      <button 
        (click)="handleCheckIn($event)"
        class="w-full flex items-center justify-center gap-2 bg-neutral-800 hover:bg-emerald-500 hover:text-black text-emerald-400 font-bold uppercase text-xs py-4 rounded-xl transition-all duration-300 border border-emerald-500/20 group-hover:border-transparent"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
        Fazer Check-in
      </button>
    </div>
  `
})
export class GymCardComponent {
  @Input({ required: true }) gym!: GymResponse;
  
  private authService = inject(AuthService);
  private router = inject(Router);
  private toastService = inject(ToastService);

  protected Math = Math;

  handleCheckIn(event: Event) {
    event.stopPropagation();
    
    if (!this.authService.currentUser()) {
      this.toastService.info('Você precisa estar logado para fazer check-in.');
      this.router.navigate(['/login']);
      return;
    }

    // Logic for check-in will go here in next steps
    this.toastService.info('Funcionalidade de check-in em breve!');
  }
}
