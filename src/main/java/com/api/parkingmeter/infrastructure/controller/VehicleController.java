package com.api.parkingmeter.infrastructure.controller;

import com.api.parkingmeter.application.domain.Vehicle;
import com.api.parkingmeter.application.usecase.CreateVehicle;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parkingmeter/vehicle")
public class VehicleController {

  private final CreateVehicle createVehicle;

  @PostMapping
  public ResponseEntity<Vehicle> create(@Valid @RequestBody final Vehicle vehicle) {
    final var vehicleCreated =
        this.createVehicle.execute(vehicle.getLicensePlate(), vehicle.getOwnerName());

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(vehicleCreated.getId())
            .toUri();

    return ResponseEntity.created(location).body(vehicleCreated);
  }
}
