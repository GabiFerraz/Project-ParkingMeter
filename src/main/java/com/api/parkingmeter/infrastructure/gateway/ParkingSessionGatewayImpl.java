package com.api.parkingmeter.infrastructure.gateway;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.dto.VehicleDto;
import com.api.parkingmeter.application.gateway.ParkingSessionGateway;
import com.api.parkingmeter.application.usecase.exception.ParkingSessionNotFoundException;
import com.api.parkingmeter.application.usecase.exception.VehicleNotFoundException;
import com.api.parkingmeter.infrastructure.persistence.entity.ParkingSessionEntity;
import com.api.parkingmeter.infrastructure.persistence.entity.VehicleEntity;
import com.api.parkingmeter.infrastructure.persistence.repository.ParkingSessionRepository;
import com.api.parkingmeter.infrastructure.persistence.repository.VehicleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParkingSessionGatewayImpl implements ParkingSessionGateway {

  private static final String MESSAGE_ERROR =
      "No parking session were found for the car with license plate=[%s].";

  private final ParkingSessionRepository parkingSessionRepository;
  private final VehicleRepository vehicleRepository;

  @Override
  public ParkingSession save(final String licensePlate, final ParkingSession parkingSession) {
    final var vehicleEntity =
        vehicleRepository
            .findByLicensePlate(licensePlate)
            .orElseThrow(() -> new VehicleNotFoundException(licensePlate));

    final var parkingSessionEntity =
        ParkingSessionEntity.builder()
            .vehicle(vehicleEntity)
            .startTime(parkingSession.getStartTime())
            .endTime(parkingSession.getEndTime())
            .extendable(parkingSession.isExtendable())
            .paymentMethod(parkingSession.getPaymentMethod())
            .totalCost(parkingSession.getTotalCost())
            .authenticationCode(parkingSession.getAuthenticationCode())
            .status(parkingSession.getStatus())
            .build();

    vehicleEntity.addParkingSession(parkingSessionEntity);

    final var savedParkingSessionEntity = parkingSessionRepository.save(parkingSessionEntity);
    vehicleRepository.save(vehicleEntity);

    return ParkingSession.builder()
        .id(savedParkingSessionEntity.getId())
        .vehicle(toVehicleDto(savedParkingSessionEntity.getVehicle()))
        .startTime(savedParkingSessionEntity.getStartTime())
        .endTime(savedParkingSessionEntity.getEndTime())
        .extendable(savedParkingSessionEntity.isExtendable())
        .paymentMethod(savedParkingSessionEntity.getPaymentMethod())
        .totalCost(savedParkingSessionEntity.getTotalCost())
        .authenticationCode(savedParkingSessionEntity.getAuthenticationCode())
        .status(savedParkingSessionEntity.getStatus())
        .build();
  }

  @Override
  public Page<ParkingSession> findByStatus(
      final ParkingSessionStatus status, final Pageable pageable) {
    final var entityPage =
        parkingSessionRepository.findByStatus(ParkingSessionStatus.ACTIVE, pageable);

    return entityPage.map(this::toParkingSessionDomain);
  }

  @Override
  public void updateStatus(final Integer id, final ParkingSessionStatus status) {
    final var entity = parkingSessionRepository.findById(id).orElseThrow();

    final var newEntity =
        ParkingSessionEntity.builder()
            .id(entity.getId())
            .vehicle(entity.getVehicle())
            .startTime(entity.getStartTime())
            .endTime(entity.getEndTime())
            .extendable(entity.isExtendable())
            .paymentMethod(entity.getPaymentMethod())
            .totalCost(entity.getTotalCost())
            .authenticationCode(entity.getAuthenticationCode())
            .status(status)
            .build();

    parkingSessionRepository.save(newEntity);
  }

  @Override
  public Optional<ParkingSession> findActiveSessionByLicensePlate(final String licensePlate) {
    return parkingSessionRepository
        .findParkingSessionByStatusAndVehicle_LicensePlate(
            ParkingSessionStatus.ACTIVE, licensePlate)
        .map(this::toParkingSessionDomain);
  }

  @Override
  public ParkingSession update(final ParkingSession parkingSession) {
    final var entity =
        parkingSessionRepository
            .findParkingSessionByStatusAndVehicle_LicensePlate(
                parkingSession.getStatus(), parkingSession.getVehicle().getLicensePlate())
            .orElseThrow(
                () ->
                    new ParkingSessionNotFoundException(
                        MESSAGE_ERROR, parkingSession.getVehicle().getLicensePlate()));

    entity.setEndTime(parkingSession.getEndTime());
    entity.setTotalCost(parkingSession.getTotalCost());

    return toParkingSessionDomain(parkingSessionRepository.save(entity));
  }

  private VehicleDto toVehicleDto(final VehicleEntity vehicleEntity) {
    return VehicleDto.builder()
        .id(vehicleEntity.getId())
        .licensePlate(vehicleEntity.getLicensePlate())
        .ownerName(vehicleEntity.getOwnerName())
        .build();
  }

  private ParkingSession toParkingSessionDomain(final ParkingSessionEntity entity) {
    return ParkingSession.builder()
        .id(entity.getId())
        .vehicle(toVehicleDto(entity.getVehicle()))
        .startTime(entity.getStartTime())
        .endTime(entity.getEndTime())
        .extendable(entity.isExtendable())
        .paymentMethod(entity.getPaymentMethod())
        .totalCost(entity.getTotalCost())
        .authenticationCode(entity.getAuthenticationCode())
        .status(entity.getStatus())
        .build();
  }

  @Override
  public Optional<ParkingSession> findByAuthenticationCode(final String authenticationCode) {
    return parkingSessionRepository
        .findByAuthenticationCode(authenticationCode)
        .map(this::toParkingSessionDomain);
  }
}
