package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation.BeanDefinitionTestImplementation;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.config.BeanDefinition;

@UtilityClass
public class BeanDefinitionFixture {

  public static final boolean PRIMARY = false;

  public static final Class<?> BEAN_CLASS = String.class;

  public static final String BEAN_CLASS_NAME = BEAN_CLASS.getName();

  public static BeanDefinition create() {
    return create(PRIMARY);
  }

  public static BeanDefinition create(boolean primary) {
    return create(primary, BEAN_CLASS_NAME);
  }

  public static BeanDefinition create(Class<?> beanClass) {
    return create(PRIMARY, beanClass.getName());
  }

  public static BeanDefinition create(String beanClassName) {
    return create(PRIMARY, beanClassName);
  }

  public static BeanDefinition create(boolean primary, String beanClassName) {
    return BeanDefinitionTestImplementation.builder()
        .primary(primary)
        .beanClassName(beanClassName)
        .build();
  }
}
