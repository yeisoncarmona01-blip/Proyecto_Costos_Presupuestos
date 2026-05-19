package com.denkitronik.softcostservice.domain.exception;

public class UsuarioServiceException extends RuntimeException {

  public UsuarioServiceException(String message) {
    super(message);
  }

  public UsuarioServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}