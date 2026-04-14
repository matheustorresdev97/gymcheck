import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login';
import { RegisterComponent } from './features/auth/register';
import { GymListComponent } from './features/gyms/gym-list';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'gyms', component: GymListComponent },
  { path: '', redirectTo: 'gyms', pathMatch: 'full' }
];
