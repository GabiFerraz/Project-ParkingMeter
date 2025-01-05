package com.api.parkingmeter.infrastructure.gateway;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.domain.Vehicle;
import com.api.parkingmeter.application.gateway.ParkingSessionGateway;
import com.api.parkingmeter.application.usecase.exception.VehicleNotFoundException;
import com.api.parkingmeter.infrastructure.persistence.entity.ParkingSessionEntity;
import com.api.parkingmeter.infrastructure.persistence.entity.VehicleEntity;
import com.api.parkingmeter.infrastructure.persistence.repository.ParkingSessionRepository;
import com.api.parkingmeter.infrastructure.persistence.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParkingSessionGatewayImpl implements ParkingSessionGateway {

  private final ParkingSessionRepository parkingSessionRepository;
  private final VehicleRepository vehicleRepository;

  @Override
  public ParkingSession save(final String licensePlate, final ParkingSession parkingSession) {
    final var vehicleEntity =
        vehicleRepository
            .findByLicensePlate(licensePlate)
            .orElseThrow(() -> new VehicleNotFoundException(licensePlate));

    final var parkingSessionEntity =
        ParkingSessionEntity.builder()
            .vehicle(vehicleEntity)
            .startTime(parkingSession.getStartTime())
            .endTime(parkingSession.getEndTime())
            .extendable(parkingSession.isExtendable())
            .paymentMethod(parkingSession.getPaymentMethod())
            .totalCost(parkingSession.getTotalCost())
            .authenticationCode(parkingSession.getAuthenticationCode())
            .status(parkingSession.getStatus())
            .build();

    vehicleEntity.addParkingSession(parkingSessionEntity);

    final var savedParkingSessionEntity = parkingSessionRepository.save(parkingSessionEntity);

    return ParkingSession.builder()
        .id(savedParkingSessionEntity.getId())
        .vehicle(toVehicleDomain(savedParkingSessionEntity.getVehicle()))
        .startTime(savedParkingSessionEntity.getStartTime())
        .endTime(savedParkingSessionEntity.getEndTime())
        .extendable(savedParkingSessionEntity.isExtendable())
        .paymentMethod(savedParkingSessionEntity.getPaymentMethod())
        .totalCost(savedParkingSessionEntity.getTotalCost())
        .authenticationCode(savedParkingSessionEntity.getAuthenticationCode())
        .status(savedParkingSessionEntity.getStatus())
        .build();
  }

  private Vehicle toVehicleDomain(final VehicleEntity vehicleEntity) {
    return Vehicle.builder()
        .id(vehicleEntity.getId())
        .licensePlate(vehicleEntity.getLicensePlate())
        .ownerName(vehicleEntity.getOwnerName())
        .build();
  }
}
