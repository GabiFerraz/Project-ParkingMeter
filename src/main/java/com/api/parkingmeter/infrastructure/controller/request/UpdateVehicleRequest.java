package com.api.parkingmeter.infrastructure.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVehicleRequest {
  private String licensePlate;
  private String ownerName;
}
