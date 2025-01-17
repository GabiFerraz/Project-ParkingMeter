package com.api.parkingmeter.application.usecase.exception;

import static java.lang.String.format;

public class ParkingSessionNotFoundException extends BusinessException {

  private static final String ERROR_CODE = "not_found";

  public ParkingSessionNotFoundException(final String message, final String licensePlate) {
    super(format(message, licensePlate), ERROR_CODE);
  }
}
