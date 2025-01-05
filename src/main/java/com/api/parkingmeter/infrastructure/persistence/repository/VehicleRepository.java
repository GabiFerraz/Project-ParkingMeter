package com.api.parkingmeter.infrastructure.persistence.repository;

import com.api.parkingmeter.infrastructure.persistence.entity.VehicleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Integer> {

  Optional<VehicleEntity> findByLicensePlate(final String licensePlate);
}
