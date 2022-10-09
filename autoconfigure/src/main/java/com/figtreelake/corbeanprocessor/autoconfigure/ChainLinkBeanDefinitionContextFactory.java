package com.figtreelake.corbeanprocessor.autoconfigure;

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

  public  Optional<ChainLinkBeanDefinitionContext<X>> create(String beanName) {
    return Optional.of(createContext(beanName))
        .flatMap(this::addBeanClass)
        .flatMap(this::addChainLinkTypeContext);
  }

  private ChainLinkBeanDefinitionContext<X> createContext(String beanName) {
    final var beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);

    return ChainLinkBeanDefinitionContext.<X>builder()
        .name(beanName)
        .definition(beanDefinition).build();
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

  @SuppressWarnings("unchecked")
  private Optional<Class<X>> retrieveBeanClass(BeanDefinition beanDefinition) {
    final var className = beanDefinition.getBeanClassName();
    if (className == null) {
      return Optional.empty();
    }
    try {
      final var beanClass = Class.forName(className);
      if (!ChainLink.class.isAssignableFrom(beanClass)) {
        return Optional.empty();
      }
      return Optional.of((Class<X>) beanClass);
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
