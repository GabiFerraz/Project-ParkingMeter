package com.api.parkingmeter.application.usecase;

import static com.api.parkingmeter.application.usecase.fixture.SearchParkingSessionTestFixture.validParkingSession;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.gateway.ParkingSessionGateway;
import com.api.parkingmeter.application.usecase.exception.ParkingSessionNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

class SearchParkingSessionTest {

    private final ParkingSessionGateway parkingSessionGateway = mock(ParkingSessionGateway.class);
    private final SearchParkingSession searchParkingSession = new SearchParkingSession(parkingSessionGateway);

    @Test
    void shouldFindParkingSessionByAuthenticationCodeSuccessfully() {
        final String authenticationCode = "AUTH12345";
        final ParkingSession expectedSession = validParkingSession();

        when(parkingSessionGateway.findByAuthenticationCode(authenticationCode))
                .thenReturn(Optional.of(expectedSession));

        final var result = searchParkingSession.execute(authenticationCode);

        assertThat(result).isNotNull();
        assertThat(result.getAuthenticationCode()).isEqualTo(authenticationCode);
        assertThat(result.getStatus()).isEqualTo(expectedSession.getStatus());
        assertThat(result.getVehicle().getLicensePlate()).isEqualTo(expectedSession.getVehicle().getLicensePlate());

        verify(parkingSessionGateway).findByAuthenticationCode(authenticationCode);
    }

    @Test
    void shouldThrowExceptionWhenParkingSessionNotFound() {
        final String invalidAuthenticationCode = "INVALID123";

        when(parkingSessionGateway.findByAuthenticationCode(invalidAuthenticationCode))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> searchParkingSession.execute(invalidAuthenticationCode))
                .isInstanceOf(ParkingSessionNotFoundException.class)
                .hasMessage("Parking Session with id=[" + invalidAuthenticationCode + "] not found.");

        verify(parkingSessionGateway).findByAuthenticationCode(invalidAuthenticationCode);
    }
}
