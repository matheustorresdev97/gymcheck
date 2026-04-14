import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { GymService } from '../../../core/services/gym.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-gym-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  template: `
    <div class="max-w-2xl mx-auto px-6 py-12">
      <header class="mb-12">
        <div class="flex items-center gap-4 mb-4">
          <a routerLink="/gyms" class="w-10 h-10 bg-neutral-900 border border-neutral-800 rounded-xl flex items-center justify-center text-neutral-500 hover:text-white transition-all">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
          </a>
          <h1 class="text-3xl font-black text-white uppercase tracking-tight">Nova Academia</h1>
        </div>
        <p class="text-neutral-500 font-medium">Cadastre uma nova unidade na rede GymCheck.</p>
      </header>

      <form [formGroup]="gymForm" (ngSubmit)="onSubmit()" class="space-y-8 bg-neutral-900/50 border border-neutral-800 p-10 rounded-3xl shadow-2xl overflow-hidden relative">
        <div class="space-y-2">
          <label class="text-[10px] font-black text-neutral-500 uppercase tracking-[0.2em] ml-1">Nome da Academia</label>
          <input 
            type="text" 
            formControlName="title"
            placeholder="Ex: JS Academy Centro"
            class="w-full bg-neutral-950 border border-neutral-800 text-white px-5 py-4 rounded-xl focus:outline-none focus:border-emerald-500/50 transition-all placeholder:text-neutral-700"
          >
        </div>

        <div class="space-y-2">
          <label class="text-[10px] font-black text-neutral-500 uppercase tracking-[0.2em] ml-1">Descrição (Opcional)</label>
          <textarea 
            formControlName="description"
            placeholder="Conte um pouco sobre a academia..."
            rows="3"
            class="w-full bg-neutral-950 border border-neutral-800 text-white px-5 py-4 rounded-xl focus:outline-none focus:border-emerald-500/50 transition-all placeholder:text-neutral-700 resize-none"
          ></textarea>
        </div>

        <div class="grid grid-cols-2 gap-6">
          <div class="space-y-2">
            <label class="text-[10px] font-black text-neutral-500 uppercase tracking-[0.2em] ml-1">Telefone</label>
            <input 
              type="text" 
              formControlName="phone"
              placeholder="(11) 99999-9999"
              class="w-full bg-neutral-950 border border-neutral-800 text-white px-5 py-4 rounded-xl focus:outline-none focus:border-emerald-500/50 transition-all placeholder:text-neutral-700"
            >
          </div>
          <div class="flex items-end">
            <button 
              type="button"
              (click)="getCurrentLocation()"
              class="w-full h-[58px] bg-neutral-800 hover:bg-neutral-700 text-white rounded-xl text-[10px] font-black uppercase tracking-widest flex items-center justify-center gap-3 transition-all border border-neutral-700/50 shadow-lg active:scale-95"
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0Z"/><circle cx="12" cy="10" r="3"/></svg>
              Minha Posição
            </button>
          </div>
        </div>

        <div class="grid grid-cols-2 gap-6">
          <div class="space-y-2">
            <label class="text-[10px] font-black text-neutral-500 uppercase tracking-[0.2em] ml-1">Latitude</label>
            <input 
              type="number" 
              formControlName="latitude"
              step="any"
              class="w-full bg-neutral-950 border border-neutral-800 text-white px-5 py-4 rounded-xl focus:outline-none focus:border-emerald-500/50 transition-all"
            >
          </div>
          <div class="space-y-2">
            <label class="text-[10px] font-black text-neutral-500 uppercase tracking-[0.2em] ml-1">Longitude</label>
            <input 
              type="number" 
              formControlName="longitude"
              step="any"
              class="w-full bg-neutral-950 border border-neutral-800 text-white px-5 py-4 rounded-xl focus:outline-none focus:border-emerald-500/50 transition-all"
            >
          </div>
        </div>

        <button 
          type="submit"
          [disabled]="gymForm.invalid || isLoading()"
          class="w-full bg-emerald-500 hover:bg-emerald-400 text-black font-black uppercase tracking-widest py-5 rounded-2xl transition-all shadow-[0_0_30px_-5px_rgba(16,185,129,0.4)] disabled:opacity-50 disabled:shadow-none mt-4 flex items-center justify-center gap-3 active:scale-[0.98]"
        >
          @if (isLoading()) {
            <svg class="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
            Criando Academia...
          } @else {
            Cadastrar Unidade
          }
        </button>
      </form>
    </div>
  `
})
export class GymCreateComponent {
  private fb = inject(FormBuilder);
  private gymService = inject(GymService);
  private toastService = inject(ToastService);
  private router = inject(Router);

  isLoading = signal(false);

  gymForm: FormGroup = this.fb.group({
    title: ['', [Validators.required, Validators.minLength(3)]],
    description: [''],
    phone: [''],
    latitude: [null, [Validators.required]],
    longitude: [null, [Validators.required]]
  });

  getCurrentLocation() {
    navigator.geolocation.getCurrentPosition(
      (pos) => {
        this.gymForm.patchValue({
          latitude: pos.coords.latitude,
          longitude: pos.coords.longitude
        });
        this.toastService.success('Coordenadas capturadas!');
      },
      () => this.toastService.error('Erro ao obter localização.')
    );
  }

  onSubmit() {
    if (this.gymForm.invalid) return;

    this.isLoading.set(true);
    this.gymService.create(this.gymForm.value).subscribe({
      next: () => {
        this.toastService.success('Academia cadastrada com sucesso!');
        this.router.navigate(['/gyms']);
      },
      error: () => {
        this.isLoading.set(false);
        this.toastService.error('Erro ao cadastrar academia.');
      }
    });
  }
}
