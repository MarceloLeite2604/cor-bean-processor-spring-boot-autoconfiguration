package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.function.BiPredicate;

public class ParameterizedTypeEqualsPredicate implements BiPredicate<ParameterizedType, ParameterizedType> {

  @Override
  public boolean test(ParameterizedType p1, ParameterizedType p2) {
    return Objects.equals(p1.getRawType(), p2.getRawType()) &&
        Objects.equals(p1.getOwnerType(), p2.getOwnerType());
  }
}
