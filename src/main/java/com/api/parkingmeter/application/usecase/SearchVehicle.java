package com.api.parkingmeter.application.usecase;

import com.api.parkingmeter.application.domain.Vehicle;
import com.api.parkingmeter.application.gateway.VehicleGateway;
import com.api.parkingmeter.application.usecase.exception.VehicleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchVehicle {

  private final VehicleGateway vehicleGateway;

  public Vehicle execute(final String licensePlate) {
    return vehicleGateway
        .findByLicensePlate(licensePlate)
        .orElseThrow(() -> new VehicleNotFoundException(licensePlate));
  }
}
