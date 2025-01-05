package com.api.parkingmeter.application.usecase.exception;

import static java.lang.String.format;

public class VehicleNotFoundException extends BusinessException {

  private static final String ERROR_CODE = "not_found";
  private static final String MESSAGE = "Vehicle with license plate=[%s] not found.";

  public VehicleNotFoundException(final String licensePlate) {
    super(format(MESSAGE, licensePlate), ERROR_CODE);
  }
}
