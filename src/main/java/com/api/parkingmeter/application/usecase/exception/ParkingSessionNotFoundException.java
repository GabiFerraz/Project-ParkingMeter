package com.api.parkingmeter.application.usecase.exception;

import static java.lang.String.format;

public class ParkingSessionNotFoundException extends BusinessException {

  private static final String ERROR_CODE = "not_found";
  private static final String MESSAGE = "Parking Session with id=[%s] not found.";

  public ParkingSessionNotFoundException(final Integer id) {
    super(format(MESSAGE, id), ERROR_CODE);
    
  }
}
