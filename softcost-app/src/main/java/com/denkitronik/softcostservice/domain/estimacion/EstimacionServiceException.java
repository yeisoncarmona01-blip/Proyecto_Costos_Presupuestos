package com.denkitronik.softcostservice.domain.estimacion;

public class EstimacionServiceException extends RuntimeException {

  public EstimacionServiceException(String message) {
    super(message);
  }

  public EstimacionServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}