package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.chain.ChainContext;
import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLinkBeanDefinitionContext;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ChainContextFixture {

  public static <T extends ChainLink<T>> ChainContext<T> create(Class<T> chainClazz) {
    final var chainLinkBeansByName = BeanFixture.createBeansMap(chainClazz);

    final var beanNames = chainLinkBeansByName.keySet()
        .stream()
        .toList();

    final List<ChainLinkBeanDefinitionContext<T>> beanDefinitionContexts = chainLinkBeansByName.entrySet()
        .stream()
        .map(ChainLinkBeanContextFixture::create)
        .toList();


    return ChainContext.<T>builder()
        .chainLinkBeansByName(chainLinkBeansByName)
        .beanNames(beanNames)
        .beanDefinitionContexts(beanDefinitionContexts)
        .build();
  }
}
