package com.api.parkingmeter.infrastructure.gateway.fixture;

import com.api.parkingmeter.application.domain.Vehicle;
import com.api.parkingmeter.infrastructure.persistence.entity.VehicleEntity;
import java.util.ArrayList;

public class VehicleGatewayImplTestFixture {

  private static final String LICENSE_PLATE = "AAA0000";
  private static final String OWNER_NAME = "John Doe";

  public static VehicleEntity vehicleEntity() {
    return VehicleEntity.builder()
        .id(1)
        .licensePlate(LICENSE_PLATE)
        .ownerName(OWNER_NAME)
        .parkingSessions(new ArrayList<>())
        .build();
  }

  public static Vehicle vehicleDomain() {
    return Vehicle.builder().licensePlate(LICENSE_PLATE).ownerName(OWNER_NAME).build();
  }
}
