import { Component, inject, signal, OnInit, afterNextRender } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { CheckInService } from '../../core/services/check-in.service';
import { CheckInResponse } from '../../core/models/check-in.model';
import { ToastService } from '../../core/services/toast.service';
import { catchError, of, tap } from 'rxjs';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule, DatePipe],
  template: `
    <div class="max-w-4xl mx-auto px-6 py-12">
      <header class="mb-10">
        <h1 class="text-4xl font-extrabold text-white uppercase tracking-tight mb-2">O Meu Histórico</h1>
        <p class="text-neutral-500 font-bold uppercase tracking-widest text-[10px]">Acompanhe a sua evolução e assiduidade.</p>
      </header>

      @if (isLoading()) {
        <div class="flex flex-col items-center justify-center py-24 gap-6 bg-neutral-900/30 border border-neutral-800/50 rounded-3xl">
          <div class="w-12 h-12 border-4 border-emerald-500/10 border-t-emerald-500 rounded-full animate-spin"></div>
          <div class="text-center">
            <p class="text-emerald-500 font-bold uppercase tracking-[0.2em] text-[10px] animate-pulse mb-1">Carregando</p>
            <p class="text-neutral-600 text-[10px] font-medium uppercase">Aguardando dados da API...</p>
          </div>
        </div>
      } @else {
        <div class="bg-neutral-900/50 border border-neutral-800 rounded-3xl overflow-hidden shadow-2xl animate-in fade-in duration-700">
          @if (checkIns().length > 0) {
            <div class="divide-y divide-neutral-800/50">
              @for (checkIn of checkIns(); track checkIn.id) {
                <div class="p-6 flex items-center justify-between hover:bg-neutral-800/20 transition-all duration-300 group">
                  <div class="flex items-center gap-6">
                    <div class="w-12 h-12 bg-neutral-800 rounded-xl flex items-center justify-center text-neutral-500 group-hover:text-emerald-500 group-hover:bg-emerald-500/10 group-hover:border-emerald-500/20 transition-all duration-500 border border-neutral-700/50">
                      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/><polyline points="14.5 2 14.5 7.5 20 7.5"/></svg>
                    </div>
                    <div>
                      <h3 class="text-lg font-bold text-white mb-1.5 tracking-tight uppercase group-hover:text-emerald-400 transition-colors">{{ checkIn.gymTitle }}</h3>
                      <div class="flex items-center gap-2.5 text-neutral-500 text-[10px] font-black uppercase tracking-[0.15em] leading-none">
                        <svg class="text-emerald-500/50" xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><rect width="18" height="18" x="3" y="4" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                        {{ checkIn.createdAt | date: 'dd MMM, yyyy - HH:mm' }}
                      </div>
                    </div>
                  </div>

                  <div class="flex items-center gap-4">
                    @if (checkIn.validatedAt) {
                      <span class="inline-flex items-center gap-2 bg-emerald-500/10 text-emerald-500 px-4 py-2 rounded-full text-[10px] font-black tracking-widest border border-emerald-500/20 uppercase shadow-[0_0_20px_-10px_rgba(16,185,129,0.5)]">
                        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
                        Validado
                      </span>
                    } @else {
                      <span class="inline-flex items-center gap-2 bg-orange-500/10 text-orange-400 px-4 py-2 rounded-full text-[10px] font-black tracking-widest border border-orange-500/20 uppercase shadow-[0_0_20px_-10px_rgba(249,115,22,0.4)]">
                        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
                        Pendente
                      </span>
                    }
                  </div>
                </div>
              }
            </div>
          } @else {
            <div class="p-24 text-center">
              <div class="w-24 h-24 bg-neutral-900 rounded-full flex items-center justify-center mx-auto mb-8 text-neutral-800 border border-neutral-800 shadow-inner">
                <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/></svg>
              </div>
              <h3 class="text-2xl font-black text-white mb-3 uppercase tracking-tight">Sem histórico</h3>
              <p class="text-neutral-500 text-sm font-medium tracking-wide">Os seus check-ins aparecerão aqui assim que começar a treinar!</p>
            </div>
          }
        </div>
      }
    </div>
  `
})
export class HistoryComponent implements OnInit {
  private checkInService = inject(CheckInService);
  private toastService = inject(ToastService);

  checkIns = signal<CheckInResponse[]>([]);
  isLoading = signal(true);

  constructor() {
    afterNextRender(() => {
      this.fetchHistory();
    });
  }

  ngOnInit() {}

  private fetchHistory() {
    this.isLoading.set(true);
    this.checkInService.getHistory()
      .pipe(
        tap(response => console.log('Check-in History Data:', response)),
        catchError(error => {
          console.error('Failed to load check-ins:', error);
          this.toastService.error('Erro de conexão com o servidor. Tente novamente.');
          return of({ content: [] });
        })
      )
      .subscribe({
        next: (response: any) => {
          // Safety check for Spring Data Page structure
          this.checkIns.set(response?.content || []);
          this.isLoading.set(false);
        }
      });
  }
}
