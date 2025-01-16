package com.api.parkingmeter.infrastructure.controller.fixture;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.domain.PaymentMethod;
import com.api.parkingmeter.application.dto.VehicleDto;
import com.api.parkingmeter.infrastructure.controller.request.CreateParkingSessionRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParkingSessionControllerTestFixture {

  public static final String LICENSE_PLATE = "AAA0000";
  public static final LocalDateTime START_TIME = LocalDateTime.parse("2126-01-10T10:00:00");
  public static final LocalDateTime END_TIME = LocalDateTime.parse("2126-01-10T11:00:01");
  public static final String AUTHENTICATION_CODE = "123uuid456";
  public static final String OWNER_NAME = "John Doe";

  public static CreateParkingSessionRequest validRequest() {
    return CreateParkingSessionRequest.builder()
        .licensePlate(LICENSE_PLATE)
        .startTime(START_TIME)
        .endTime(END_TIME)
        .paymentMethod(PaymentMethod.DEBIT_CARD)
        .build();
  }

  public static ParkingSession validCreateResponse() {
    return ParkingSession.builder()
        .id(1)
        .vehicle(validVehicle())
        .startTime(START_TIME)
        .endTime(END_TIME)
        .extendable(true)
        .paymentMethod(PaymentMethod.DEBIT_CARD)
        .totalCost(BigDecimal.valueOf(10))
        .authenticationCode(AUTHENTICATION_CODE)
        .status(ParkingSessionStatus.ACTIVE)
        .build();
  }

  private static VehicleDto validVehicle() {
    return VehicleDto.builder().id(1).licensePlate(LICENSE_PLATE).ownerName(OWNER_NAME).build();
  }
}
