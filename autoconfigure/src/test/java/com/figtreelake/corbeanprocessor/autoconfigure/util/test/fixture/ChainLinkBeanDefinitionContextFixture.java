package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLinkBeanDefinitionContext;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.config.BeanDefinition;

@UtilityClass
public class ChainLinkBeanDefinitionContextFixture {

  public static final String NAME = "nameValue";

  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(Class<? extends T> beanClass) {
    final var definition = BeanDefinitionFixture.create();
    return create(beanClass, definition);
  }

  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(Class<? extends T> beanClass, BeanDefinition beanDefinition) {
    final var chainLinkTypeContext = ParameterizedTypeContextFixture.createForChainLink(beanClass);
    return ChainLinkBeanDefinitionContext.<T>builder()
        .definition(beanDefinition)
        .name(NAME)
        .beanClass(beanClass)
        .chainLinkTypeContext(chainLinkTypeContext)
        .build();
  }
}
