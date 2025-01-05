package com.api.parkingmeter.application.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

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

  @NotNull(message = "Total cost is required")
  @Positive(message = "Total cost must be a positive value")
  private BigDecimal totalCost;

  public static ParkingSession create(
      final Vehicle vehicle,
      final LocalDateTime startTime,
      final LocalDateTime endTime,
      final BigDecimal totalCost) {
    return ParkingSession.builder()
        .vehicle(vehicle)
        .startTime(startTime)
        .endTime(endTime)
        .totalCost(totalCost)
        .build();
  }
}
