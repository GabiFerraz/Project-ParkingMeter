package com.api.parkingmeter.infrastructure.controller;

import static com.api.parkingmeter.infrastructure.controller.fixture.ParkingSessionControllerTestFixture.AUTHENTICATION_CODE;
import static com.api.parkingmeter.infrastructure.controller.fixture.ParkingSessionControllerTestFixture.END_TIME;
import static com.api.parkingmeter.infrastructure.controller.fixture.ParkingSessionControllerTestFixture.LICENSE_PLATE;
import static com.api.parkingmeter.infrastructure.controller.fixture.ParkingSessionControllerTestFixture.OWNER_NAME;
import static com.api.parkingmeter.infrastructure.controller.fixture.ParkingSessionControllerTestFixture.START_TIME;
import static com.api.parkingmeter.infrastructure.controller.fixture.ParkingSessionControllerTestFixture.validCreateResponse;
import static com.api.parkingmeter.infrastructure.controller.fixture.ParkingSessionControllerTestFixture.validRequest;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.domain.PaymentMethod;
import com.api.parkingmeter.application.usecase.CreateParkingSession;
import com.api.parkingmeter.application.usecase.SearchParkingSession;
import com.api.parkingmeter.application.usecase.UpdateParkingSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ParkingSessionControllerTest {
  private static final String BASE_URL = "/parkingmeter/parking-sessions";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private CreateParkingSession createParkingSession;
  @MockBean private UpdateParkingSession updateParkingSession;
  @MockBean private SearchParkingSession searchParkingSession;

  @Test
  void shouldCreateAParkingSessionSuccessfully() throws Exception {
    final var request = validRequest();
    final var createResponse = validCreateResponse();
    final var startTime = "2126-01-10T10:00:00";
    final var endTime = "2126-01-10T11:00:01";
    when(createParkingSession.execute(
            LICENSE_PLATE, START_TIME, END_TIME, PaymentMethod.DEBIT_CARD))
        .thenReturn(createResponse);

    mockMvc
        .perform(
            post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.vehicle.id").value(1))
        .andExpect(jsonPath("$.vehicle.licensePlate").value(LICENSE_PLATE))
        .andExpect(jsonPath("$.vehicle.ownerName").value(OWNER_NAME))
        .andExpect(jsonPath("$.startTime").value(startTime))
        .andExpect(jsonPath("$.endTime").value(endTime))
        .andExpect(jsonPath("$.extendable").value(true))
        .andExpect(jsonPath("$.paymentMethod").value(PaymentMethod.DEBIT_CARD.toString()))
        .andExpect(jsonPath("$.totalCost").value("10"))
        .andExpect(jsonPath("$.authenticationCode").value(AUTHENTICATION_CODE))
        .andExpect(jsonPath("$.status").value("ACTIVE"));
  }

  @Test
  void shouldExtendParkingSessionSuccessfully() throws Exception {
    final String licensePlate = "AAA0000";

    final var updatedSession =
        ParkingSession.builder()
            .id(1)
            .startTime(START_TIME)
            .endTime(START_TIME.plusHours(1))
            .extendable(true)
            .paymentMethod(PaymentMethod.DEBIT_CARD)
            .totalCost(new BigDecimal("15.0"))
            .authenticationCode(AUTHENTICATION_CODE)
            .status(ParkingSessionStatus.ACTIVE)
            .vehicle(validCreateResponse().getVehicle())
            .build();

    when(updateParkingSession.execute(licensePlate)).thenReturn(updatedSession);

    mockMvc
        .perform(
            put(BASE_URL + "/extend")
                .param("licensePlate", licensePlate)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.vehicle.licensePlate").value(licensePlate))
        .andExpect(jsonPath("$.endTime").value("2126-01-10T11:00:00"))
        .andExpect(jsonPath("$.totalCost").value("15.0"));
  }

  @Test
  void shouldFindParkingSessionByAuthenticationCodeSuccessfully() throws Exception {
    final var authenticationCode = AUTHENTICATION_CODE;
    final var parkingSession = validCreateResponse();
    final var startTime = "2126-01-10T10:00:00";
    final var endTime = "2126-01-10T11:00:01";

    when(searchParkingSession.execute(authenticationCode)).thenReturn(parkingSession);

    mockMvc
        .perform(
            get(BASE_URL + "/code/{authenticationCode}", authenticationCode)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.vehicle.licensePlate").value(LICENSE_PLATE))
        .andExpect(jsonPath("$.vehicle.ownerName").value(OWNER_NAME))
        .andExpect(jsonPath("$.startTime").value(startTime))
        .andExpect(jsonPath("$.endTime").value(endTime))
        .andExpect(jsonPath("$.extendable").value(true))
        .andExpect(jsonPath("$.paymentMethod").value(PaymentMethod.DEBIT_CARD.toString()))
        .andExpect(jsonPath("$.totalCost").value("10"))
        .andExpect(jsonPath("$.authenticationCode").value(authenticationCode))
        .andExpect(jsonPath("$.status").value(ParkingSessionStatus.ACTIVE.toString()));
  }
}
