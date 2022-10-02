package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.ChainLinkBeanContext;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ChainLinkBeanContextFixture {

  public static <T extends ChainLink<T>> ChainLinkBeanContext<T> create(T bean) {
    return ChainLinkBeanContext.<T>builder()
        .bean(bean)
        .definition(BeanDefinitionFixture.create())
        .name(bean.getClass().getSimpleName())
        .build();
  }
}
