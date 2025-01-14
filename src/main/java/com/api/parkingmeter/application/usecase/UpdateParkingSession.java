package com.api.parkingmeter.application.usecase;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.gateway.ParkingSessionGateway;
import com.api.parkingmeter.application.usecase.exception.ParkingSessionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class UpdateParkingSession {
  private final ParkingSessionGateway parkingSessionGateway;

  public ParkingSession execute(final String licensePlate) {
    ParkingSession parkingSession =
        parkingSessionGateway
            .findActiveSessionByLicensePlate(licensePlate)
            .orElseThrow(() -> new ParkingSessionNotFoundException(licensePlate));

    parkingSession.setEndTime(parkingSession.getEndTime().plusHours(1));


    BigDecimal additionalCost = BigDecimal.valueOf(10.00);
    BigDecimal updatedTotalCost = parkingSession.getTotalCost().add(additionalCost);

    parkingSession.setTotalCost(updatedTotalCost);

    return parkingSessionGateway.update(parkingSession);
  }
}
