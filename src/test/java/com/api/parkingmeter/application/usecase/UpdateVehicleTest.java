package com.api.parkingmeter.application.usecase;

import static com.api.parkingmeter.application.usecase.fixture.UpdateVehicleTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.api.parkingmeter.application.domain.Vehicle;
import com.api.parkingmeter.application.gateway.VehicleGateway;
import com.api.parkingmeter.application.usecase.exception.VehicleNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UpdateVehicleTest {

    private final VehicleGateway vehicleGateway = mock(VehicleGateway.class);
    private final UpdateVehicle updateVehicle = new UpdateVehicle(vehicleGateway);

    @Test
    void shouldUpdateVehicleSuccessfully() {
        final var existingVehicle = existingVehicle();
        final var updatedVehicle = updatedVehicle();

        when(vehicleGateway.findByLicensePlate(existingVehicle.getLicensePlate()))
                .thenReturn(Optional.of(existingVehicle));
        when(vehicleGateway.update(any(Vehicle.class)))
                .thenReturn(updatedVehicle);

        final var response = updateVehicle.execute(
                existingVehicle.getLicensePlate(),
                updatedVehicle.getLicensePlate(),
                updatedVehicle.getOwnerName()      // Corrigido: Fechando o parêntese
        );

        assertThat(response.getOwnerName()).isEqualTo("Jane Doe");

        verify(vehicleGateway).findByLicensePlate(existingVehicle.getLicensePlate());
        verify(vehicleGateway).update(any(Vehicle.class));  // Correção: de save para update
    }

    @Test
    void shouldThrowExceptionWhenVehicleDoesNotExist() {
        final var licensePlate = "ZZZ9999";

        when(vehicleGateway.findByLicensePlate(licensePlate)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateVehicle.execute(licensePlate, licensePlate, "New Owner"))
                .isInstanceOf(VehicleNotFoundException.class)
                .hasMessage("Vehicle with license plate=[" + licensePlate + "] not found.");

        verify(vehicleGateway).findByLicensePlate(licensePlate);
        verify(vehicleGateway, never()).update(any());  // Correção: de save para update
    }
}
