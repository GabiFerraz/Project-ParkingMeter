package com.api.parkingmeter.infrastructure.controller;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.usecase.CreateParkingSession;
import com.api.parkingmeter.application.usecase.UpdateParkingSession;
import com.api.parkingmeter.infrastructure.controller.request.CreateParkingSessionRequest;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parkingmeter/parking-sessions")
public class ParkingSessionController {

  private final CreateParkingSession createParkingSession;
  private final UpdateParkingSession updateParkingSession;

  @PostMapping
  public ResponseEntity<ParkingSession> create(
      @Valid @RequestBody final CreateParkingSessionRequest request) {
    final var parkingSessionCreated =
        this.createParkingSession.execute(
            request.getLicensePlate(),
            request.getStartTime(),
            request.getEndTime(),
            request.getPaymentMethod());

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(parkingSessionCreated.getId())
            .toUri();

    return ResponseEntity.created(location).body(parkingSessionCreated);
  }

  @PutMapping("/extend")
  public ResponseEntity<ParkingSession> extendParkingSession(
      @RequestParam final String licensePlate) {
    final var updatedSession = updateParkingSession.execute(licensePlate);
    return ResponseEntity.ok(updatedSession);
  }
}
