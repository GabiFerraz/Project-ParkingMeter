package com.api.parkingmeter.infrastructure.config;

import com.api.parkingmeter.application.domain.exception.DomainException;
import com.api.parkingmeter.application.usecase.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ParkingMeterControllerExceptionHandler extends ResponseEntityExceptionHandler {

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
}
