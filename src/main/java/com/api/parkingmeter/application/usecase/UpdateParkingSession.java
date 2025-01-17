package com.api.parkingmeter.application.usecase;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.gateway.ParkingSessionGateway;
import com.api.parkingmeter.application.usecase.exception.ParkingSessionCannotBeExtendedException;
import com.api.parkingmeter.application.usecase.exception.ParkingSessionNotFoundException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateParkingSession {
  private static final String MESSAGE =
      "No parking session were found for the car with license plate=[%s].";

  private final ParkingSessionGateway parkingSessionGateway;

  public ParkingSession execute(final String licensePlate) {
    final ParkingSession parkingSession =
        parkingSessionGateway
            .findActiveSessionByLicensePlate(licensePlate)
            .orElseThrow(() -> new ParkingSessionNotFoundException(MESSAGE, licensePlate));

    if (!parkingSession.isExtendable()) {
      throw new ParkingSessionCannotBeExtendedException(
          parkingSession.getVehicle().getLicensePlate());
    }

    parkingSession.setEndTime(parkingSession.getEndTime().plusHours(1));
    parkingSession.setExtendable(false);
    parkingSession.setTotalCost(parkingSession.getTotalCost().add(BigDecimal.valueOf(5)));

    return parkingSessionGateway.update(parkingSession);
  }
}
