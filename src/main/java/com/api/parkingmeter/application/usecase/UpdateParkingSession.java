package com.api.parkingmeter.application.usecase;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.gateway.ParkingSessionGateway;
import com.api.parkingmeter.application.usecase.exception.ParkingSessionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateParkingSession {
  private final ParkingSessionGateway parkingSessionGateway;

  public ParkingSession execute(final String licensePlate) {
    final var parkingSession =
        parkingSessionGateway
            .findActiveSessionByLicensePlate(licensePlate)
            .orElseThrow(() -> new ParkingSessionNotFoundException(licensePlate));

    parkingSession.setEndTime(parkingSession.getEndTime().plusHours(1));

    return parkingSessionGateway.update(parkingSession);
  }
}
