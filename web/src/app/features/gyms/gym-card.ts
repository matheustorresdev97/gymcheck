import { Component, Input, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GymResponse } from '../../core/models/gym.model';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import { ToastService } from '../../core/services/toast.service';
import { CheckInService } from '../../core/services/check-in.service';
import { AppCardComponent } from '../../shared/components/card';
import { AppBadgeComponent } from '../../shared/components/badge';
import { AppButtonComponent } from '../../shared/components/button';

@Component({
  selector: 'app-gym-card',
  standalone: true,
  imports: [CommonModule, AppCardComponent, AppBadgeComponent, AppButtonComponent],
  template: `
    <app-card [hoverable]="true" [glass]="true" padding="lg" class="h-full flex flex-col">
      <div class="flex justify-between items-start mb-6">
        <div class="space-y-1">
          <h3 class="text-xl font-extrabold text-white group-hover:text-emerald-400 transition-colors uppercase tracking-tighter leading-tight">
            {{ gym.title }}
          </h3>
          <p class="text-[10px] text-emerald-500/60 font-bold uppercase tracking-[0.2em]">Premium Box</p>
        </div>
        
        <app-badge variant="neutral">
          <svg icon xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0Z"/><circle cx="12" cy="10" r="3"/></svg>
          {{ distance }} km
        </app-badge>
      </div>
      
      <p class="text-neutral-500 text-sm mb-auto line-clamp-3 font-medium leading-relaxed">
        {{ gym.description || 'Esta academia ainda não forneceu uma descrição detalhada das suas instalações e serviços.' }}
      </p>

      <div class="mt-8 pt-6 border-t border-neutral-800/50 flex flex-col gap-3">
        @if (authService.isAdmin()) {
          <app-button variant="secondary" size="sm" [fullWidth]="true" [disabled]="true">
            Modo administrador
          </app-button>
        } @else {
          <app-button 
            [variant]="'primary'" 
            [isLoading]="isLoading()" 
            [fullWidth]="true" 
            (onClick)="handleCheckIn($event)"
          >
            <svg class="w-4 h-4 mr-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
              <polyline points="20 6 9 17 4 12"/>
            </svg>
            Fazer Check-in
          </app-button>
        }
      </div>
    </app-card>
  `
})
export class GymCardComponent {
  @Input({ required: true }) gym!: GymResponse;
  
  authService = inject(AuthService);
  private checkInService = inject(CheckInService);
  private router = inject(Router);
  private toastService = inject(ToastService);

  isLoading = signal(false);
  // Simulating a dynamic distance for visual impact - in a real app this would come from a service
  readonly distance = (Math.random() * 2.5 + 0.1).toFixed(1);

  handleCheckIn(event: Event) {
    if (!this.authService.currentUser()) {
      this.toastService.info('Sessão necessária. Faça login para continuar o check-in.');
      this.router.navigate(['/login']);
      return;
    }

    if (this.isLoading()) return;
    this.isLoading.set(true);

    navigator.geolocation.getCurrentPosition(
      () => {
        this.performCheckIn(Number(this.gym.latitude), Number(this.gym.longitude));
      },
      (error) => {
        this.isLoading.set(false);
        console.error('Geolocation error:', error);
        this.toastService.error('Acesso à localização negado. Verifique as permissões do seu navegador.');
      },
      { enableHighAccuracy: true, timeout: 5000, maximumAge: 0 }
    );
  }

  private performCheckIn(latitude: number, longitude: number) {
    this.checkInService.create(this.gym.id, { latitude, longitude }).subscribe({
      next: () => {
        this.toastService.success(`Check-in confirmado! Bom treino na ${this.gym.title}.`);
        this.router.navigate(['/history']);
      },
      error: () => {
        this.isLoading.set(false);
        // Error messages are now handled by the errorInterceptor
      }
    });
  }
}
