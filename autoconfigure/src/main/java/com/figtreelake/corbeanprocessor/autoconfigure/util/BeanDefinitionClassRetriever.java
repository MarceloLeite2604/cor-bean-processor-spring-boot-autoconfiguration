package com.figtreelake.corbeanprocessor.autoconfigure.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Optional;

@RequiredArgsConstructor
public class BeanDefinitionClassRetriever {

  private final BeanDefinitionRegistry beanDefinitionRegistry;

  public Optional<Class<?>> retrieve(BeanDefinition beanDefinition) {
    var optionalBeanClassName = retrieveBeanClassName(beanDefinition);

    if (optionalBeanClassName.isEmpty()) {
      return Optional.empty();
    }

    final var className = optionalBeanClassName.get();

    return ClassRetriever.getInstance()
        .retrieve(className);
  }

  private Optional<String> retrieveBeanClassName(BeanDefinition beanDefinition) {
    final var optionalBeanClassName = Optional.ofNullable(beanDefinition.getBeanClassName());

    if (optionalBeanClassName.isPresent()) {
      return optionalBeanClassName;
    }

    return retrieveBeanClassThroughBeanFactory(beanDefinition);
  }

  private Optional<String> retrieveBeanClassThroughBeanFactory(BeanDefinition beanDefinition) {
    final var factoryMethodName = beanDefinition.getFactoryMethodName();
    if (factoryMethodName == null) {
      return Optional.empty();
    }

    final var factoryBeanName = beanDefinition.getFactoryBeanName();
    if (factoryBeanName == null) {
      return Optional.empty();
    }

    final var factoryBeanDefinition = beanDefinitionRegistry.getBeanDefinition(factoryBeanName);

    final var factoryBeanClassName = factoryBeanDefinition.getBeanClassName();

    final var optionalFactoryBeanClass = ClassRetriever.getInstance()
        .retrieve(factoryBeanClassName);

    if (optionalFactoryBeanClass.isEmpty()) {
      return Optional.empty();
    }

    final var factoryBeanClass = optionalFactoryBeanClass.get();

    final var optionalMethod = MethodRetriever.getInstance()
        .retrieve(factoryBeanClass, factoryMethodName);
    if (optionalMethod.isEmpty()) {
      return Optional.empty();
    }

    final var method = optionalMethod.get();

    return Optional.ofNullable(method.getGenericReturnType()
        .getTypeName());

  }

}
