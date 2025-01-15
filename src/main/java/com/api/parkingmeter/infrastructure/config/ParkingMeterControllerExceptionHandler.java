package com.api.parkingmeter.infrastructure.config;

import com.api.parkingmeter.application.domain.exception.DomainException;
import com.api.parkingmeter.application.usecase.exception.BusinessException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ParkingMeterControllerExceptionHandler {

  @ExceptionHandler({DomainException.class})
  public ResponseEntity<Object> handleDomainException(final DomainException ex) {
    log.error(ex.getMessage());
    final var errorResponse = new ErrorResponse(ex.getMessage(), ex.getCause().toString());

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler({BusinessException.class})
  public ResponseEntity<Object> handleBusinessException(final BusinessException ex) {
    log.error(ex.getMessage());
    final var errorResponse = new ErrorResponse(ex.getMessage(), ex.getErrorCode());

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<Object> handleValidationExceptions(
      final MethodArgumentNotValidException ex) {
    log.error(ex.getMessage());
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    ex.getBindingResult()
        .getGlobalErrors()
        .forEach(error -> errors.put(error.getObjectName(), error.getDefaultMessage()));

    return ResponseEntity.badRequest().body(errors);
  }
}
