package com.api.parkingmeter.infrastructure.persistence.repository;

import com.api.parkingmeter.infrastructure.persistence.entity.RateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<RateEntity, Long> {}
