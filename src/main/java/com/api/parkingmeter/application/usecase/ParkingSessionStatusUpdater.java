package com.api.parkingmeter.application.usecase;

import com.api.parkingmeter.application.domain.ParkingSession;
import com.api.parkingmeter.application.domain.ParkingSessionStatus;
import com.api.parkingmeter.application.gateway.ParkingSessionGateway;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParkingSessionStatusUpdater {

  private final ParkingSessionGateway parkingSessionGateway;

  @Scheduled(fixedRate = 60000) // Executa a cada 60 segundos
  @Transactional
  public void updateExpiredSessions() {
    log.info("Starting updating parking sessions...");

    int page = 0;
    final int size = 100;

    Page<ParkingSession> activeSessionsPage;

    do {
      activeSessionsPage =
          parkingSessionGateway.findByStatus(
              ParkingSessionStatus.ACTIVE, PageRequest.of(page, size));
      log.info(
          "Page {} processed with {} active sessions.",
          page,
          activeSessionsPage.getNumberOfElements());

      activeSessionsPage.forEach(
          session -> {
            if (session.getEndTime().isBefore(LocalDateTime.now())) {
              parkingSessionGateway.updateStatus(session.getId(), ParkingSessionStatus.FINISHED);

              log.info("Session with ID {} updated to FINISHED status.", session.getId());
            }
          });

      page++;

    } while (activeSessionsPage.hasNext());

    log.info("Parking sessions update complete.");
  }
}
