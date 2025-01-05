package com.api.parkingmeter.infrastructure.controller.request;

import com.api.parkingmeter.application.domain.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CreateParkingSessionRequest {

  @NotBlank(message = "License plate is required")
  @Size(min = 7, max = 7, message = "License plate must be 7 characters")
  @Pattern(
      regexp = "[A-Z]{3}\\d{4}|[A-Z]{3}\\d[A-Z]\\d{2}",
      message = "License plate must be in the format AAA0000 or AAA0A00")
  private String licensePlate;

  @NotNull(message = "Start time is required")
  private LocalDateTime startTime;

  @NotNull(message = "End time is required")
  private LocalDateTime endTime;

  @NotNull(message = "Payment method is required")
  private PaymentMethod paymentMethod;
}
