package com.api.parkingmeter.application.usecase.exception;

import static java.lang.String.format;

public class ParkingSessionCannotBeExtendedException extends BusinessException {
  private static final String ERROR_CODE = "bad_request";
  private static final String MESSAGE =
      "Parking session with license plate=[%s] cannot be extended.";

  public ParkingSessionCannotBeExtendedException(final String licensePlate) {
    super(format(MESSAGE, licensePlate), ERROR_CODE);
  }
}
