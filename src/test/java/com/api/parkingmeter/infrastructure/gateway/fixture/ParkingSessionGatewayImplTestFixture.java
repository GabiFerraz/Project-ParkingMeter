package com.api.parkingmeter.infrastructure.gateway.fixture;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.domain.PaymentMethod;
import com.api.parkingmeter.application.dto.VehicleDto;
import com.api.parkingmeter.infrastructure.persistence.entity.ParkingSessionEntity;
import com.api.parkingmeter.infrastructure.persistence.entity.VehicleEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class ParkingSessionGatewayImplTestFixture {
  public static final String LICENSE_PLATE = "AAA0000";
  public static final String OWNER_NAME = "Bruna";
  public static final LocalDateTime START_TIME = LocalDateTime.parse("2025-01-10T10:00:00");
  public static final LocalDateTime END_TIME = LocalDateTime.parse("2025-01-10T11:00:00");
  public static final String AUTHENTICATION_CODE = "123uuid456";

  public static VehicleEntity validVehicleEntity() {
    return VehicleEntity.builder()
        .id(1)
        .licensePlate(LICENSE_PLATE)
        .ownerName(OWNER_NAME)
        .parkingSessions(new ArrayList<>())
        .build();
  }

  public static ParkingSessionEntity validParkingSessionEntity() {
    return ParkingSessionEntity.builder()
        .id(1)
        .vehicle(validVehicleEntity())
        .startTime(START_TIME)
        .endTime(END_TIME)
        .extendable(true)
        .paymentMethod(PaymentMethod.DEBIT_CARD)
        .totalCost(BigDecimal.valueOf(10.00))
        .authenticationCode(AUTHENTICATION_CODE)
        .status(ParkingSessionStatus.ACTIVE)
        .build();
  }

  public static VehicleEntity validVehicleWithParkingSession() {
    return VehicleEntity.builder()
        .id(1)
        .licensePlate(LICENSE_PLATE)
        .ownerName(OWNER_NAME)
        .parkingSessions(List.of(validParkingSessionEntity()))
        .build();
  }

  public static ParkingSession parkingSession() {
    return ParkingSession.builder()
        .startTime(START_TIME)
        .endTime(END_TIME)
        .extendable(true)
        .paymentMethod(PaymentMethod.DEBIT_CARD)
        .totalCost(BigDecimal.valueOf(10.0))
        .authenticationCode(AUTHENTICATION_CODE)
        .status(ParkingSessionStatus.ACTIVE)
        .build();
  }

  public static Page<ParkingSessionEntity> validPageOfParkingSessions() {
    return new PageImpl<>(List.of(validParkingSessionEntity()));
  }

  public static ParkingSessionEntity validParkingSessionEntityWithStatusFinished() {
    return ParkingSessionEntity.builder()
        .id(1)
        .vehicle(validVehicleEntity())
        .startTime(START_TIME)
        .endTime(END_TIME)
        .extendable(true)
        .paymentMethod(PaymentMethod.DEBIT_CARD)
        .totalCost(BigDecimal.valueOf(10.00))
        .authenticationCode(AUTHENTICATION_CODE)
        .status(ParkingSessionStatus.FINISHED)
        .build();
  }

  public static ParkingSessionEntity parkingSessionEntity() {
    return ParkingSessionEntity.builder()
        .id(1)
        .startTime(LocalDateTime.now())
        .endTime(LocalDateTime.now().plusHours(2))
        .status(ParkingSessionStatus.ACTIVE)
        .totalCost(BigDecimal.valueOf(15.00))
        .vehicle(vehicleEntity())
        .build();
  }

  public static ParkingSession parkingSessionDomain() {
    return ParkingSession.builder()
        .id(1)
        .startTime(LocalDateTime.now())
        .endTime(LocalDateTime.now().plusHours(2))
        .status(ParkingSessionStatus.ACTIVE)
        .totalCost(BigDecimal.valueOf(15.00))
        .vehicle(vehicleDto())
        .build();
  }

  public static VehicleEntity vehicleEntity() {
    return VehicleEntity.builder().id(1).licensePlate("AAA0000").ownerName(OWNER_NAME).build();
  }

  public static VehicleDto vehicleDto() {
    return VehicleDto.builder().id(1).licensePlate("AAA0000").ownerName(OWNER_NAME).build();
  }

  public static ParkingSessionEntity validUpdatedParkingSessionEntity() {
    return ParkingSessionEntity.builder()
        .id(1)
        .vehicle(validVehicleEntity())
        .startTime(START_TIME)
        .endTime(LocalDateTime.parse("2025-01-10T12:00:00"))
        .extendable(true)
        .paymentMethod(PaymentMethod.DEBIT_CARD)
        .totalCost(BigDecimal.valueOf(15.00))
        .authenticationCode(AUTHENTICATION_CODE)
        .status(ParkingSessionStatus.ACTIVE)
        .build();
  }

  public static ParkingSession validUpdatedParkingSession() {
    return ParkingSession.builder()
        .id(1)
        .vehicle(vehicleDto())
        .startTime(START_TIME)
        .endTime(LocalDateTime.parse("2025-01-10T12:00:00"))
        .extendable(true)
        .paymentMethod(PaymentMethod.DEBIT_CARD)
        .totalCost(BigDecimal.valueOf(15.00))
        .authenticationCode(AUTHENTICATION_CODE)
        .status(ParkingSessionStatus.ACTIVE)
        .build();
  }
}
