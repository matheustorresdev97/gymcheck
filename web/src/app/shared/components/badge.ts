import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span [class]="badgeClasses">
      <ng-content select="[icon]"></ng-content>
      <ng-content></ng-content>
    </span>
  `
})
export class AppBadgeComponent {
  @Input() variant: 'neutral' | 'emerald' | 'amber' | 'blue' = 'neutral';
  @Input() size: 'xs' | 'sm' | 'md' = 'sm';

  get badgeClasses(): string {
    const base = 'inline-flex items-center gap-1.5 font-bold uppercase tracking-wider rounded-full transition-all duration-300';
    
    const variants = {
      neutral: 'bg-neutral-800 text-neutral-400 border border-neutral-700/50',
      emerald: 'bg-emerald-500/10 text-emerald-500 border border-emerald-500/20',
      amber: 'bg-amber-500/10 text-amber-500 border border-amber-500/20',
      blue: 'bg-blue-500/10 text-blue-500 border border-blue-500/20'
    };

    const sizes = {
      xs: 'px-2 py-0.5 text-[8px]',
      sm: 'px-3 py-1.5 text-[10px]',
      md: 'px-4 py-2 text-xs'
    };

    return `${base} ${variants[this.variant]} ${sizes[this.size]}`;
  }
}
