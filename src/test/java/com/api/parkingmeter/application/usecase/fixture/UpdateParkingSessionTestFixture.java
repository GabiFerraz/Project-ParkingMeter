package com.api.parkingmeter.application.usecase.fixture;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.domain.PaymentMethod;
import com.api.parkingmeter.application.dto.VehicleDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UpdateParkingSessionTestFixture {

  private static final String LICENSE_PLATE = "XYZ1234";
  private static final String AUTHENTICATION_CODE = "auth-123";

  public static ParkingSession activeParkingSession() {
    return ParkingSession.builder()
        .id(1)
        .vehicle(
            VehicleDto.builder().id(1).licensePlate(LICENSE_PLATE).ownerName("John Doe").build())
        .startTime(LocalDateTime.now().minusHours(1))
        .endTime(LocalDateTime.now())
        .extendable(true)
        .paymentMethod(PaymentMethod.PIX)
        .totalCost(BigDecimal.valueOf(10.00))
        .authenticationCode(AUTHENTICATION_CODE)
        .status(ParkingSessionStatus.ACTIVE)
        .build();
  }

  public static ParkingSession updatedParkingSession() {
    return ParkingSession.builder()
        .id(1)
        .vehicle(
            VehicleDto.builder().id(1).licensePlate(LICENSE_PLATE).ownerName("John Doe").build())
        .startTime(LocalDateTime.now().minusHours(1))
        .endTime(LocalDateTime.now().plusHours(1))
        .extendable(true)
        .paymentMethod(PaymentMethod.PIX)
        .totalCost(BigDecimal.valueOf(20.00)) // Valor atualizado
        .authenticationCode(AUTHENTICATION_CODE)
        .status(ParkingSessionStatus.ACTIVE)
        .build();
  }
}
