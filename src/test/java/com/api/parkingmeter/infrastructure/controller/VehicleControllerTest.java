package com.api.parkingmeter.infrastructure.controller;

import static com.api.parkingmeter.infrastructure.controller.fixture.VehicleControllerTestFixture.validRequest;
import static com.api.parkingmeter.infrastructure.controller.fixture.VehicleControllerTestFixture.validResponse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.api.parkingmeter.application.usecase.CreateVehicle;
import com.api.parkingmeter.application.usecase.SearchVehicle;
import com.api.parkingmeter.application.usecase.UpdateVehicle;
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
class VehicleControllerTest {

  private static final String BASE_URL = "/parkingmeter/vehicle";

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private CreateVehicle createVehicle;
  @MockBean private SearchVehicle searchVehicle;
  @MockBean private UpdateVehicle updateVehicle;

  @Test
  void shouldCreateAVehicleSuccessfully() throws Exception {
    final var request = validRequest();
    final var createResponse = validResponse();

    when(createVehicle.execute(request.getLicensePlate(), request.getOwnerName()))
        .thenReturn(createResponse);

    mockMvc
        .perform(
            post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.licensePlate").value("AAA0000"))
        .andExpect(jsonPath("$.ownerName").value("John Doe"))
        .andExpect(jsonPath("$.parkingSessions").isEmpty());
  }

  @Test
  void shouldSearchAVehicleSuccessfully() throws Exception {
    final var request = "AAA0000";
    final var response = validResponse();

    when(searchVehicle.execute(request)).thenReturn(response);

    mockMvc
        .perform(
            get(BASE_URL + "/{licensePlate}", request)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.licensePlate").value("AAA0000"))
        .andExpect(jsonPath("$.ownerName").value("John Doe"))
        .andExpect(jsonPath("$.parkingSessions").isEmpty());
  }

  @Test
  void shouldUpdateAVehicleSuccessfully() throws Exception {
    final var request = validRequest();
    final var updatedResponse = validResponse();

    when(updateVehicle.execute(anyString(), anyString(), anyString())).thenReturn(updatedResponse);

    mockMvc
        .perform(
            put(BASE_URL + "/{licensePlate}", request.getLicensePlate())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.licensePlate").value("AAA0000"))
        .andExpect(jsonPath("$.ownerName").value("John Doe"))
        .andExpect(jsonPath("$.parkingSessions").isEmpty());
  }
}
