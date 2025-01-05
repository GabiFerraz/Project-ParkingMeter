package com.api.parkingmeter.application.gateway;

import com.api.parkingmeter.application.domain.ParkingSession;

public interface ParkingSessionGateway {

  ParkingSession save(final Integer vehicleId, final ParkingSession parkingSession);
}
