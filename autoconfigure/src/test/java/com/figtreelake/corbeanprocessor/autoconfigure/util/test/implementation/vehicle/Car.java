package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.vehicle;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Car extends Vehicle {

  private final int seats;

  private final int doors;
}
