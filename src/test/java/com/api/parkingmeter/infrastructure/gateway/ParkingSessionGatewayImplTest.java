package com.api.parkingmeter.infrastructure.gateway;

import static com.api.parkingmeter.infrastructure.gateway.fixture.ParkingSessionGatewayImplTestFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.domain.PaymentMethod;
import com.api.parkingmeter.application.usecase.exception.ParkingSessionNotFoundException;
import com.api.parkingmeter.application.usecase.exception.VehicleNotFoundException;
import com.api.parkingmeter.infrastructure.persistence.entity.ParkingSessionEntity;
import com.api.parkingmeter.infrastructure.persistence.entity.VehicleEntity;
import com.api.parkingmeter.infrastructure.persistence.repository.ParkingSessionRepository;
import com.api.parkingmeter.infrastructure.persistence.repository.VehicleRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class ParkingSessionGatewayImplTest {

  private final ParkingSessionRepository parkingSessionRepository =
      mock(ParkingSessionRepository.class);
  private final VehicleRepository vehicleRepository = mock(VehicleRepository.class);

  private final ParkingSessionGatewayImpl parkingSessionGateway =
      new ParkingSessionGatewayImpl(parkingSessionRepository, vehicleRepository);

  @Test
  void shouldSaveSuccessfully() {
    final var licensePlate = "AAA0000";
    final var vehicle = validVehicleEntity();
    final ArgumentCaptor<ParkingSessionEntity> parkingSessionCaptor =
        ArgumentCaptor.forClass(ParkingSessionEntity.class);
    final var parkingSessionEntity = validParkingSessionEntity();
    final ArgumentCaptor<VehicleEntity> vehicleCaptor =
        ArgumentCaptor.forClass(VehicleEntity.class);
    final var vehicleWithParkingSession = validVehicleWithParkingSession();
    final var parkingSession = parkingSession();

    when(vehicleRepository.findByLicensePlate(licensePlate)).thenReturn(Optional.of(vehicle));
    when(parkingSessionRepository.save(parkingSessionCaptor.capture()))
        .thenReturn(parkingSessionEntity);
    when(vehicleRepository.save(vehicleCaptor.capture())).thenReturn(vehicleWithParkingSession);

    final var response = parkingSessionGateway.save(licensePlate, parkingSession);

    assertThat(response.getId()).isNotNull();
    assertThat(response.getVehicle().getId()).isEqualTo(1);
    assertThat(response.getVehicle().getLicensePlate()).isEqualTo(licensePlate);
    assertThat(response.getVehicle().getOwnerName()).isEqualTo(OWNER_NAME);
    assertThat(response.getStartTime()).isEqualTo(START_TIME);
    assertThat(response.getEndTime()).isEqualTo(END_TIME);
    assertThat(response.isExtendable()).isTrue();
    assertThat(response.getPaymentMethod()).isEqualTo(PaymentMethod.DEBIT_CARD);
    assertThat(response.getTotalCost()).isEqualTo("10.0");
    assertThat(response.getAuthenticationCode()).isEqualTo(AUTHENTICATION_CODE);
    assertThat(response.getStatus()).isEqualTo(ParkingSessionStatus.ACTIVE);

    verify(vehicleRepository).findByLicensePlate(licensePlate);
    verify(parkingSessionRepository).save(parkingSessionCaptor.getValue());
    verify(vehicleRepository).save(vehicleCaptor.getValue());
  }

  @Test
  void shouldNotSaveWhenVehicleNotFound() {
    final var licensePlate = "AAA0000";
    final var parkingSession = parkingSession();

    when(vehicleRepository.findByLicensePlate(licensePlate)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> parkingSessionGateway.save(licensePlate, parkingSession))
        .isInstanceOf(VehicleNotFoundException.class)
        .hasMessage("Vehicle with license plate=[AAA0000] not found.");

    verify(vehicleRepository).findByLicensePlate(licensePlate);
    verify(parkingSessionRepository, never()).save(any());
    verify(vehicleRepository, never()).save(any());
  }

  @Test
  void shouldFindSuccessfully() {
    final Pageable pageable = PageRequest.of(0, 10);
    final var entityPage = validPageOfParkingSessions();

    when(parkingSessionRepository.findByStatus(ParkingSessionStatus.ACTIVE, pageable))
        .thenReturn(entityPage);

    final var response = parkingSessionGateway.findByStatus(ParkingSessionStatus.ACTIVE, pageable);

    assertThat(response).isNotNull();
    assertThat(response.getTotalElements()).isEqualTo(1);
    assertThat(response.getContent().get(0).getId()).isEqualTo(1);
    assertThat(response.getContent().get(0).getVehicle().getId()).isEqualTo(1);
    assertThat(response.getContent().get(0).getVehicle().getLicensePlate())
        .isEqualTo(LICENSE_PLATE);
    assertThat(response.getContent().get(0).getVehicle().getOwnerName()).isEqualTo(OWNER_NAME);
    assertThat(response.getContent().get(0).getStartTime()).isEqualTo(START_TIME);
    assertThat(response.getContent().get(0).getEndTime()).isEqualTo(END_TIME);
    assertThat(response.getContent().get(0).isExtendable()).isTrue();
    assertThat(response.getContent().get(0).getPaymentMethod()).isEqualTo(PaymentMethod.DEBIT_CARD);
    assertThat(response.getContent().get(0).getTotalCost()).isEqualTo("10.0");
    assertThat(response.getContent().get(0).getAuthenticationCode()).isEqualTo(AUTHENTICATION_CODE);
    assertThat(response.getContent().get(0).getStatus()).isEqualTo(ParkingSessionStatus.ACTIVE);

    verify(parkingSessionRepository).findByStatus(ParkingSessionStatus.ACTIVE, pageable);
  }

  @Test
  void shouldUpdateStatusSuccessfully() {
    final var parkingSessionEntity = validParkingSessionEntity();
    final ArgumentCaptor<ParkingSessionEntity> parkingSessionCaptor =
        ArgumentCaptor.forClass(ParkingSessionEntity.class);
    final var newEntity = validParkingSessionEntityWithStatusFinished();

    when(parkingSessionRepository.findById(1)).thenReturn(Optional.of(parkingSessionEntity));
    when(parkingSessionRepository.save(parkingSessionCaptor.capture())).thenReturn(newEntity);

    parkingSessionGateway.updateStatus(1, ParkingSessionStatus.FINISHED);

    verify(parkingSessionRepository).findById(1);
    verify(parkingSessionRepository).save(parkingSessionCaptor.getValue());

    assertThat(parkingSessionCaptor.getValue().getId()).isEqualTo(newEntity.getId());
    assertThat(parkingSessionCaptor.getValue().getStatus()).isEqualTo(newEntity.getStatus());
  }

  @Test
  void shouldUpdateParkingSessionSuccessfully() {
    final var licensePlate = "AAA0000";
    final var existingSessionEntity = validParkingSessionEntity();
    final var updatedSessionEntity = validUpdatedParkingSessionEntity();
    final var updatedDomainSession = validUpdatedParkingSession();
    final ArgumentCaptor<ParkingSessionEntity> parkingSessionCaptor =
        ArgumentCaptor.forClass(ParkingSessionEntity.class);

    when(parkingSessionRepository.findParkingSessionByStatusAndVehicle_LicensePlate(
            ParkingSessionStatus.ACTIVE, licensePlate))
        .thenReturn(Optional.of(existingSessionEntity));
    when(parkingSessionRepository.save(parkingSessionCaptor.capture()))
        .thenReturn(updatedSessionEntity);

    final var response = parkingSessionGateway.update(updatedDomainSession);

    assertThat(response.getId()).isEqualTo(updatedDomainSession.getId());
    assertThat(response.getVehicle().getId()).isEqualTo(updatedDomainSession.getVehicle().getId());
    assertThat(response.getVehicle().getLicensePlate())
        .isEqualTo(updatedDomainSession.getVehicle().getLicensePlate());
    assertThat(response.getEndTime()).isEqualTo(updatedDomainSession.getEndTime());
    assertThat(response.getTotalCost()).isEqualTo(updatedDomainSession.getTotalCost());

    verify(parkingSessionRepository)
        .findParkingSessionByStatusAndVehicle_LicensePlate(
            ParkingSessionStatus.ACTIVE, licensePlate);

    final var parkingSessionCaptured = parkingSessionCaptor.getValue();
    verify(parkingSessionRepository).save(parkingSessionCaptured);

    assertThat(parkingSessionCaptured.getId()).isEqualTo(updatedSessionEntity.getId());
    assertThat(parkingSessionCaptured.getEndTime()).isEqualTo(updatedSessionEntity.getEndTime());
    assertThat(parkingSessionCaptured.getTotalCost())
        .isEqualTo(updatedSessionEntity.getTotalCost());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingNonExistentSession() {
    final var nonExistentSession = parkingSessionDomain();

    when(parkingSessionRepository.findParkingSessionByStatusAndVehicle_LicensePlate(
            ParkingSessionStatus.ACTIVE, nonExistentSession.getVehicle().getLicensePlate()))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> parkingSessionGateway.update(nonExistentSession))
        .isInstanceOf(ParkingSessionNotFoundException.class)
        .hasMessage(
            "No parking session were found for the car with license plate=["
                + nonExistentSession.getVehicle().getLicensePlate()
                + "].");

    verify(parkingSessionRepository)
        .findParkingSessionByStatusAndVehicle_LicensePlate(
            ParkingSessionStatus.ACTIVE, nonExistentSession.getVehicle().getLicensePlate());
    verify(parkingSessionRepository, never()).save(any());
  }

  @Test
  void shouldFindByAuthenticationCodeSuccessfully() {
    final var authenticationCode = "AUTH12345";
    final var entity = parkingSessionEntity();

    when(parkingSessionRepository.findByAuthenticationCode(authenticationCode))
        .thenReturn(Optional.of(entity));

    final var response = parkingSessionGateway.findByAuthenticationCode(authenticationCode);

    assertThat(response).isPresent();
    assertThat(response.get().getId()).isEqualTo(entity.getId());
    assertThat(response.get().getAuthenticationCode()).isEqualTo(entity.getAuthenticationCode());
    assertThat(response.get().getStatus()).isEqualTo(entity.getStatus());

    verify(parkingSessionRepository).findByAuthenticationCode(authenticationCode);
  }

  @Test
  void shouldReturnEmptyWhenAuthenticationCodeNotFound() {
    final var authenticationCode = "INVALID_CODE";

    when(parkingSessionRepository.findByAuthenticationCode(authenticationCode))
        .thenReturn(Optional.empty());

    final var response = parkingSessionGateway.findByAuthenticationCode(authenticationCode);

    assertThat(response).isEmpty();

    verify(parkingSessionRepository).findByAuthenticationCode(authenticationCode);
  }

  @Test
  void shouldFindActiveSessionByLicensePlateSuccessfully() {
    final var licensePlate = "AAA0000";
    final var entity = parkingSessionEntity();

    when(parkingSessionRepository.findParkingSessionByStatusAndVehicle_LicensePlate(
            ParkingSessionStatus.ACTIVE, licensePlate))
        .thenReturn(Optional.of(entity));

    final var response = parkingSessionGateway.findActiveSessionByLicensePlate(licensePlate);

    assertThat(response).isPresent();
    assertThat(response.get().getId()).isEqualTo(entity.getId());
    assertThat(response.get().getStatus()).isEqualTo(ParkingSessionStatus.ACTIVE);
    assertThat(response.get().getVehicle().getLicensePlate()).isEqualTo(licensePlate);

    verify(parkingSessionRepository)
        .findParkingSessionByStatusAndVehicle_LicensePlate(
            ParkingSessionStatus.ACTIVE, licensePlate);
  }

  @Test
  void shouldReturnEmptyWhenNoActiveSessionExistsForLicensePlate() {
    final var licensePlate = "AAA0000";

    when(parkingSessionRepository.findParkingSessionByStatusAndVehicle_LicensePlate(
            ParkingSessionStatus.ACTIVE, licensePlate))
        .thenReturn(Optional.empty());

    final var response = parkingSessionGateway.findActiveSessionByLicensePlate(licensePlate);

    assertThat(response).isEmpty();

    verify(parkingSessionRepository)
        .findParkingSessionByStatusAndVehicle_LicensePlate(
            ParkingSessionStatus.ACTIVE, licensePlate);
  }
}
