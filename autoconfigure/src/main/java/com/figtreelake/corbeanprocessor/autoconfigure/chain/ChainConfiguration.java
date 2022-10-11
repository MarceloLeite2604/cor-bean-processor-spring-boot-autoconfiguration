package com.figtreelake.corbeanprocessor.autoconfigure.chain;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLinkBeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A Spring context configuration class to create all beans required to
 * implement chain of responsibility automatically.
 */
@SuppressWarnings("java:S1118")
@Configuration
public class ChainConfiguration {

  /**
   * Creates a bean of type {@link BeanDefinitionRegistryPostProcessor}
   * responsible for processing {@link ChainLink} beans.
   * @return An instance of {@link ChainLinkBeanDefinitionRegistryPostProcessor}.
   */
  @Bean
  public static BeanDefinitionRegistryPostProcessor createBeanDefinitionRegistryPostProcessor() {
    return new ChainLinkBeanDefinitionRegistryPostProcessor<>();
  }
}
