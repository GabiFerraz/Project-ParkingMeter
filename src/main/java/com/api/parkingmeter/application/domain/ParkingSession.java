package com.api.parkingmeter.application.domain;

import com.api.parkingmeter.application.dto.VehicleDto;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

  private Integer id;

  @NotNull(message = "Vehicle is required")
  private VehicleDto vehicle;

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
      final VehicleDto vehicle,
      final LocalDateTime startTime,
      final LocalDateTime endTime,
      final boolean extendable,
      final PaymentMethod paymentMethod,
      final BigDecimal totalCost,
      final String authenticationCode,
      final ParkingSessionStatus status) {

    return ParkingSession.builder()
        .vehicle(vehicle)
        .startTime(startTime)
        .endTime(endTime)
        .extendable(extendable)
        .paymentMethod(paymentMethod)
        .totalCost(totalCost)
        .authenticationCode(authenticationCode)
        .status(status)
        .build();
  }
}
