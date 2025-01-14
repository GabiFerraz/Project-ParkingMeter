package com.api.parkingmeter.application.usecase.exception;

import static java.lang.String.format;

public class ParkingSessionNotFoundException extends BusinessException {

  private static final String ERROR_CODE = "not_found";
  private static final String MESSAGE =
      "No parking session were found for the car with license plate=[%s].";

  public ParkingSessionNotFoundException(final String licensePlate) {
    super(format(MESSAGE, licensePlate), ERROR_CODE);
  }
}
