package com.api.parkingmeter.infrastructure.controller;

import com.api.parkingmeter.application.domain.Vehicle;
import com.api.parkingmeter.application.usecase.CreateVehicle;
import com.api.parkingmeter.application.usecase.SearchVehicle;
import com.api.parkingmeter.application.usecase.UpdateVehicle;
import com.api.parkingmeter.infrastructure.controller.request.UpdateVehicleRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parkingmeter/vehicle")
public class VehicleController {

  private final CreateVehicle createVehicle;
  private final SearchVehicle searchVehicle;
  private final UpdateVehicle updateVehicle;

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

  @GetMapping("/{licensePlate}")
  public ResponseEntity<Vehicle> findByLicensePlate(@PathVariable final String licensePlate) {

    return ResponseEntity.ok(searchVehicle.execute(licensePlate));
  }

  @PutMapping("/{licensePlate}")
  public ResponseEntity<Vehicle> updateVehicle(
      @PathVariable final String licensePlate,
      @RequestBody @Valid final UpdateVehicleRequest request) {

    final var updatedVehicle =
        this.updateVehicle.execute(licensePlate, request.getLicensePlate(), request.getOwnerName());

    return ResponseEntity.ok(updatedVehicle);
  }
}
