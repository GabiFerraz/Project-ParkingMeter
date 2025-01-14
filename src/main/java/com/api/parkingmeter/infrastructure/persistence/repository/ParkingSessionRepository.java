package com.api.parkingmeter.infrastructure.persistence.repository;

import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.infrastructure.persistence.entity.ParkingSessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingSessionRepository extends JpaRepository<ParkingSessionEntity, Integer> {

  @EntityGraph(attributePaths = {"vehicle"})
  Page<ParkingSessionEntity> findByStatus(
      final ParkingSessionStatus status, final Pageable pageable);

  @EntityGraph(attributePaths = {"vehicle"})
  Optional<ParkingSessionEntity> findByVehicle_LicensePlateAndStatus(String licensePlate, ParkingSessionStatus status);

}
