package com.api.parkingmeter.application.usecase;

import static com.api.parkingmeter.application.usecase.fixture.CreateParkingSessionTestFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.domain.PaymentMethod;
import com.api.parkingmeter.application.gateway.ParkingSessionGateway;
import com.api.parkingmeter.application.gateway.VehicleGateway;
import com.api.parkingmeter.application.usecase.exception.VehicleNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CreateParkingSessionTest {

  private final ParkingSessionGateway parkingSessionGateway = mock(ParkingSessionGateway.class);
  private final VehicleGateway vehicleGateway = mock(VehicleGateway.class);

  private final CreateParkingSession createParkingSession =
      new CreateParkingSession(parkingSessionGateway, vehicleGateway);

  @Test
  void shouldCreateAParkingSessionSuccessfully() {
    final var licensePlate = "AAA0000";
    final var startTime = LocalDateTime.parse("2025-01-10T10:00:00");
    final var endTime = LocalDateTime.parse("2025-01-10T11:00:00");
    final var paymentMethod = PaymentMethod.DEBIT_CARD;
    final var vehicleGatewayResponse = validVehicle();
    final var parkingSessionGatewayResponse = validParkingSession();
    final var vehicleResponse = validVehicleDto();

    when(vehicleGateway.findByLicensePlate(licensePlate))
        .thenReturn(Optional.of(vehicleGatewayResponse));
    when(parkingSessionGateway.save(any(), any(ParkingSession.class)))
        .thenReturn(parkingSessionGatewayResponse);

    final var response =
        createParkingSession.execute(licensePlate, startTime, endTime, paymentMethod);

    assertThat(response.getId()).isEqualTo(1);
    assertThat(response.getVehicle().getId()).isEqualTo(vehicleResponse.getId());
    assertThat(response.getVehicle().getLicensePlate())
        .isEqualTo(vehicleResponse.getLicensePlate());
    assertThat(response.getVehicle().getOwnerName()).isEqualTo(vehicleResponse.getOwnerName());
    assertThat(response.getStartTime()).isEqualTo(startTime);
    assertThat(response.getEndTime()).isEqualTo(endTime);
    assertThat(response.isExtendable()).isTrue();
    assertThat(response.getPaymentMethod()).isEqualTo(paymentMethod);
    assertThat(response.getTotalCost()).isEqualTo("10");
    assertThat(response.getAuthenticationCode()).isEqualTo("123uuid456");
    assertThat(response.getStatus()).isEqualTo(ParkingSessionStatus.ACTIVE);

    verify(vehicleGateway).findByLicensePlate(licensePlate);
    verify(parkingSessionGateway).save(any(), any(ParkingSession.class));
  }

  @Test
  void shouldNotCreateParkingSessionWhenVehicleNotFound() {
    final var licensePlate = "AAA0000";
    final var startTime = LocalDateTime.parse("2025-01-10T10:00:00");
    final var endTime = LocalDateTime.parse("2025-01-10T11:00:00");
    final var paymentMethod = PaymentMethod.DEBIT_CARD;

    when(vehicleGateway.findByLicensePlate(licensePlate)).thenReturn(Optional.empty());

    assertThatThrownBy(
            () -> createParkingSession.execute(licensePlate, startTime, endTime, paymentMethod))
        .isInstanceOf(VehicleNotFoundException.class)
        .hasMessage("Vehicle with license plate=[AAA0000] not found.");

    verify(vehicleGateway).findByLicensePlate(licensePlate);
    verify(parkingSessionGateway, never()).save(any(), any(ParkingSession.class));
  }
}
