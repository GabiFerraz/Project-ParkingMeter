package com.api.parkingmeter.infrastructure.gateway;

import com.api.parkingmeter.application.domain.Vehicle;
import com.api.parkingmeter.application.gateway.VehicleGateway;
import com.api.parkingmeter.infrastructure.persistence.entity.VehicleEntity;
import com.api.parkingmeter.infrastructure.persistence.repository.VehicleRepository;
import java.util.ArrayList;
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

  private Vehicle toResponse(final VehicleEntity entity) {
    return new Vehicle(entity.getId(), entity.getLicensePlate(), entity.getOwnerName());
  }
}
