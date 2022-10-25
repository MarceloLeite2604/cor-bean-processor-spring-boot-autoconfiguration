package com.figtreelake.corbeanprocessor.autoconfigure.chain;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLinkBeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A Spring context configuration class to create all beans required to
 * implement chain of responsibility automatically.
 *
 * @author MarceloLeite2604
 */
@SuppressWarnings("java:S1118")
@Configuration
@ConditionalOnProperty(
    name = "cor-bean-processor.enabled",
    value = "true",
    matchIfMissing = true)
public class ChainConfiguration {

  /**
   * Creates a bean of type {@link BeanDefinitionRegistryPostProcessor}
   * responsible for processing {@link ChainLink} beans and creating the chains of responsibility.
   *
   * @return An instance of {@link ChainLinkBeanDefinitionRegistryPostProcessor}.
   */
  @Bean
  public static BeanDefinitionRegistryPostProcessor createBeanDefinitionRegistryPostProcessor() {
    return new ChainLinkBeanDefinitionRegistryPostProcessor<>();
  }
}
