package com.api.parkingmeter.application.usecase.fixture;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.domain.PaymentMethod;
import com.api.parkingmeter.application.domain.Vehicle;
import com.api.parkingmeter.application.dto.VehicleDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CreateParkingSessionTestFixture {

  public static final String LICENSE_PLATE = "AAA0000";
  public static final String OWNER_NAME = "John Doe";
  public static final LocalDateTime START_TIME = LocalDateTime.parse("2025-01-10T10:00:00");
  public static final LocalDateTime END_TIME = LocalDateTime.parse("2025-01-10T11:00:00");
  public static final String AUTHENTICATION_CODE = "123uuid456";

  public static Vehicle validVehicle() {
    return Vehicle.builder()
        .id(1)
        .licensePlate(LICENSE_PLATE)
        .ownerName(OWNER_NAME)
        .parkingSessions(List.of())
        .build();
  }

  public static ParkingSession validParkingSession() {
    return ParkingSession.builder()
        .id(1)
        .vehicle(validVehicleDto())
        .startTime(START_TIME)
        .endTime(END_TIME)
        .extendable(true)
        .paymentMethod(PaymentMethod.DEBIT_CARD)
        .totalCost(BigDecimal.valueOf(10))
        .authenticationCode(AUTHENTICATION_CODE)
        .status(ParkingSessionStatus.ACTIVE)
        .build();
  }

  public static VehicleDto validVehicleDto() {
    return VehicleDto.builder().id(1).licensePlate(LICENSE_PLATE).ownerName(OWNER_NAME).build();
  }
}
