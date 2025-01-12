package com.api.parkingmeter.application.usecase.exception;

public class ParkingSessionNotFoundException extends RuntimeException {
  public ParkingSessionNotFoundException(String message) {
    super(message);
  }
}
