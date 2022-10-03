package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@Getter
public abstract class Vehicle {

  private final String name;

  private final double maxSpeedKph;

  private final VehicleType type;
}
