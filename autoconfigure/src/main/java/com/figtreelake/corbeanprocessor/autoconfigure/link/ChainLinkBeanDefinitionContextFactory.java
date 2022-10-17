package com.figtreelake.corbeanprocessor.autoconfigure.link;

import com.figtreelake.corbeanprocessor.autoconfigure.util.BeanDefinitionClassRetriever;
import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypeContext;
import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypesRetriever;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Optional;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ChainLinkBeanDefinitionContextFactory<X extends ChainLink<X>> {

  private final BeanDefinitionRegistry beanDefinitionRegistry;

  private final ParameterizedTypesRetriever parameterizedTypesRetriever;

  private final BeanDefinitionClassRetriever beanDefinitionClassRetriever;

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

  @SuppressWarnings("unchecked")
  private Optional<ChainLinkBeanDefinitionContext<X>> addBeanClass(ChainLinkBeanDefinitionContext<X> context) {

    final var optionalBeanClass = beanDefinitionClassRetriever.retrieve(context.getDefinition());
    if (optionalBeanClass.isEmpty()) {
      return Optional.empty();
    }

    if (!ChainLink.class.isAssignableFrom(optionalBeanClass.get())) {
      return Optional.empty();
    }

    final var beanClass = (Class<X>)optionalBeanClass.get();

    final var updatedContext = context.toBuilder()
        .beanClass(beanClass)
        .build();

    return Optional.of(updatedContext);
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
