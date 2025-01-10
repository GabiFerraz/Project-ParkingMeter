package com.api.parkingmeter.application.gateway;

import com.api.parkingmeter.application.domain.Vehicle;
import java.util.Optional;

public interface VehicleGateway {

  Vehicle save(final Vehicle vehicle);

  Optional<Vehicle> findByLicensePlate(final String licensePlate);

  Vehicle update(final Vehicle vehicle);
}
