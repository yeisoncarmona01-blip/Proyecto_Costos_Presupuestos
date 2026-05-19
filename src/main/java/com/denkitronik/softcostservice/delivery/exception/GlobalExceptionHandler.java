package com.denkitronik.softcostservice.delivery.exception;

import com.denkitronik.softcostservice.domain.exception.ProyectoNotFoundException;
import com.denkitronik.softcostservice.domain.exception.ProyectoServiceException;
import com.denkitronik.softcostservice.domain.exception.UsuarioAlreadyExistsException;
import com.denkitronik.softcostservice.domain.exception.UsuarioInvalidCredentialsException;
import com.denkitronik.softcostservice.domain.exception.UsuarioServiceException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ProyectoNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleNotFound(
    ProyectoNotFoundException ex) {

    ApiErrorResponse error = ApiErrorResponse.builder()
      .status(HttpStatus.NOT_FOUND.value())
      .error("NOT_FOUND")
      .message(ex.getMessage())
      .timestamp(LocalDateTime.now())
      .build();

    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ProyectoServiceException.class)
  public ResponseEntity<ApiErrorResponse> handleServiceException(
    ProyectoServiceException ex) {

    ApiErrorResponse error = ApiErrorResponse.builder()
      .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
      .error("INTERNAL_SERVER_ERROR")
      .message(ex.getMessage())
      .timestamp(LocalDateTime.now())
      .build();

    return new ResponseEntity<>(error,
      HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(UsuarioAlreadyExistsException.class)
  public ResponseEntity<ApiErrorResponse> handleUsuarioAlreadyExists(
    UsuarioAlreadyExistsException ex) {

    ApiErrorResponse error = ApiErrorResponse.builder()
      .status(HttpStatus.CONFLICT.value())
      .error("CONFLICT")
      .message(ex.getMessage())
      .timestamp(LocalDateTime.now())
      .build();

    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UsuarioInvalidCredentialsException.class)
  public ResponseEntity<ApiErrorResponse> handleUsuarioInvalidCredentials(
    UsuarioInvalidCredentialsException ex) {

    ApiErrorResponse error = ApiErrorResponse.builder()
      .status(HttpStatus.UNAUTHORIZED.value())
      .error("UNAUTHORIZED")
      .message(ex.getMessage())
      .timestamp(LocalDateTime.now())
      .build();

    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(UsuarioServiceException.class)
  public ResponseEntity<ApiErrorResponse> handleUsuarioServiceException(
    UsuarioServiceException ex) {

    ApiErrorResponse error = ApiErrorResponse.builder()
      .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
      .error("INTERNAL_SERVER_ERROR")
      .message(ex.getMessage())
      .timestamp(LocalDateTime.now())
      .build();

    return new ResponseEntity<>(error,
      HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
