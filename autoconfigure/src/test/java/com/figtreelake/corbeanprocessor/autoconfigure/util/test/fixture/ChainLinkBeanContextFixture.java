package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLinkBeanDefinitionContext;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class ChainLinkBeanContextFixture {

  public static <T extends ChainLink<T>> List<ChainLinkBeanDefinitionContext<T>> create(Class<T> clazz) {
    return BeanFixture.createBeansMap(clazz)
        .entrySet().stream().map(ChainLinkBeanContextFixture::create)
        .toList();
  }

  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(Map.Entry<String, T> beanEntry) {
    return create(beanEntry.getKey(), beanEntry.getValue());
  }

  @SuppressWarnings("unchecked")
  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(String name, T bean) {
    return create(name, (Class<T>)bean.getClass());
  }

  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(String name, Class<T> beanClass) {
    return ChainLinkBeanDefinitionContext.<T>builder()
        .definition(BeanDefinitionFixture.create())
        .name(name)
        .beanClass(beanClass)
        .build();
  }
}
