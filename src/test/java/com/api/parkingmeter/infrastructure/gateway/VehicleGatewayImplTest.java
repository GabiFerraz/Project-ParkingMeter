package com.api.parkingmeter.infrastructure.gateway;

import static com.api.parkingmeter.infrastructure.gateway.fixture.VehicleGatewayImplTestFixture.vehicleDomain;
import static com.api.parkingmeter.infrastructure.gateway.fixture.VehicleGatewayImplTestFixture.vehicleEntity;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import com.api.parkingmeter.application.domain.Vehicle;
import com.api.parkingmeter.application.usecase.exception.VehicleNotFoundException;
import com.api.parkingmeter.infrastructure.persistence.entity.VehicleEntity;
import com.api.parkingmeter.infrastructure.persistence.repository.VehicleRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class VehicleGatewayImplTest {

  private final VehicleRepository vehicleRepository = mock(VehicleRepository.class);

  private final VehicleGatewayImpl vehicleGateway = new VehicleGatewayImpl(vehicleRepository);

  @Test
  void shouldSaveSuccessfully() {
    final var entity = vehicleEntity();
    final ArgumentCaptor<VehicleEntity> vehicleCaptor =
        ArgumentCaptor.forClass(VehicleEntity.class);
    final var domain = vehicleDomain();

    when(vehicleRepository.save(vehicleCaptor.capture())).thenReturn(entity);

    final var response = vehicleGateway.save(domain);

    final var captured = vehicleCaptor.getValue();
    assertThat(response.getId()).isEqualTo(1);
    assertThat(response.getLicensePlate()).isEqualTo(captured.getLicensePlate());
    assertThat(response.getOwnerName()).isEqualTo(captured.getOwnerName());
    assertThat(response.getParkingSessions()).isEqualTo(captured.getParkingSessions());

    verify(vehicleRepository).save(vehicleCaptor.getValue());
  }

  @Test
  void shouldFindByLicensePlateSuccessfully() {
    final var entity = vehicleEntity();
    final var licensePlate = "AAA0000";

    when(vehicleRepository.findByLicensePlate(licensePlate)).thenReturn(Optional.of(entity));

    final var response = vehicleGateway.findByLicensePlate(licensePlate);

    assertThat(response).isPresent();
    assertThat(response)
        .hasValueSatisfying(
            vehicle -> {
              assertThat(vehicle.getId()).isEqualTo(entity.getId());
              assertThat(vehicle.getLicensePlate()).isEqualTo(entity.getLicensePlate());
              assertThat(vehicle.getOwnerName()).isEqualTo(entity.getOwnerName());
              assertThat(vehicle.getParkingSessions()).isEqualTo(entity.getParkingSessions());
            });

    verify(vehicleRepository).findByLicensePlate(licensePlate);
  }

  @Test
  void shouldUpdateVehicleSuccessfully() {
    final var existingVehicleEntity =
        VehicleEntity.builder().id(1).licensePlate("AAA0000").ownerName("Gabi").build();

    final var updatedVehicleEntity =
        VehicleEntity.builder().id(1).licensePlate("BBB1111").ownerName("Bruna").build();

    final var updatedDomainVehicle =
        Vehicle.builder().id(1).licensePlate("BBB1111").ownerName("Bruna").build();

    when(vehicleRepository.findById(1)).thenReturn(Optional.of(existingVehicleEntity));
    when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(updatedVehicleEntity);

    final var response = vehicleGateway.update(updatedDomainVehicle);

    assertThat(response.getLicensePlate()).isEqualTo("BBB1111");
    assertThat(response.getOwnerName()).isEqualTo("Bruna");

    verify(vehicleRepository).findById(1);
    verify(vehicleRepository).save(existingVehicleEntity);
  }

  @Test
  void shouldThrowExceptionWhenUpdatingNonExistentVehicle() {
    final var nonExistentVehicle =
        Vehicle.builder().id(99).licensePlate("ZZZ9999").ownerName("Juninho").build();

    when(vehicleRepository.findById(99)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> vehicleGateway.update(nonExistentVehicle))
        .isInstanceOf(VehicleNotFoundException.class)
        .hasMessage("Vehicle with license plate=[ZZZ9999] not found.");

    verify(vehicleRepository).findById(99);
    verify(vehicleRepository, never()).save(any());
  }
}
