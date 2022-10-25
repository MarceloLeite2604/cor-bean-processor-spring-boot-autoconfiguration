package com.figtreelake.corbeanprocessor.autoconfigure.link;

import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypeContext;
import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypesRetriever;
import com.figtreelake.corbeanprocessor.autoconfigure.util.BeanDefinitionClassRetriever;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Optional;

/**
 * Factory to create {@link ChainLinkBeanDefinitionContext} objects.
 *
 * @param <T> Element that either implements or extends the {@link ChainLink}
 *            interface.
 * @author MarceloLeite2604
 */
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ChainLinkBeanDefinitionContextFactory<T extends ChainLink<T>> {

  private final BeanDefinitionRegistry beanDefinitionRegistry;

  private final ParameterizedTypesRetriever parameterizedTypesRetriever;

  private final BeanDefinitionClassRetriever beanDefinitionClassRetriever;

  /**
   * Create a {@link ChainLinkBeanDefinitionContext}
   *
   * @param beanName The bean name to be retrieved through {@link BeanDefinitionRegistry}
   * @return An {@link Optional} containing the {@link ChainLinkBeanDefinitionContext}
   * created of empty if it was not possible to create the object.
   */
  public Optional<ChainLinkBeanDefinitionContext<T>> create(String beanName) {
    return Optional.of(createContext(beanName))
        .flatMap(this::addBeanClass)
        .flatMap(this::addChainLinkTypeContext);
  }

  private ChainLinkBeanDefinitionContext<T> createContext(String beanName) {
    final var beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanName);

    return ChainLinkBeanDefinitionContext.<T>builder()
        .name(beanName)
        .definition(beanDefinition)
        .build();
  }

  @SuppressWarnings("unchecked")
  private Optional<ChainLinkBeanDefinitionContext<T>> addBeanClass(ChainLinkBeanDefinitionContext<T> context) {

    final var optionalBeanClass = beanDefinitionClassRetriever.retrieve(context.getDefinition());
    if (optionalBeanClass.isEmpty()) {
      return Optional.empty();
    }

    if (!ChainLink.class.isAssignableFrom(optionalBeanClass.get())) {
      return Optional.empty();
    }

    final var beanClass = (Class<T>) optionalBeanClass.get();

    final var updatedContext = context.toBuilder()
        .beanClass(beanClass)
        .build();

    return Optional.of(updatedContext);
  }

  private Optional<ChainLinkBeanDefinitionContext<T>> addChainLinkTypeContext(ChainLinkBeanDefinitionContext<T> context) {

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

  private Optional<ParameterizedTypeContext> retrieveChainLinkTypeContext(ChainLinkBeanDefinitionContext<T> context) {
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
