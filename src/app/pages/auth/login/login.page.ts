import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, RouterModule]
})
export class LoginPage implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  credentials = {
    email: '',
    password: ''
  };
  
  isLoading = false;
  errorMessage = '';

  ngOnInit() {
    if (this.authService.isAuthenticated) {
      this.router.navigate(['/']);
    }
  }

  login() {
    if (!this.credentials.email || !this.credentials.password) {
      this.errorMessage = 'Preencha todos os campos';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.login(this.credentials).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.isLoading = false;
        if (err.status === 401 || err.status === 404) {
          this.errorMessage = 'E-mail ou senha incorretos.';
        } else {
          this.errorMessage = 'Erro ao realizar login. Tente novamente mais tarde.';
        }
        console.error('Erro no login', err);
      }
    });
  }
}
