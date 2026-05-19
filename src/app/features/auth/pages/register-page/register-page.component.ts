import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register-page',
  templateUrl: './register-page.component.html',
  styleUrls: ['./register-page.component.scss']
})
export class RegisterPageComponent {
  fullName = '';
  email = '';
  organization = '';
  password = '';
  confirmPassword = '';
  acceptTerms = false;
  isLoading = false;
  errorMessage = '';

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) { }

  onRegister(): void {
    if (!this.acceptTerms) {
      this.errorMessage = 'Debes aceptar los términos y condiciones.';
      return;
    }

    if (!this.fullName || !this.email || !this.password) {
      this.errorMessage = 'Completa los campos obligatorios.';
      return;
    }

    if (this.password.length < 6) {
      this.errorMessage = 'La contraseña debe tener al menos 6 caracteres.';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Las contraseñas no coinciden.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.register({
      fullName: this.fullName,
      email: this.email,
      organization: this.organization,
      password: this.password
    }).subscribe({
      next: () => {
        this.router.navigateByUrl('/login');
      },
      error: (error) => {
        this.errorMessage = error?.error?.message ?? 'No se pudo crear la cuenta.';
        this.isLoading = false;
      }
    });
  }
}
