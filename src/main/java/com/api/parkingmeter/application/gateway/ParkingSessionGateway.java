package com.api.parkingmeter.application.gateway;

import com.api.parkingmeter.application.domain.ParkingSession;

public interface ParkingSessionGateway {

  ParkingSession save(final String licensePlate, final ParkingSession parkingSession);
}
