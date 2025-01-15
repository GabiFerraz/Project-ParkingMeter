package com.api.parkingmeter.application.usecase.fixture;

import com.api.parkingmeter.application.domain.Vehicle;

public class UpdateVehicleTestFixture {

  public static Vehicle existingVehicle() {
    return Vehicle.builder().id(1).licensePlate("AAA0000").ownerName("John Doe").build();
  }

  public static Vehicle updatedVehicle() {
    return Vehicle.builder().id(1).licensePlate("AAA0000").ownerName("Jane Doe").build();
  }
}
