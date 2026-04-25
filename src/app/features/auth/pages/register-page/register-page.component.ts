import { Component } from '@angular/core';

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

  onRegister(): void {
    // TODO: integrar con Spring Boot backend
    console.log('Register:', {
      fullName: this.fullName,
      email: this.email,
      organization: this.organization,
      acceptTerms: this.acceptTerms
    });
  }
}
