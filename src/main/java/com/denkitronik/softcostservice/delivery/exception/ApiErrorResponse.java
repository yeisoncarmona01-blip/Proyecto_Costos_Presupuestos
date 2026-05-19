package com.denkitronik.softcostservice.delivery.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiErrorResponse {

  private Integer status;

  private String error;

  private String message;

  private LocalDateTime timestamp;
}
