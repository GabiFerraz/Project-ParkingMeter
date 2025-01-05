package com.api.parkingmeter.application.usecase.exception;

import static java.lang.String.format;

public class VehicleNotFoundException extends BusinessException {

  private static final String ERROR_CODE = "not_found";
  private static final String MESSAGE = "Vehicle with id=[%s] not found.";

  public VehicleNotFoundException(final Integer vehicleId) {
    super(format(MESSAGE, vehicleId), ERROR_CODE);
  }
}
