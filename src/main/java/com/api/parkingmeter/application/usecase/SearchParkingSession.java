package com.api.parkingmeter.application.usecase;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.gateway.ParkingSessionGateway;
import com.api.parkingmeter.application.usecase.exception.ParkingSessionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchParkingSession {

  private static final String MESSAGE =
      "No parking session were found for the authentication code=[%s].";

  private final ParkingSessionGateway parkingSessionGateway;

  public ParkingSession execute(final String authenticationCode) {
    return parkingSessionGateway
        .findByAuthenticationCode(authenticationCode)
        .orElseThrow(() -> new ParkingSessionNotFoundException(MESSAGE, authenticationCode));
  }
}
