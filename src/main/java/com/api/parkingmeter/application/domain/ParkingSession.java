package com.api.parkingmeter.application.domain;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkingSession {

  private Long id;

  @NotNull(message = "Vehicle is required")
  private Vehicle vehicle;

  @NotNull(message = "Start time is required")
  private LocalDateTime startTime;

  @NotNull(message = "End time is required")
  private LocalDateTime endTime;

  private boolean extendable;

  @NotNull(message = "Payment method is required")
  private PaymentMethod paymentMethod;

  private BigDecimal totalCost;

  private String authenticationCode;

  private ParkingSessionStatus status;

  public static ParkingSession create(
      final Vehicle vehicle,
      final LocalDateTime startTime,
      final LocalDateTime endTime,
      final PaymentMethod paymentMethod) {

    final var parkingSession =
        ParkingSession.builder()
            .vehicle(vehicle)
            .startTime(startTime)
            .endTime(endTime)
            .paymentMethod(paymentMethod)
            .authenticationCode(UUID.randomUUID().toString())
            .status(ParkingSessionStatus.ACTIVE)
            .build();

    parkingSession.setExtendable(parkingSession.isExtendable(startTime, endTime));
    parkingSession.setTotalCost(parkingSession.calculateTotalCost(startTime, endTime));

    return parkingSession;
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
