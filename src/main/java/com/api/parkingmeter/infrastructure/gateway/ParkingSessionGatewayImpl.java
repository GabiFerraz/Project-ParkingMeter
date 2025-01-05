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
  public ParkingSession save(final Integer vehicleId, final ParkingSession parkingSession) {
    final var vehicleEntity =
        vehicleRepository
            .findById(vehicleId)
            .orElseThrow(() -> new VehicleNotFoundException(vehicleId));

    final var entity =
        ParkingSessionEntity.builder()
            .vehicle(vehicleEntity)
            .startTime(parkingSession.getStartTime())
            .endTime(parkingSession.getEndTime())
            .totalCost(parkingSession.getTotalCost())
            .build();

    vehicleEntity.addParkingSession(entity);

    final var savedEntity = parkingSessionRepository.save(entity);

    return ParkingSession.builder()
        .id(savedEntity.getId())
        .vehicle(toDomain(savedEntity.getVehicle()))
        .startTime(savedEntity.getStartTime())
        .endTime(savedEntity.getEndTime())
        .totalCost(savedEntity.getTotalCost())
        .build();
  }

  private Vehicle toDomain(final VehicleEntity vehicleEntity) {
    return new Vehicle(
        vehicleEntity.getId(), vehicleEntity.getLicensePlate(), vehicleEntity.getOwnerName());
  }
}
