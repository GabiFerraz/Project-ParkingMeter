package com.api.parkingmeter.application.usecase.exception;

import static java.lang.String.format;

public class VehicleAlreadyExistsException extends BusinessException {

  private static final String ERROR_CODE = "already_exists";
  private static final String MESSAGE = "Vehicle with license plate=[%s] already exists.";

  public VehicleAlreadyExistsException(final String licensePlate) {
    super(format(MESSAGE, licensePlate), ERROR_CODE);
  }
}
