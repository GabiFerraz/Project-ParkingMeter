package com.api.parkingmeter.application.usecase;

import com.api.parkingmeter.application.domain.Vehicle;
import com.api.parkingmeter.application.gateway.VehicleGateway;
import com.api.parkingmeter.application.usecase.exception.VehicleAlreadyExistsException;
import com.api.parkingmeter.application.usecase.exception.VehicleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateVehicle {

  private final VehicleGateway vehicleGateway;

  public Vehicle execute(
      final String currentLicensePlate, final String newLicensePlate, final String ownerName) {
    final var vehicle =
        vehicleGateway
            .findByLicensePlate(currentLicensePlate)
            .orElseThrow(() -> new VehicleNotFoundException(currentLicensePlate));

    if (newLicensePlate != null
        && !newLicensePlate.isBlank()
        && !currentLicensePlate.equals(newLicensePlate)) {
      final var existingVehicleWithNewPlate = vehicleGateway.findByLicensePlate(newLicensePlate);

      if (existingVehicleWithNewPlate.isPresent()
          && !existingVehicleWithNewPlate.get().getId().equals(vehicle.getId())) {
        throw new VehicleAlreadyExistsException(newLicensePlate);
      }

      vehicle.setLicensePlate(newLicensePlate);
    }

    if (ownerName != null && !ownerName.isBlank() && !ownerName.equals(vehicle.getOwnerName())) {
      vehicle.setOwnerName(ownerName);
    }

    return vehicleGateway.update(vehicle);
  }
}
