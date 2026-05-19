package com.denkitronik.softcostservice.domain.exception;

public class ProyectoNotFoundException extends RuntimeException {

  public ProyectoNotFoundException(Long id) {
    super("Proyecto con ID " + id + " no encontrado");
  }
}
