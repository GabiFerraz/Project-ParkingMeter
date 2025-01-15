package com.api.parkingmeter.infrastructure.persistence.repository;

import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.infrastructure.persistence.entity.ParkingSessionEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSessionRepository extends JpaRepository<ParkingSessionEntity, Integer> {

  @EntityGraph(attributePaths = {"vehicle"})
  Page<ParkingSessionEntity> findByStatus(
      final ParkingSessionStatus status, final Pageable pageable);

  @EntityGraph(attributePaths = {"vehicle"})
  Optional<ParkingSessionEntity> findParkingSessionByStatusAndVehicle_LicensePlate(
      final ParkingSessionStatus status, final String licensePlate);

  @EntityGraph(attributePaths = {"vehicle"})
  Optional<ParkingSessionEntity> findByAuthenticationCode(final String authenticationCode);
}
