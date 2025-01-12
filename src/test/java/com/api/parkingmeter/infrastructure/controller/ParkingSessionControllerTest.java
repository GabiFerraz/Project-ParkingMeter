package com.api.parkingmeter.infrastructure.controller;

import static com.api.parkingmeter.infrastructure.controller.fixture.ParkingSessionControllerTestFixture.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.api.parkingmeter.application.domain.PaymentMethod;
import com.api.parkingmeter.application.usecase.CreateParkingSession;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  @Test
  void shouldCreateAParkingSessionSuccessfully() throws Exception {
    final var request = validRequest();
    final var createResponse = validCreateResponse();

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
        .andExpect(jsonPath("$.startTime").value("2025-01-10T10:00:00"))
        .andExpect(jsonPath("$.endTime").value("2025-01-10T11:00:00"))
        .andExpect(jsonPath("$.extendable").value(true))
        .andExpect(jsonPath("$.paymentMethod").value(PaymentMethod.DEBIT_CARD.toString()))
        .andExpect(jsonPath("$.totalCost").value("10"))
        .andExpect(jsonPath("$.authenticationCode").value(AUTHENTICATION_CODE))
        .andExpect(jsonPath("$.status").value("ACTIVE"));
  }
}
