import { Component } from '@angular/core';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent {
  email = '';
  password = '';
  rememberMe = false;

  onLogin(): void {
    // TODO: integrar con Spring Boot backend
    console.log('Login:', { email: this.email, rememberMe: this.rememberMe });
  }
}
