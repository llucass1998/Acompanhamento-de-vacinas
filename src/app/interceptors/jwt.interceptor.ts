import { inject, Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth/auth.service';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Add auth header with jwt if user is logged in and request is to the API url
    const apiRoot = environment.apiUrl.replace(/\/$/, '');
    const isApiUrl = request.url === apiRoot || request.url.startsWith(`${apiRoot}/`);
    const token = this.authService.getAccessToken();
    
    if (token && isApiUrl) {
      request = request.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        // Handle 401 Unauthorized errors globally
        if (error.status === 401 && isApiUrl) {
          // If 401, token might be expired. For now, simple logout.
          // TODO: Implement refresh token logic if needed
          this.authService.logout();
          this.router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }
}
