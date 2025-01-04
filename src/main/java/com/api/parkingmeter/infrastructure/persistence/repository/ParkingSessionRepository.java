package com.api.parkingmeter.infrastructure.persistence.repository;

import com.api.parkingmeter.infrastructure.persistence.entity.ParkingSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSessionRepository extends JpaRepository<ParkingSessionEntity, Integer> {}
