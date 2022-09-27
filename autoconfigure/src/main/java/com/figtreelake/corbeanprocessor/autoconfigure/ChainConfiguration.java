package com.figtreelake.corbeanprocessor.autoconfigure;

import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChainConfiguration {

  public ChainConfiguration() {
    System.out.println("Instantiating ChainConfiguration");
  }

  @Bean
  public static BeanDefinitionRegistryPostProcessor createBeanDefinitionRegistryPostProcessor() {
    return new ChainBeanDefinitionRegistryPostProcessor<>();
  }
}
