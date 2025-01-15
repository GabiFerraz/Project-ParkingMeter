package com.api.parkingmeter.infrastructure.controller.request;

import com.api.parkingmeter.application.domain.PaymentMethod;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
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

  @AssertTrue(message = "Start time must not be in the past")
  public boolean isStartTimeValid() {
    return startTime == null || !startTime.isBefore(LocalDateTime.now());
  }

  @AssertTrue(message = "End time must be at least 1 hour after start time")
  public boolean isEndTimeValid() {
    return startTime == null || endTime == null || endTime.isAfter(startTime.plusHours(1));
  }
}
