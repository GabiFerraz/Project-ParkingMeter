package com.api.parkingmeter.application.usecase;

import static com.api.parkingmeter.application.usecase.fixture.SearchVehicleTestFixture.LICENSE_PLATE;
import static com.api.parkingmeter.application.usecase.fixture.SearchVehicleTestFixture.OWNER_NAME;
import static com.api.parkingmeter.application.usecase.fixture.SearchVehicleTestFixture.validVehicleGatewayResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.api.parkingmeter.application.gateway.VehicleGateway;
import com.api.parkingmeter.application.usecase.exception.VehicleNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class SearchVehicleTest {

  private final VehicleGateway vehicleGateway = mock(VehicleGateway.class);

  private final SearchVehicle searchVehicle = new SearchVehicle(vehicleGateway);

  @Test
  void shouldSearchAVehicleSuccessfully() {
    final var licensePlate = "AAA0000";
    final var vehicleGatewayResponse = validVehicleGatewayResponse();

    when(vehicleGateway.findByLicensePlate(licensePlate))
        .thenReturn(Optional.of(vehicleGatewayResponse));

    final var response = searchVehicle.execute(licensePlate);

    assertThat(response.getId()).isEqualTo(1);
    assertThat(response.getLicensePlate()).isEqualTo(LICENSE_PLATE);
    assertThat(response.getOwnerName()).isEqualTo(OWNER_NAME);
    assertThat(response.getParkingSessions()).isEqualTo(List.of());

    verify(vehicleGateway).findByLicensePlate(licensePlate);
  }

  @Test
  void shouldThrowAnExceptionWhenVehicleNotFound() {
    final var licensePlate = "AAA0000";

    when(vehicleGateway.findByLicensePlate(licensePlate)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> searchVehicle.execute(licensePlate))
        .isInstanceOf(VehicleNotFoundException.class)
        .hasMessage("Vehicle with license plate=[AAA0000] not found.");

    verify(vehicleGateway).findByLicensePlate(licensePlate);
  }
}
