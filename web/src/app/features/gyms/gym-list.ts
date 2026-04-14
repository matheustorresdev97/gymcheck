import { Component, OnInit, inject, signal, afterNextRender } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GymService } from '../../core/services/gym.service';
import { GymResponse } from '../../core/models/gym.model';
import { GymCardComponent } from './gym-card';
import { debounceTime, distinctUntilChanged, Subject, map, Observable } from 'rxjs';
import { AppInputComponent } from '../../shared/components/input';

@Component({
  selector: 'app-gym-list',
  standalone: true,
  imports: [CommonModule, FormsModule, GymCardComponent, AppInputComponent],
  template: `
    <div class="max-w-7xl mx-auto px-6 py-16">
      <header class="mb-12 animate-in fade-in slide-in-from-top-4 duration-700">
        <div class="flex items-center gap-4 mb-4">
          <div class="h-1 w-12 bg-emerald-500 rounded-full"></div>
          <p class="text-emerald-500 font-bold uppercase tracking-[0.3em] text-[10px]">Descubra seu potencial</p>
        </div>
        <h1 class="text-5xl font-extrabold text-white uppercase tracking-tighter mb-4">Academias Próximas</h1>
        <p class="text-neutral-500 font-medium max-w-2xl leading-relaxed">
          Encontre o local ideal para o seu treino. Explore as melhores boxes, academias e estúdios da região com o Gymcheck.
        </p>
      </header>

      <!-- Search Bar -->
      <div class="mb-16 animate-in fade-in slide-in-from-bottom-4 duration-700 delay-150">
        <app-input 
          [(ngModel)]="searchQuery"
          (ngModelChange)="onSearchChange($event)"
          placeholder="Pesquisar por nome da academia (ex: JS Academy)..."
          [icon]="true"
        >
          <svg icon class="w-5 h-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
            <circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/>
          </svg>
        </app-input>
      </div>

      @if (isLoading()) {
        <div class="flex flex-col items-center justify-center py-32 gap-8">
          <div class="relative">
            <div class="w-16 h-16 border-4 border-emerald-500/10 border-t-emerald-500 rounded-full animate-spin"></div>
            <div class="absolute inset-0 flex items-center justify-center">
              <div class="w-8 h-8 bg-emerald-500/20 rounded-full animate-pulse"></div>
            </div>
          </div>
          <p class="text-emerald-500 font-bold uppercase tracking-[0.25em] text-[10px] animate-pulse">Sincronizando com as Redes</p>
        </div>
      } @else {
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 animate-in fade-in zoom-in-95 duration-500">
          @for (gym of gyms(); track gym.id) {
            <app-gym-card [gym]="gym"></app-gym-card>
          } @empty {
            <div class="col-span-full py-32 text-center bg-neutral-900/20 border border-neutral-800/50 border-dashed rounded-[2.5rem] backdrop-blur-sm">
              <div class="w-24 h-24 bg-neutral-900 border border-neutral-800 rounded-full flex items-center justify-center mx-auto mb-8 text-neutral-700 shadow-inner">
                <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/><polyline points="14.5 2 14.5 7.5 20 7.5"/><line x1="12" y1="13" x2="12" y2="17"/><line x1="10" y1="15" x2="14" y2="15"/></svg>
              </div>
              <h2 class="text-xl font-bold text-white uppercase tracking-tight mb-2">Sem resultados</h2>
              <p class="text-neutral-500 font-medium max-w-xs mx-auto text-sm">
                Não conseguimos encontrar nenhuma academia com esse termo. Tente usar palavras-chave mais genéricas.
              </p>
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
    
    // Using search with empty string to return all gyms by default
    const apiCall$: Observable<GymResponse[]> = this.gymService.search(query || '').pipe(
      map(res => res.content)
    );
    
    apiCall$.subscribe({
      next: (gymsList: GymResponse[]) => {
        this.gyms.set(gymsList);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }
}
