package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.BeanDefinitionForTests;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.config.BeanDefinition;

@UtilityClass
public class BeanDefinitionFixture {

  public static final boolean PRIMARY = false;

  public static BeanDefinition create() {
    return create(PRIMARY);
  }

  public static BeanDefinition create(boolean primary) {
    return BeanDefinitionForTests.builder()
        .primary(primary)
        .build();
  }
}
