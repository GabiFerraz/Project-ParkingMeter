package com.api.parkingmeter.application.domain.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

  private final String errorCode;
  private final String message;

  public DomainException(final String message) {
    super(message);

    this.message = message;
    this.errorCode = "domain_exception";
  }
}
