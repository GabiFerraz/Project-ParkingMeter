package com.api.parkingmeter.infrastructure.controller.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVehicleRequest {

  @Size(min = 7, max = 7, message = "License plate must be 7 characters")
  @Pattern(
      regexp = "[A-Z]{3}\\d{4}|[A-Z]{3}\\d[A-Z]\\d{2}",
      message = "License plate must be in the format AAA0000 or AAA0A00")
  private String licensePlate;

  private String ownerName;
}
