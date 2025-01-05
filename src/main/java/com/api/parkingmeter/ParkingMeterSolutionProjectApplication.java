package com.api.parkingmeter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ParkingMeterSolutionProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(ParkingMeterSolutionProjectApplication.class, args);
  }
}
