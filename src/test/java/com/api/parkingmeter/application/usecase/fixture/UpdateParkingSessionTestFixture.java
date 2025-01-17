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
  public static final LocalDateTime START_TIME = LocalDateTime.parse("2025-02-10T10:00:00");
  public static final LocalDateTime END_TIME = LocalDateTime.parse("2025-02-10T11:00:00");
  public static final LocalDateTime NEW_END_TIME = LocalDateTime.parse("2025-02-10T12:00:00");

  public static ParkingSession activeParkingSession() {
    return ParkingSession.builder()
        .id(1)
        .vehicle(
            VehicleDto.builder().id(1).licensePlate(LICENSE_PLATE).ownerName("John Doe").build())
        .startTime(START_TIME)
        .endTime(END_TIME)
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
        .startTime(START_TIME)
        .endTime(NEW_END_TIME)
        .extendable(false)
        .paymentMethod(PaymentMethod.PIX)
        .totalCost(BigDecimal.valueOf(15.00))
        .authenticationCode(AUTHENTICATION_CODE)
        .status(ParkingSessionStatus.ACTIVE)
        .build();
  }

  public static ParkingSession activeParkingSessionNotExtendable() {
    return ParkingSession.builder()
        .id(1)
        .vehicle(
            VehicleDto.builder().id(1).licensePlate(LICENSE_PLATE).ownerName("John Doe").build())
        .startTime(START_TIME)
        .endTime(NEW_END_TIME)
        .extendable(false)
        .paymentMethod(PaymentMethod.PIX)
        .totalCost(BigDecimal.valueOf(15.00))
        .authenticationCode(AUTHENTICATION_CODE)
        .status(ParkingSessionStatus.ACTIVE)
        .build();
  }
}
