package com.figtreelake.corbeanprocessor.autoconfigure.link;

import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypeContext;
import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypesRetriever;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Optional;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ChainLinkBeanDefinitionContextFactory<X extends ChainLink<X>> {

  private final BeanDefinitionRegistry beanDefinitionRegistry;

  private final ParameterizedTypesRetriever parameterizedTypesRetriever;

  public Optional<ChainLinkBeanDefinitionContext<X>> create(String beanName) {
    return Optional.of(createContext(beanName))
        .flatMap(this::addBeanClass)
        .flatMap(this::addChainLinkTypeContext);
  }

  private ChainLinkBeanDefinitionContext<X> createContext(String beanName) {
    final var beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);

    return ChainLinkBeanDefinitionContext.<X>builder()
        .name(beanName)
        .definition(beanDefinition)
        .build();
  }

  private Optional<ChainLinkBeanDefinitionContext<X>> addBeanClass(ChainLinkBeanDefinitionContext<X> context) {

    final var optionalBeanClass = retrieveBeanClass(context.getDefinition());
    if (optionalBeanClass.isEmpty()) {
      return Optional.empty();
    }

    final var beanClass = optionalBeanClass.get();

    final var updatedContext = context.toBuilder()
        .beanClass(beanClass)
        .build();

    return Optional.of(updatedContext);
  }

  private Optional<Class<X>> retrieveBeanClass(BeanDefinition beanDefinition) {
    var optionalBeanClassName = retrieveBeanClassName(beanDefinition);

    if (optionalBeanClassName.isEmpty()) {
      return Optional.empty();
    }

    final var className = optionalBeanClassName.get();

    return retrieveClass(className);
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

    final var optionalFactoryBeanClass = retrieveClass(factoryBeanClassName);

    if (optionalFactoryBeanClass.isEmpty()) {
      return Optional.empty();
    }

    final var factoryBeanClass = optionalFactoryBeanClass.get();

    try {
      final var method = factoryBeanClass.getMethod(factoryMethodName);

      return Optional.ofNullable(method.getGenericReturnType()
          .getTypeName());

    } catch (NoSuchMethodException noSuchMethodException) {
      final var message = String.format("Exception thrown while searching for method \"%s\" on class \"%s\".", factoryMethodName, factoryBeanClassName);
      log.warn(message, noSuchMethodException);
      return Optional.empty();
    }
  }

  @SuppressWarnings("unchecked")
  private <T> Optional<Class<T>> retrieveClass(String className) {
    try {
      return Optional.of((Class<T>) Class.forName(className));

    } catch (ClassNotFoundException classNotFoundException) {
      final var message = String.format("Exception thrown while searching class \"%s\".", className);
      log.warn(message, classNotFoundException);
      return Optional.empty();
    }
  }

  private Optional<ChainLinkBeanDefinitionContext<X>> addChainLinkTypeContext(ChainLinkBeanDefinitionContext<X> context) {

    final var optionalChainLinkTypeContext = retrieveChainLinkTypeContext(context);
    if (optionalChainLinkTypeContext.isEmpty()) {
      return Optional.empty();
    }

    final var chainLinkTypeContext = optionalChainLinkTypeContext.get();

    final var updatedContext = context.toBuilder()
        .chainLinkTypeContext(chainLinkTypeContext)
        .build();

    return Optional.of(updatedContext);
  }

  private Optional<ParameterizedTypeContext> retrieveChainLinkTypeContext(ChainLinkBeanDefinitionContext<X> context) {
    return parameterizedTypesRetriever.retrieveForClass(context.getBeanClass())
        .stream()
        .filter(parameterizedTypeContext -> {
          final var rawType = parameterizedTypeContext.getParameterizedType()
              .getRawType();
          return ChainLink.class.equals(rawType);
        })
        .findFirst();
  }
}
