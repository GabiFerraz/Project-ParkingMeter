package com.api.parkingmeter.application.domain;

import static java.util.Collections.emptyList;

import com.api.parkingmeter.application.dto.ParkingSessionDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
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
public class Vehicle {

  private Integer id;

  @NotBlank(message = "License plate is required")
  @Size(min = 7, max = 7, message = "License plate must be 7 characters")
  @Pattern(
      regexp = "[A-Z]{3}\\d{4}|[A-Z]{3}\\d[A-Z]\\d{2}",
      message = "License plate must be in the format AAA0000 or AAA0A00")
  private String licensePlate;

  private String ownerName;

  private List<ParkingSessionDto> parkingSessions;

  public static Vehicle create(final String licensePlate, final String ownerName) {
    return Vehicle.builder()
        .licensePlate(licensePlate)
        .ownerName(ownerName)
        .parkingSessions(emptyList())
        .build();
  }
}
