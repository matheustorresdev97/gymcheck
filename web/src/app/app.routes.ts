import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login';
import { RegisterComponent } from './features/auth/register';
import { GymListComponent } from './features/gyms/gym-list';
import { HistoryComponent } from './features/history/history';
import { ValidationsComponent } from './features/admin/validations/validations';
import { GymCreateComponent } from './features/admin/gym-create/gym-create';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'gyms', component: GymListComponent },
  { path: 'history', component: HistoryComponent },
  { path: 'admin/validations', component: ValidationsComponent },
  { path: 'admin/gyms/create', component: GymCreateComponent },
  { path: '', redirectTo: 'gyms', pathMatch: 'full' }
];
