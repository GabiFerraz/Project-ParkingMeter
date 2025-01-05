package com.api.parkingmeter.application.gateway;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParkingSessionGateway {

  ParkingSession save(final String licensePlate, final ParkingSession parkingSession);

  Page<ParkingSession> findByStatus(final ParkingSessionStatus status, Pageable pageable);

  void updateStatus(final Integer id, final ParkingSessionStatus status);
}
