package com.api.parkingmeter.application.usecase;

import com.api.parkingmeter.application.domain.Vehicle;
import com.api.parkingmeter.application.gateway.VehicleGateway;
import com.api.parkingmeter.application.usecase.exception.VehicleAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateVehicle {

  private final VehicleGateway vehicleGateway;

  public Vehicle execute(final String licensePlate, final String ownerName) {
    final var vehicle = vehicleGateway.findByLicensePlate(licensePlate);

    if (vehicle.isPresent()) {
      throw new VehicleAlreadyExistsException(licensePlate);
    }

    final var buildDomain = Vehicle.create(licensePlate, ownerName);

    return vehicleGateway.save(buildDomain);
  }
}
