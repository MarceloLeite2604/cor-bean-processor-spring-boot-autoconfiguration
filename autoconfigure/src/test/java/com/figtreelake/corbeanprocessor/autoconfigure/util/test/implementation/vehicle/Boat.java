package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Boat extends Vehicle {

  private final double lengthMeters;

  private final String poweredBy;
}
