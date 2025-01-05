package com.api.parkingmeter.application.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDto {

  private Integer id;

  private String licensePlate;

  private String ownerName;
}
