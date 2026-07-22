import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule, ToastController } from '@ionic/angular';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.page.html',
  styleUrls: ['./register.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, RouterModule]
})
export class RegisterPage {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly toastController = inject(ToastController);

  userData = {
    name: '',
    email: '',
    password: '',
    confirmPassword: ''
  };
  
  isLoading = false;
  errorMessage = '';

  async register() {
    if (!this.userData.name || !this.userData.email || !this.userData.password) {
      this.errorMessage = 'Preencha todos os campos obrigatórios';
      return;
    }

    if (this.userData.password !== this.userData.confirmPassword) {
      this.errorMessage = 'As senhas não coincidem';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const payload = {
      name: this.userData.name,
      email: this.userData.email,
      password: this.userData.password
    };

    this.authService.register(payload).subscribe({
      next: async () => {
        this.isLoading = false;
        const toast = await this.toastController.create({
          message: 'Cadastro realizado com sucesso! Faça seu login.',
          duration: 3000,
          color: 'success',
          position: 'top'
        });
        await toast.present();
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.isLoading = false;
        if (err.status === 409) {
          this.errorMessage = 'Este e-mail já está cadastrado.';
        } else if (err.status === 400) {
          this.errorMessage = 'Dados inválidos. Verifique os campos.';
        } else {
          this.errorMessage = 'Erro ao realizar cadastro. Tente novamente.';
        }
        console.error('Erro no cadastro', err);
      }
    });
  }
}
