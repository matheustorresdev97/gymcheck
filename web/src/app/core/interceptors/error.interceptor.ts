import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { ToastService } from '../services/toast.service';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const toastService = inject(ToastService);
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'Ocorreu um erro inesperado.';

      if (error.error instanceof ErrorEvent) {
        // Client-side error
        errorMessage = `Erro: ${error.error.message}`;
      } else {
        // Server-side error
        switch (error.status) {
          case 401:
            errorMessage = 'Sessão expirada. Por favor, faça login novamente.';
            authService.logout();
            break;
          case 403:
            errorMessage = 'Você não tem permissão para realizar esta ação.';
            break;
          case 404:
            errorMessage = 'Recurso não encontrado.';
            break;
          case 409:
            errorMessage = error.error?.message || 'Este registo já existe.';
            break;
          case 400:
            errorMessage = error.error?.message || 'Dados inválidos. Verifique os campos.';
            break;
          case 500:
            errorMessage = 'Erro interno do servidor. Tente novamente mais tarde.';
            break;
          default:
            errorMessage = error.error?.message || errorMessage;
        }
      }

      toastService.error(errorMessage);
      return throwError(() => error);
    })
  );
};
