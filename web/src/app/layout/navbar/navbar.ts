import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.html'
})
export class NavbarComponent {
  authService = inject(AuthService);
  private toastService = inject(ToastService);
  private router = inject(Router);

  logout() {
    this.authService.logout();
    this.toastService.info('Sessão encerrada.');
    this.router.navigate(['/login']);
  }
}
