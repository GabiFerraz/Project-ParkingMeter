package com.api.parkingmeter.application.usecase;

import static com.api.parkingmeter.application.usecase.fixture.UpdateParkingSessionTestFixture.*;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.gateway.ParkingSessionGateway;
import com.api.parkingmeter.application.usecase.exception.ParkingSessionCannotBeExtendedException;
import com.api.parkingmeter.application.usecase.exception.ParkingSessionNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class UpdateParkingSessionTest {

  private final ParkingSessionGateway parkingSessionGateway = mock(ParkingSessionGateway.class);
  private final UpdateParkingSession updateParkingSession =
      new UpdateParkingSession(parkingSessionGateway);

  @Test
  void shouldExtendParkingSessionSuccessfully() {
    final var activeSession = activeParkingSession();
    final var updatedSession = updatedParkingSession();
    final ArgumentCaptor<ParkingSession> sessionCaptor =
        ArgumentCaptor.forClass(ParkingSession.class);

    when(parkingSessionGateway.findActiveSessionByLicensePlate(
            activeSession.getVehicle().getLicensePlate()))
        .thenReturn(of(activeSession));
    when(parkingSessionGateway.update(sessionCaptor.capture())).thenReturn(updatedSession);

    final var response = updateParkingSession.execute(activeSession.getVehicle().getLicensePlate());

    assertThat(response.getId()).isEqualTo(updatedSession.getId());
    assertThat(response.getVehicle().getId()).isEqualTo(updatedSession.getVehicle().getId());
    assertThat(response.getVehicle().getLicensePlate())
        .isEqualTo(updatedSession.getVehicle().getLicensePlate());
    assertThat(response.getEndTime()).isEqualTo(updatedSession.getEndTime());
    assertThat(response.isExtendable()).isFalse();
    assertThat(response.getTotalCost()).isEqualTo(updatedSession.getTotalCost());

    verify(parkingSessionGateway)
        .findActiveSessionByLicensePlate(activeSession.getVehicle().getLicensePlate());

    final var sessionCaptured = sessionCaptor.getValue();
    verify(parkingSessionGateway).update(sessionCaptor.getValue());

    assertThat(sessionCaptured.getId()).isEqualTo(updatedSession.getId());
    assertThat(sessionCaptured.getVehicle().getId()).isEqualTo(updatedSession.getVehicle().getId());
    assertThat(sessionCaptured.getVehicle().getLicensePlate())
        .isEqualTo(updatedSession.getVehicle().getLicensePlate());
    assertThat(sessionCaptured.getEndTime()).isEqualTo(updatedSession.getEndTime());
    assertThat(sessionCaptured.isExtendable()).isFalse();
    assertThat(sessionCaptured.getTotalCost()).isEqualTo(updatedSession.getTotalCost());
  }

  @Test
  void shouldThrowExceptionWhenSessionNotFound() {
    final var licensePlate = "XYZ1234";
    final String MESSAGE = "No parking session were found for the car with license plate=[%s].";

    when(parkingSessionGateway.findActiveSessionByLicensePlate(licensePlate))
        .thenThrow(new ParkingSessionNotFoundException(MESSAGE, licensePlate));

    assertThatThrownBy(() -> updateParkingSession.execute(licensePlate))
        .isInstanceOf(ParkingSessionNotFoundException.class)
        .hasMessage("No parking session were found for the car with license plate=[XYZ1234].");

    verify(parkingSessionGateway).findActiveSessionByLicensePlate(licensePlate);
    verify(parkingSessionGateway, never()).update(any());
  }

  @Test
  void shouldThrowExceptionWhenSessionCannotBeExtended() {
    final var activeSession = activeParkingSessionNotExtendable();
    final var licensePlate = activeSession.getVehicle().getLicensePlate();

    when(parkingSessionGateway.findActiveSessionByLicensePlate(licensePlate))
        .thenReturn(of(activeSession));

    assertThatThrownBy(() -> updateParkingSession.execute(licensePlate))
        .isInstanceOf(ParkingSessionCannotBeExtendedException.class)
        .hasMessage("Parking session with license plate=[XYZ1234] cannot be extended.");

    verify(parkingSessionGateway)
        .findActiveSessionByLicensePlate(activeSession.getVehicle().getLicensePlate());
    verify(parkingSessionGateway, never()).update(any());
  }
}
