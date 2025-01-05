package com.api.parkingmeter.infrastructure.controller;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.usecase.CreateParkingSession;
import com.api.parkingmeter.infrastructure.controller.request.CreateParkingSessionRequest;
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
@RequestMapping("/parkingmeter/parking-sessions")
public class ParkingSessionController {

  private final CreateParkingSession createParkingSession;

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
}
