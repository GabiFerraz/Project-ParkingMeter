package com.api.parkingmeter.application.usecase;

import static com.api.parkingmeter.application.usecase.fixture.UpdateParkingSessionTestFixture.activeParkingSession;
import static com.api.parkingmeter.application.usecase.fixture.UpdateParkingSessionTestFixture.updatedParkingSession;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.gateway.ParkingSessionGateway;
import com.api.parkingmeter.application.usecase.exception.ParkingSessionNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

class UpdateParkingSessionTest {

    private final ParkingSessionGateway parkingSessionGateway = mock(ParkingSessionGateway.class);
    private final UpdateParkingSession updateParkingSession = new UpdateParkingSession(parkingSessionGateway);

    @Test
    void shouldExtendParkingSessionSuccessfully() {
        final var activeSession = activeParkingSession();
        final var updatedSession = updatedParkingSession();
        final ArgumentCaptor<ParkingSession> sessionCaptor = ArgumentCaptor.forClass(ParkingSession.class);

        when(parkingSessionGateway.findActiveSessionByLicensePlate(activeSession.getVehicle().getLicensePlate()))
                .thenReturn(of(activeSession));
        when(parkingSessionGateway.update(sessionCaptor.capture()))
                .thenReturn(updatedSession);

        final var response = updateParkingSession.execute(activeSession.getVehicle().getLicensePlate());

        assertThat(response.getEndTime()).isEqualTo(updatedSession.getEndTime());
        assertThat(response.getTotalCost()).isEqualTo(updatedSession.getTotalCost());

        verify(parkingSessionGateway).findActiveSessionByLicensePlate(activeSession.getVehicle().getLicensePlate());
        verify(parkingSessionGateway).update(sessionCaptor.getValue());
    }

    @Test
    void shouldThrowExceptionWhenSessionNotFound() {
        final var licensePlate = "XYZ1234";

        when(parkingSessionGateway.findActiveSessionByLicensePlate(licensePlate))
                .thenThrow(new ParkingSessionNotFoundException(licensePlate));

        try {
            updateParkingSession.execute(licensePlate);
        } catch (ParkingSessionNotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("No parking session were found for the car with license plate=[" + licensePlate + "].");
        }

        verify(parkingSessionGateway).findActiveSessionByLicensePlate(licensePlate);
        verify(parkingSessionGateway, never()).update(any());
    }
}
