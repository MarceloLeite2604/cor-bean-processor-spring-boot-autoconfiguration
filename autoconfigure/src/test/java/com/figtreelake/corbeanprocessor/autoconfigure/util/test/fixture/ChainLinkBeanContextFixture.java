package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.ChainLinkBeanDefinitionContext;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ChainLinkBeanContextFixture {

  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(T bean) {
    return ChainLinkBeanDefinitionContext.<T>builder()
        .beanDefinition(bean)
        .definition(BeanDefinitionFixture.create())
        .name(bean.getClass().getSimpleName())
        .build();
  }
}
