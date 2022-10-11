package com.figtreelake.corbeanprocessor.autoconfigure.chain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import static org.assertj.core.api.Assertions.assertThat;

class ChainConfigurationTest {

  @Test
  void shouldCreateBeanDefinitionRegistryPostProcessorBean() {
    final var beanDefinitionRegistryPostProcessor = ChainConfiguration.createBeanDefinitionRegistryPostProcessor();

    assertThat(beanDefinitionRegistryPostProcessor).isInstanceOf(BeanDefinitionRegistryPostProcessor.class);
  }
}