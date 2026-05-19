import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface AuthRegisterRequest {
  fullName: string;
  email: string;
  organization?: string;
  password: string;
}

export interface AuthLoginRequest {
  email: string;
  password: string;
  rememberMe?: boolean;
}

export interface AuthUserResponse {
  id: number;
  fullName: string;
  email: string;
  organization?: string | null;
  createdAt?: string | null;
  lastLoginAt?: string | null;
  loginCount: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = 'http://localhost:8080/api/v1/auth';

  constructor(private readonly http: HttpClient) { }

  register(payload: AuthRegisterRequest) {
    return this.http.post<AuthUserResponse>(`${this.apiUrl}/register`, payload);
  }

  login(payload: AuthLoginRequest) {
    return this.http.post<AuthUserResponse>(`${this.apiUrl}/login`, payload);
  }
}