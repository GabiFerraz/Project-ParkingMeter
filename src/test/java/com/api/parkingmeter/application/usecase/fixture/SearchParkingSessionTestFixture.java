package com.api.parkingmeter.application.usecase.fixture;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.domain.PaymentMethod;
import com.api.parkingmeter.application.dto.VehicleDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SearchParkingSessionTestFixture {

  public static ParkingSession validParkingSession() {
    return ParkingSession.builder()
        .id(1)
        .vehicle(validVehicle())
        .startTime(LocalDateTime.now().minusHours(1))
        .endTime(LocalDateTime.now().plusHours(1))
        .extendable(true)
        .paymentMethod(PaymentMethod.PIX)
        .totalCost(BigDecimal.valueOf(20.00))
        .authenticationCode("AUTH12345")
        .status(ParkingSessionStatus.ACTIVE)
        .build();
  }

  private static VehicleDto validVehicle() {
    return VehicleDto.builder().id(1).licensePlate("XYZ1234").ownerName("John Doe").build();
  }
}
