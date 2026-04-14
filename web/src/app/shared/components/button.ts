import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-button',
  standalone: true,
  imports: [CommonModule],
  template: `
    <button
      [type]="type"
      [disabled]="disabled || isLoading"
      (click)="onClick.emit($event)"
      [class]="buttonClasses"
    >
      @if (isLoading) {
        <svg class="animate-spin h-5 w-5 mr-3" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" fill="none"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
        <span>Processando...</span>
      } @else {
        <ng-content></ng-content>
      }
    </button>
  `
})
export class AppButtonComponent {
  @Input() type: 'button' | 'submit' = 'button';
  @Input() variant: 'primary' | 'secondary' | 'outline' | 'ghost' = 'primary';
  @Input() size: 'sm' | 'md' | 'lg' | 'xl' = 'md';
  @Input() disabled = false;
  @Input() isLoading = false;
  @Input() fullWidth = false;

  @Output() onClick = new EventEmitter<MouseEvent>();

  get buttonClasses(): string {
    const base = 'inline-flex items-center justify-center font-bold uppercase tracking-tight transition-all duration-300 rounded-xl disabled:opacity-50 disabled:cursor-not-allowed active:scale-[0.98]';
    
    const variants = {
      primary: 'bg-emerald-500 hover:bg-emerald-400 text-black shadow-[0_0_20px_-5px_rgba(16,185,129,0.4)] hover:shadow-[0_0_25px_-5px_rgba(16,185,129,0.6)]',
      secondary: 'bg-neutral-800 hover:bg-neutral-700 text-white border border-neutral-700 hover:border-neutral-600',
      outline: 'bg-transparent border-2 border-emerald-500/30 hover:border-emerald-500 text-emerald-500 hover:bg-emerald-500/5',
      ghost: 'bg-transparent hover:bg-neutral-800 text-neutral-400 hover:text-white'
    };

    const sizes = {
      sm: 'px-4 py-2 text-[10px]',
      md: 'px-6 py-3.5 text-xs',
      lg: 'px-8 py-4 text-sm',
      xl: 'px-10 py-5 text-base'
    };

    return `${base} ${variants[this.variant]} ${sizes[this.size]} ${this.fullWidth ? 'w-full' : ''}`;
  }
}
