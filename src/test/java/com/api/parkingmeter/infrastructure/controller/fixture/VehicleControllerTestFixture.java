package com.api.parkingmeter.infrastructure.controller.fixture;

import com.api.parkingmeter.application.domain.Vehicle;
import java.util.List;

public class VehicleControllerTestFixture {

  private static final String LICENSE_PLATE = "AAA0000";
  private static final String OWNER_NAME = "John Doe";

  public static Vehicle validRequest() {
    return Vehicle.builder().licensePlate(LICENSE_PLATE).ownerName(OWNER_NAME).build();
  }

  public static Vehicle validResponse() {
    return Vehicle.builder()
        .id(1)
        .licensePlate(LICENSE_PLATE)
        .ownerName(OWNER_NAME)
        .parkingSessions(List.of())
        .build();
  }
}
