import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login';
import { RegisterComponent } from './features/auth/register';
import { GymListComponent } from './features/gyms/gym-list';
import { HistoryComponent } from './features/history/history';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'gyms', component: GymListComponent },
  { path: 'history', component: HistoryComponent },
  { path: 'admin/validations', component: GymListComponent }, // Placeholder
  { path: 'admin/gyms/create', component: GymListComponent }, // Placeholder
  { path: '', redirectTo: 'gyms', pathMatch: 'full' }
];
