package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.BeanDefinitionForTests;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.config.BeanDefinition;

@UtilityClass
public class BeanDefinitionFixture {

  public static BeanDefinition create() {
    return BeanDefinitionForTests.builder()
        .build();
  }
}
