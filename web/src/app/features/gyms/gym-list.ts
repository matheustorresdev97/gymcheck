import { Component, inject, signal, OnInit, PLATFORM_ID, afterNextRender } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GymService } from '../../core/services/gym.service';
import { GymResponse } from '../../core/models/gym.model';
import { GymCardComponent } from './gym-card';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-gym-list',
  standalone: true,
  imports: [CommonModule, FormsModule, GymCardComponent],
  template: `
    <div class="max-w-7xl mx-auto px-6 py-12">
      <header class="mb-12">
        <h1 class="text-4xl font-extrabold text-white uppercase tracking-tight mb-2">Academias Disponíveis</h1>
        <p class="text-neutral-500 font-medium font-bold">Encontre a sua próxima box ou academia perto de si.</p>
      </header>

      <!-- Search Section -->
      <div class="relative mb-12 group">
        <div class="absolute inset-y-0 left-5 flex items-center pointer-events-none">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" class="text-neutral-600 group-focus-within:text-emerald-500 transition-colors"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
        </div>
        <input 
          type="text" 
          [(ngModel)]="searchQuery" 
          (input)="onSearch()"
          placeholder="Pesquisar por nome (ex: JS Academy)..."
          class="w-full bg-neutral-900 border border-neutral-800 rounded-2xl py-5 pl-14 pr-6 text-white placeholder-neutral-700 outline-none focus:border-emerald-500/50 focus:ring-4 focus:ring-emerald-500/5 transition-all duration-300 shadow-xl"
        />
      </div>

      <!-- Loading State -->
      @if (isLoading()) {
        <div class="flex flex-col items-center justify-center py-24 gap-4">
          <div class="w-12 h-12 border-4 border-emerald-500/20 border-t-emerald-500 rounded-full animate-spin"></div>
          <p class="text-neutral-500 font-bold uppercase tracking-widest text-[10px] animate-pulse">Buscando academias...</p>
        </div>
      } @else {
        <!-- Grid -->
        @if (gyms().length > 0) {
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            @for (gym of gyms(); track gym.id) {
              <app-gym-card [gym]="gym" class="animate-in fade-in slide-in-from-bottom-4 duration-500" />
            }
          </div>
        } @else {
          <!-- Empty State -->
          <div class="text-center py-24 bg-neutral-900/50 border border-dashed border-neutral-800 rounded-3xl">
            <div class="w-20 h-20 bg-neutral-800 rounded-full flex items-center justify-center mx-auto mb-6 text-neutral-600">
               <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="18" height="18" x="3" y="3" rx="2"/><path d="M3 9h18"/><path d="M9 21V9"/></svg>
            </div>
            <h3 class="text-xl font-bold text-white mb-2">Nenhuma academia encontrada</h3>
            <p class="text-neutral-500">Tente ajustar a sua pesquisa ou procure por outra região.</p>
          </div>
        }
      }
    </div>
  `
})
export class GymListComponent implements OnInit {
  private gymService = inject(GymService);
  private toastService = inject(ToastService);

  gyms = signal<GymResponse[]>([]);
  isLoading = signal(true);
  searchQuery = '';

  constructor() {
    // Ensuring data is only fetched on the client side to avoid SSR Prerender timeouts
    afterNextRender(() => {
      this.fetchGyms();
    });
  }

  ngOnInit() {}

  fetchGyms() {
    this.isLoading.set(true);
    this.gymService.search(this.searchQuery).subscribe({
      next: (response) => {
        this.gyms.set(response.content);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
        this.toastService.error('Erro ao carregar as academias. Verifique se o servidor está rodando.');
      }
    });
  }

  onSearch() {
    this.fetchGyms();
  }
}
