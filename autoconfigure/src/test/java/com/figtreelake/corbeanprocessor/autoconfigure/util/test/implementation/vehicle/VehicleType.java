package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum VehicleType {
  CAR("car"),
  BOAT("boat"),
  PLANE("plane");

  private final String value;
}
