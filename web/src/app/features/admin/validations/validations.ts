import { Component, inject, signal, OnInit, afterNextRender } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CheckInService } from '../../../core/services/check-in.service';
import { CheckInResponse } from '../../../core/models/check-in.model';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-validations',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="max-w-6xl mx-auto px-6 py-12">
      <header class="mb-10 flex justify-between items-end">
        <div>
          <h1 class="text-4xl font-extrabold text-white uppercase tracking-tight mb-2">Validação de Check-ins</h1>
          <p class="text-neutral-500 font-medium">Gerencie e valide os acessos dos membros em tempo real.</p>
        </div>
        <div class="bg-emerald-500/10 border border-emerald-500/20 px-4 py-2 rounded-xl flex items-center gap-3">
          <div class="w-2 h-2 bg-emerald-500 rounded-full animate-pulse"></div>
          <span class="text-emerald-500 text-xs font-black uppercase tracking-widest">Live Monitoring</span>
        </div>
      </header>

      @if (isLoading()) {
        <div class="flex flex-col items-center justify-center py-24 gap-6 bg-neutral-900/30 border border-neutral-800/50 rounded-3xl">
          <div class="w-12 h-12 border-4 border-emerald-500/10 border-t-emerald-500 rounded-full animate-spin"></div>
          <p class="text-neutral-600 font-bold uppercase tracking-widest text-[10px]">Buscando check-ins pendentes...</p>
        </div>
      } @else {
        <div class="grid gap-4">
          @for (checkIn of checkIns(); track checkIn.id) {
            <div 
              class="bg-neutral-900/50 border border-neutral-800 p-6 rounded-2xl flex items-center justify-between hover:border-neutral-700 transition-all duration-300 group"
              [class.opacity-60]="checkIn.validatedAt"
            >
              <div class="flex items-center gap-6">
                <div class="w-14 h-14 bg-neutral-800 rounded-2xl flex items-center justify-center text-neutral-400 group-hover:text-emerald-500 transition-colors border border-neutral-700/50 relative">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                  @if (!checkIn.validatedAt) {
                    <span class="absolute -top-1 -right-1 w-3 h-3 bg-orange-500 rounded-full border-2 border-neutral-900"></span>
                  }
                </div>
                <div>
                  <div class="flex items-center gap-3 mb-1">
                    <h3 class="text-lg font-bold text-white tracking-tight leading-none">Usuário ID: {{ checkIn.userId.substring(0, 8) }}...</h3>
                    @if (checkIn.validatedAt) {
                      <span class="text-[9px] font-black uppercase bg-emerald-500/10 text-emerald-500 px-2 py-0.5 rounded border border-emerald-500/20">Validado</span>
                    } @else {
                      <span class="text-[9px] font-black uppercase bg-orange-500/10 text-orange-400 px-2 py-0.5 rounded border border-orange-500/20">Aguardando</span>
                    }
                  </div>
                  <p class="text-emerald-500 text-xs font-black uppercase tracking-widest mb-2">{{ checkIn.gymTitle }}</p>
                  <p class="text-neutral-500 text-[10px] font-bold uppercase tracking-tighter">{{ checkIn.createdAt | date: 'dd/MM/yyyy HH:mm:ss' }}</p>
                </div>
              </div>

              <div class="flex items-center gap-3">
                @if (!checkIn.validatedAt) {
                  <button 
                    (click)="validate(checkIn.id)"
                    class="bg-emerald-500 hover:bg-emerald-400 text-black px-6 py-2.5 rounded-xl text-[10px] font-black uppercase tracking-widest transition-all duration-300 shadow-[0_0_20px_-5px_rgba(16,185,129,0.5)] active:scale-95"
                  >
                    Confirmar Acesso
                  </button>
                } @else {
                  <div class="text-neutral-600 text-[10px] font-black uppercase tracking-widest flex items-center gap-2 pr-4">
                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
                    Acesso Liberado
                  </div>
                }
              </div>
            </div>
          } @empty {
            <div class="py-32 text-center bg-neutral-900/30 border border-neutral-800 border-dashed rounded-3xl">
              <div class="w-20 h-20 bg-neutral-900 rounded-full flex items-center justify-center mx-auto mb-6 text-neutral-800">
                <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
              </div>
              <h3 class="text-xl font-bold text-white mb-2 uppercase tracking-tight">Tudo em dia!</h3>
              <p class="text-neutral-500 text-sm font-medium">Nenhum check-in pendente de validação no momento.</p>
            </div>
          }
        </div>
      }
    </div>
  `
})
export class ValidationsComponent implements OnInit {
  private checkInService = inject(CheckInService);
  private toastService = inject(ToastService);

  checkIns = signal<CheckInResponse[]>([]);
  isLoading = signal(true);

  constructor() {
    afterNextRender(() => {
      this.fetchCheckIns();
    });
  }

  ngOnInit() {}

  private fetchCheckIns() {
    this.isLoading.set(true);
    // As an admin, this call will return all user check-ins
    this.checkInService.getHistory().subscribe({
      next: (response) => {
        // Sort: Pending first
        const sorted = response.content.sort((a, b) => {
          if (!a.validatedAt && b.validatedAt) return -1;
          if (a.validatedAt && !b.validatedAt) return 1;
          return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
        });
        this.checkIns.set(sorted);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
        this.toastService.error('Erro ao carregar validações.');
      }
    });
  }

  validate(checkInId: string) {
    this.checkInService.validate(checkInId).subscribe({
      next: () => {
        this.toastService.success('Check-in validado com sucesso!');
        this.fetchCheckIns(); // Refresh list
      },
      error: (err) => {
        const message = err.error?.message || 'Erro ao validar check-in. O tempo pode ter expirado (20 min).';
        this.toastService.error(message);
      }
    });
  }
}
