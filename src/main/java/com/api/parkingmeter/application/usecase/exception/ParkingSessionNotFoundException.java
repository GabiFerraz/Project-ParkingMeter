package com.api.parkingmeter.application.usecase.exception;

import static java.lang.String.format;

public class ParkingSessionNotFoundException extends BusinessException {

  private static final String ERROR_CODE = "session_not_found";
  private static final String MESSAGE = "Active parking session for vehicle with license plate=[%s] not found.";

  public ParkingSessionNotFoundException(final String licensePlate) {
    super(format(MESSAGE, licensePlate), ERROR_CODE);
  }
}
