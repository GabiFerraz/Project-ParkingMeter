package com.api.parkingmeter.infrastructure.gateway;

import com.api.parkingmeter.application.domain.Vehicle;
import com.api.parkingmeter.application.dto.ParkingSessionDto;
import com.api.parkingmeter.application.gateway.VehicleGateway;
import com.api.parkingmeter.application.usecase.exception.VehicleNotFoundException;
import com.api.parkingmeter.infrastructure.persistence.entity.ParkingSessionEntity;
import com.api.parkingmeter.infrastructure.persistence.entity.VehicleEntity;
import com.api.parkingmeter.infrastructure.persistence.repository.VehicleRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VehicleGatewayImpl implements VehicleGateway {

  private final VehicleRepository vehicleRepository;

  @Override
  public Vehicle save(final Vehicle vehicle) {
    final var entity =
        VehicleEntity.builder()
            .licensePlate(vehicle.getLicensePlate())
            .ownerName(vehicle.getOwnerName())
            .parkingSessions(new ArrayList<>())
            .build();

    final var saved = vehicleRepository.save(entity);

    return this.toResponse(saved);
  }

  @Override
  public Optional<Vehicle> findByLicensePlate(final String licensePlate) {
    final var entity = vehicleRepository.findByLicensePlate(licensePlate);

    return entity.map(this::toResponseWithParkingSessions);
  }

  @Override
  public Vehicle update(final Vehicle vehicle) {
    final var existingEntity =
        vehicleRepository
            .findById(vehicle.getId())
            .orElseThrow(() -> new VehicleNotFoundException(vehicle.getLicensePlate()));

    existingEntity.setLicensePlate(vehicle.getLicensePlate());
    existingEntity.setOwnerName(vehicle.getOwnerName());

    final var updatedEntity = vehicleRepository.save(existingEntity);

    return this.toResponse(updatedEntity);
  }

  private Vehicle toResponse(final VehicleEntity entity) {
    return Vehicle.builder()
        .id(entity.getId())
        .licensePlate(entity.getLicensePlate())
        .ownerName(entity.getOwnerName())
        .parkingSessions(Collections.emptyList())
        .build();
  }

  private Vehicle toResponseWithParkingSessions(final VehicleEntity entity) {
    return Vehicle.builder()
        .id(entity.getId())
        .licensePlate(entity.getLicensePlate())
        .ownerName(entity.getOwnerName())
        .parkingSessions(this.toParkingSessionsDto(entity.getParkingSessions()))
        .build();
  }

  private List<ParkingSessionDto> toParkingSessionsDto(final List<ParkingSessionEntity> entities) {
    return entities.stream()
        .map(
            entity ->
                ParkingSessionDto.builder()
                    .id(entity.getId())
                    .startTime(entity.getStartTime())
                    .endTime(entity.getEndTime())
                    .extendable(entity.isExtendable())
                    .paymentMethod(entity.getPaymentMethod())
                    .totalCost(entity.getTotalCost())
                    .authenticationCode(entity.getAuthenticationCode())
                    .status(entity.getStatus())
                    .build())
        .toList();
  }
}
