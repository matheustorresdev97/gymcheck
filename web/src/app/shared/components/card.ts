import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-card',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div 
      [class]="cardClasses"
    >
      <ng-content></ng-content>
    </div>
  `
})
export class AppCardComponent {
  @Input() hoverable = false;
  @Input() glass = false;
  @Input() padding: 'none' | 'sm' | 'md' | 'lg' = 'md';

  get cardClasses(): string {
    const base = 'bg-neutral-900 border border-neutral-800 rounded-3xl transition-all duration-300 group';
    
    const glassEffect = this.glass ? 'backdrop-blur-xl bg-neutral-900/60' : '';
    const hoverEffect = this.hoverable ? 'hover:border-emerald-500/50 hover:shadow-2xl hover:shadow-emerald-500/5 hover:-translate-y-1' : '';
    
    const paddings = {
      none: 'p-0',
      sm: 'p-4',
      md: 'p-6',
      lg: 'p-8'
    };

    return `${base} ${glassEffect} ${hoverEffect} ${paddings[this.padding]}`;
  }
}
