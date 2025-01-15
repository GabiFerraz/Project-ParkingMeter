package com.api.parkingmeter.application.usecase;

import static com.api.parkingmeter.application.usecase.fixture.CreateVehicleTestFixture.validVehicleGatewayResponse;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.api.parkingmeter.application.domain.Vehicle;
import com.api.parkingmeter.application.gateway.VehicleGateway;
import com.api.parkingmeter.application.usecase.exception.VehicleAlreadyExistsException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CreateVehicleTest {

  private final VehicleGateway vehicleGateway = mock(VehicleGateway.class);

  private final CreateVehicle createVehicle = new CreateVehicle(vehicleGateway);

  @Test
  void shouldCreateAVehicleSuccessfully() {
    final var licensePlate = "AAA0000";
    final var ownerName = "John Doe";
    final var vehicleGatewayResponse = validVehicleGatewayResponse();
    final ArgumentCaptor<Vehicle> vehicleCaptor = ArgumentCaptor.forClass(Vehicle.class);

    when(vehicleGateway.findByLicensePlate(licensePlate)).thenReturn(empty());
    when(vehicleGateway.save(vehicleCaptor.capture())).thenReturn(vehicleGatewayResponse);

    final var response = createVehicle.execute(licensePlate, ownerName);

    assertThat(response.getId()).isEqualTo(1);
    assertThat(response.getLicensePlate()).isEqualTo(licensePlate);
    assertThat(response.getOwnerName()).isEqualTo(ownerName);
    assertThat(response.getParkingSessions()).isEqualTo(List.of());

    verify(vehicleGateway).findByLicensePlate(licensePlate);
    verify(vehicleGateway).save(vehicleCaptor.getValue());
  }

  @Test
  void shouldNotCreateAVehicleWhenItAlreadyExists() {
    final var licensePlate = "AAA0000";
    final var ownerName = "John Doe";
    final var vehicleGatewayResponse = validVehicleGatewayResponse();

    when(vehicleGateway.findByLicensePlate(licensePlate))
        .thenReturn(Optional.of(vehicleGatewayResponse));

    assertThatThrownBy(() -> createVehicle.execute(licensePlate, ownerName))
        .isInstanceOf(VehicleAlreadyExistsException.class)
        .hasMessage("Vehicle with license plate=[AAA0000] already exists.");

    verify(vehicleGateway).findByLicensePlate(licensePlate);
    verify(vehicleGateway, never()).save(any());
  }
}
