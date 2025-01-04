package com.api.parkingmeter.application.gateway;

import com.api.parkingmeter.application.domain.Vehicle;

public interface VehicleGateway {

  Vehicle save(final Vehicle vehicle);
}
