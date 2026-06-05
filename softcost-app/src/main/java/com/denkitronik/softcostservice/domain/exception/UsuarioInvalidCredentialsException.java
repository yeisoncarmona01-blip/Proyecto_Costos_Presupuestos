package com.denkitronik.softcostservice.domain.exception;

public class UsuarioInvalidCredentialsException extends RuntimeException {

  public UsuarioInvalidCredentialsException() {
    super("Correo o contraseña inválidos");
  }
}