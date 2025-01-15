package com.api.parkingmeter.infrastructure.gateway;

import static com.api.parkingmeter.infrastructure.gateway.fixture.ParkingSessionGatewayImplTestFixture.AUTHENTICATION_CODE;
import static com.api.parkingmeter.infrastructure.gateway.fixture.ParkingSessionGatewayImplTestFixture.END_TIME;
import static com.api.parkingmeter.infrastructure.gateway.fixture.ParkingSessionGatewayImplTestFixture.LICENSE_PLATE;
import static com.api.parkingmeter.infrastructure.gateway.fixture.ParkingSessionGatewayImplTestFixture.OWNER_NAME;
import static com.api.parkingmeter.infrastructure.gateway.fixture.ParkingSessionGatewayImplTestFixture.START_TIME;
import static com.api.parkingmeter.infrastructure.gateway.fixture.ParkingSessionGatewayImplTestFixture.parkingSession;
import static com.api.parkingmeter.infrastructure.gateway.fixture.ParkingSessionGatewayImplTestFixture.validPageOfParkingSessions;
import static com.api.parkingmeter.infrastructure.gateway.fixture.ParkingSessionGatewayImplTestFixture.validParkingSessionEntity;
import static com.api.parkingmeter.infrastructure.gateway.fixture.ParkingSessionGatewayImplTestFixture.validParkingSessionEntityWithStatusFinished;
import static com.api.parkingmeter.infrastructure.gateway.fixture.ParkingSessionGatewayImplTestFixture.validVehicleEntity;
import static com.api.parkingmeter.infrastructure.gateway.fixture.ParkingSessionGatewayImplTestFixture.validVehicleWithParkingSession;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.domain.PaymentMethod;
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
}
