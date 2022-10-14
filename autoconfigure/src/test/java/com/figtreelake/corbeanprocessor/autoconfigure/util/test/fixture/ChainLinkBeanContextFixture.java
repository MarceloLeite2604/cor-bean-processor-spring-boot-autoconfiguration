package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLinkBeanDefinitionContext;
import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypeContext;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyAbstractChainLink;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class ChainLinkBeanContextFixture {

  public static final ParameterizedTypeContext PARAMETERIZED_TYPE_CONTEXT = ParameterizedTypeContextFixture.createForChainLink(DummyAbstractChainLink.class);

  public static <T extends ChainLink<T>> List<ChainLinkBeanDefinitionContext<T>> create(Class<T> clazz) {
    return BeanFixture.createBeansMap(clazz)
        .entrySet()
        .stream()
        .map(entry -> create(entry, PARAMETERIZED_TYPE_CONTEXT))
        .toList();
  }

  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(Map.Entry<String, T> beanEntry) {
    return create(beanEntry.getKey(), beanEntry.getValue(), PARAMETERIZED_TYPE_CONTEXT);
  }

  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(Map.Entry<String, T> beanEntry, ParameterizedTypeContext parameterizedTypeContext) {
    return create(beanEntry.getKey(), beanEntry.getValue(), parameterizedTypeContext);
  }

  @SuppressWarnings("unchecked")
  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(String name, T bean, ParameterizedTypeContext parameterizedTypeContext) {
    return create(name, (Class<T>) bean.getClass(), parameterizedTypeContext);
  }

  public static <T extends ChainLink<T>> ChainLinkBeanDefinitionContext<T> create(String name, Class<T> beanClass, ParameterizedTypeContext parameterizedTypeContext) {
    return ChainLinkBeanDefinitionContext.<T>builder()
        .definition(BeanDefinitionFixture.create(beanClass))
        .name(name)
        .beanClass(beanClass)
        .chainLinkTypeContext(parameterizedTypeContext)
        .build();
  }
}
