import { Component, OnInit, inject, signal, afterNextRender } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GymService } from '../../core/services/gym.service';
import { GymResponse } from '../../core/models/gym.model';
import { GymCardComponent } from './gym-card';
import { debounceTime, distinctUntilChanged, Subject, map, Observable } from 'rxjs';
import { PaginatedResponse } from '../../core/models/pagination.model';

@Component({
  selector: 'app-gym-list',
  standalone: true,
  imports: [CommonModule, FormsModule, GymCardComponent],
  template: `
    <div class="max-w-7xl mx-auto px-6 py-12">
      <header class="mb-10 animate-in fade-in slide-in-from-left duration-700">
        <h1 class="text-4xl font-extrabold text-white uppercase tracking-tight mb-2">Academias Disponíveis</h1>
        <p class="text-neutral-500 font-medium">Encontre a sua próxima box ou academia perto de si.</p>
      </header>

      <!-- Search Bar -->
      <div class="relative mb-12 group">
        <div class="absolute inset-y-0 left-5 flex items-center pointer-events-none">
          <svg class="w-5 h-5 text-neutral-500 group-focus-within:text-emerald-500 transition-colors" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
            <circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/>
          </svg>
        </div>
        <input 
          type="text" 
          [(ngModel)]="searchQuery"
          (ngModelChange)="onSearchChange($event)"
          placeholder="Pesquisar por nome (ex: JS Academy)..." 
          class="w-full bg-neutral-900/50 border border-neutral-800 text-white pl-14 pr-6 py-5 rounded-2xl focus:outline-none focus:border-emerald-500/50 focus:ring-4 focus:ring-emerald-500/5 transition-all duration-300 placeholder:text-neutral-600 font-medium"
        >
      </div>

      @if (isLoading()) {
        <div class="flex flex-col items-center justify-center py-24 gap-6">
          <div class="w-12 h-12 border-4 border-emerald-500/10 border-t-emerald-500 rounded-full animate-spin"></div>
          <p class="text-emerald-500 font-bold uppercase tracking-[0.2em] text-[10px] animate-pulse">Buscando Academias</p>
        </div>
      } @else {
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 animate-in fade-in zoom-in-95 duration-500">
          @for (gym of gyms(); track gym.id) {
            <app-gym-card [gym]="gym"></app-gym-card>
          } @empty {
            <div class="col-span-full py-24 text-center bg-neutral-900/30 border border-neutral-800 border-dashed rounded-3xl">
              <div class="w-20 h-20 bg-neutral-900 rounded-full flex items-center justify-center mx-auto mb-6 text-neutral-700">
                <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="18" height="18" x="3" y="3" rx="2" ry="2"/><line x1="12" y1="8" x2="12" y2="16"/><line x1="8" y1="12" x2="16" y2="12"/></svg>
              </div>
              <p class="text-neutral-400 font-bold uppercase tracking-widest text-sm">Nenhuma academia encontrada</p>
              <p class="text-neutral-600 text-xs mt-2">Tente buscar por outro termo ou localidade.</p>
            </div>
          }
        </div>
      }
    </div>
  `
})
export class GymListComponent implements OnInit {
  private gymService = inject(GymService);
  
  gyms = signal<GymResponse[]>([]);
  isLoading = signal(true);
  searchQuery = '';
  
  private searchSubject = new Subject<string>();

  constructor() {
    afterNextRender(() => {
      this.fetchGyms();
    });
  }

  ngOnInit() {
    this.searchSubject.pipe(
      debounceTime(400),
      distinctUntilChanged()
    ).subscribe(query => {
      this.fetchGyms(query);
    });
  }

  onSearchChange(query: string) {
    this.searchSubject.next(query);
  }

  private fetchGyms(query?: string) {
    this.isLoading.set(true);
    
    // Normalize return types to Observable<GymResponse[]>
    const apiCall$: Observable<GymResponse[]> = query 
      ? this.gymService.search(query).pipe(map(res => res.content))
      : this.gymService.getNearby(0, 0); 
    
    apiCall$.subscribe({
      next: (gymsList: GymResponse[]) => {
        this.gyms.set(gymsList);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }
}
