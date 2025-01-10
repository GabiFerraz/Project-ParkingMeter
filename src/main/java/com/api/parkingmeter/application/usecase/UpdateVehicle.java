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

    public Vehicle execute(final String currentLicensePlate, final String newLicensePlate, final String ownerName) {
        final var vehicle = vehicleGateway.findByLicensePlate(currentLicensePlate)
                .orElseThrow(() -> new VehicleNotFoundException(currentLicensePlate));

        if (newLicensePlate != null && !currentLicensePlate.equals(newLicensePlate)) {
            if (vehicleGateway.findByLicensePlate(newLicensePlate).isPresent()) {
                throw new VehicleAlreadyExistsException(newLicensePlate);
            }
            vehicle.updateLicensePlate(newLicensePlate);
        }

        if (ownerName != null && !ownerName.equals(vehicle.getOwnerName())) {
            vehicle.updateOwner(ownerName);
        }

        return vehicleGateway.save(vehicle);
    }
}
