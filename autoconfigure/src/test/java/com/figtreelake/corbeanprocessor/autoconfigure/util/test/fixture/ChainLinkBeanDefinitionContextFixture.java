package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLinkBeanDefinitionContext;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.config.BeanDefinition;

@UtilityClass
public class ChainLinkBeanDefinitionContextFixture {

  public static final String NAME = "nameValue";

  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(Class<? extends T> beanClass) {
    return create(beanClass, NAME);
  }

  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(Class<? extends T> beanClass, String name) {
    return create(beanClass, name, ParameterizedTypeContextFixture.ARGUMENT_CLASS);
  }

  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(Class<? extends T> beanClass, String name, Class<?> argumentClass) {
    final var beanDefinition = BeanDefinitionFixture.create(beanClass);
    return create(beanClass, beanDefinition, name, argumentClass);
  }

  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(Class<? extends T> beanClass, BeanDefinition beanDefinition) {
    return create(beanClass, beanDefinition, NAME, ParameterizedTypeContextFixture.ARGUMENT_CLASS);
  }

  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(
      Class<? extends T> beanClass,
      BeanDefinition beanDefinition,
      String name,
      Class<?> argumentClass) {

    final var chainLinkTypeContext = ParameterizedTypeContextFixture.createForChainLink(argumentClass);

    return ChainLinkBeanDefinitionContext.<T>builder()
        .definition(beanDefinition)
        .name(name)
        .beanClass(beanClass)
        .chainLinkTypeContext(chainLinkTypeContext)
        .build();
  }
}
