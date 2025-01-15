package com.api.parkingmeter.application.usecase;

import static com.api.parkingmeter.application.usecase.fixture.CreateParkingSessionTestFixture.validParkingSession;
import static com.api.parkingmeter.application.usecase.fixture.CreateParkingSessionTestFixture.validVehicle;
import static com.api.parkingmeter.application.usecase.fixture.CreateParkingSessionTestFixture.validVehicleDto;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.domain.PaymentMethod;
import com.api.parkingmeter.application.gateway.ParkingSessionGateway;
import com.api.parkingmeter.application.gateway.VehicleGateway;
import com.api.parkingmeter.application.usecase.exception.VehicleNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

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
    final ArgumentCaptor<String> licensePlateCaptor = ArgumentCaptor.forClass(String.class);
    final ArgumentCaptor<ParkingSession> parkingSessionCaptor =
        ArgumentCaptor.forClass(ParkingSession.class);

    when(vehicleGateway.findByLicensePlate(licensePlate))
        .thenReturn(Optional.of(vehicleGatewayResponse));
    when(parkingSessionGateway.save(licensePlateCaptor.capture(), parkingSessionCaptor.capture()))
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
    verify(parkingSessionGateway)
        .save(licensePlateCaptor.getValue(), parkingSessionCaptor.getValue());
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
    verify(parkingSessionGateway, never()).save(any(), any());
  }
}
