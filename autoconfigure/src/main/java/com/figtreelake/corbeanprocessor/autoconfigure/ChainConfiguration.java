package com.figtreelake.corbeanprocessor.autoconfigure;

import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("java:S1118")
@Configuration
public class ChainConfiguration {

  @Bean
  public static BeanDefinitionRegistryPostProcessor createBeanDefinitionRegistryPostProcessor() {
    return new ChainBeanDefinitionRegistryPostProcessor<>();
  }
}
