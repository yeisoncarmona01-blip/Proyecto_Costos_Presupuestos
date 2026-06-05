import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent {
  email = '';
  password = '';
  rememberMe = false;
  isLoading = false;
  errorMessage = '';

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) { }

  onLogin(): void {
    if (!this.email || !this.password) {
      this.errorMessage = 'Debes completar correo y contraseña.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.login({
      email: this.email,
      password: this.password,
      rememberMe: this.rememberMe
    }).subscribe({
      next: (response) => {
        const storage = this.rememberMe ? localStorage : sessionStorage;
        storage.setItem('softcost-user', JSON.stringify(response));
        this.router.navigateByUrl('/');
      },
      error: (error) => {
        this.errorMessage = error?.error?.message ?? 'No se pudo iniciar sesión.';
        this.isLoading = false;
      }
    });
  }
}
