import { Component, Input, forwardRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ControlValueAccessor, NG_VALUE_ACCESSOR, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-input',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="space-y-2">
      @if (label) {
        <label [for]="id" class="text-xs font-bold text-neutral-500 uppercase tracking-widest ml-1">
          {{ label }}
        </label>
      }
      
      <div class="relative group">
        @if (icon) {
          <div class="absolute inset-y-0 left-5 flex items-center pointer-events-none text-neutral-500 group-focus-within:text-emerald-500 transition-colors">
            <ng-content select="[icon]"></ng-content>
          </div>
        }
        
        <input
          [id]="id"
          [type]="type"
          [placeholder]="placeholder"
          [value]="value"
          [disabled]="disabled"
          (input)="onInput($event)"
          (blur)="onBlur()"
          [class]="inputClasses"
        />
      </div>

      @if (error) {
        <p class="text-[10px] font-bold text-red-500 uppercase tracking-wider ml-1 animate-in fade-in slide-in-from-top-1">
          {{ error }}
        </p>
      }
    </div>
  `,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => AppInputComponent),
      multi: true
    }
  ]
})
export class AppInputComponent implements ControlValueAccessor {
  @Input() id = `input-${Math.random().toString(36).substr(2, 9)}`;
  @Input() label = '';
  @Input() type = 'text';
  @Input() placeholder = '';
  @Input() error = '';
  @Input() icon = false;
  @Input() disabled = false;

  value = '';
  onChange: any = () => {};
  onTouched: any = () => {};

  get inputClasses(): string {
    return `w-full bg-neutral-950 border text-white placeholder-neutral-700 outline-none transition-all duration-300 rounded-xl p-4
      ${this.icon ? 'pl-14' : 'pl-4'}
      ${this.error 
        ? 'border-red-500/50 focus:border-red-500 focus:ring-4 focus:ring-red-500/5' 
        : 'border-neutral-800 focus:border-emerald-500/50 focus:ring-4 focus:ring-emerald-500/5'
      }`;
  }

  onInput(event: any) {
    this.value = event.target.value;
    this.onChange(this.value);
  }

  onBlur() {
    this.onTouched();
  }

  writeValue(value: any): void {
    this.value = value || '';
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }
}
