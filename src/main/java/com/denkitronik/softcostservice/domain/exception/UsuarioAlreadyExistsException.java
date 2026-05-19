package com.denkitronik.softcostservice.domain.exception;

public class UsuarioAlreadyExistsException extends RuntimeException {

  public UsuarioAlreadyExistsException(String email) {
    super("Ya existe un usuario registrado con el correo: " + email);
  }
}