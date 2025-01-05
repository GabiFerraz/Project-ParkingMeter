package com.api.parkingmeter.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "license_plate", nullable = false, unique = true)
  private String licensePlate;

  @Column(name = "owner_name")
  private String ownerName;

  @OneToMany(
      mappedBy = "vehicle",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  @Builder.Default
  private List<ParkingSessionEntity> parkingSessions = new ArrayList<>();

  public void addParkingSession(final ParkingSessionEntity parkingSession) {
    this.parkingSessions.add(parkingSession);
    parkingSession.setVehicle(this);
  }
}
