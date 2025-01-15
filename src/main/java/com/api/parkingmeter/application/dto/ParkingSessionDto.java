package com.api.parkingmeter.application.dto;

import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.domain.PaymentMethod;
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
public class ParkingSessionDto {

  private Integer id;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  private boolean extendable;

  private PaymentMethod paymentMethod;

  private BigDecimal totalCost;

  private String authenticationCode;

  private ParkingSessionStatus status;
}
