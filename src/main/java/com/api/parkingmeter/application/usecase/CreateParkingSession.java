package com.api.parkingmeter.application.usecase;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.domain.PaymentMethod;
import com.api.parkingmeter.application.gateway.ParkingSessionGateway;
import com.api.parkingmeter.application.gateway.VehicleGateway;
import com.api.parkingmeter.application.usecase.exception.VehicleNotFoundException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateParkingSession {

  private final ParkingSessionGateway parkingSessionGateway;
  private final VehicleGateway vehicleGateway;

  public ParkingSession execute(
      final String licensePlate,
      final LocalDateTime startTime,
      final LocalDateTime endTime,
      final PaymentMethod paymentMethod) {

    final var vehicle =
        vehicleGateway
            .findByLicensePlate(licensePlate)
            .orElseThrow(() -> new VehicleNotFoundException(licensePlate));

    final var buildDomain =
        ParkingSession.create(
            vehicle,
            startTime,
            endTime,
            this.isExtendable(startTime, endTime),
            paymentMethod,
            this.calculateTotalCost(startTime, endTime),
            UUID.randomUUID().toString(),
            ParkingSessionStatus.ACTIVE);

    return parkingSessionGateway.save(licensePlate, buildDomain);
  }

  private boolean isExtendable(final LocalDateTime startTime, final LocalDateTime endTime) {
    final var durationInMinutes = Duration.between(startTime, endTime).toMinutes();

    return durationInMinutes <= 60;
  }

  private BigDecimal calculateTotalCost(
      final LocalDateTime startTime, final LocalDateTime endTime) {
    final var durationInMinutes = Math.min(Duration.between(startTime, endTime).toMinutes(), 120);

    if (durationInMinutes <= 60) {
      return BigDecimal.valueOf(10.00);
    }

    return BigDecimal.valueOf(15.00);
  }
}
