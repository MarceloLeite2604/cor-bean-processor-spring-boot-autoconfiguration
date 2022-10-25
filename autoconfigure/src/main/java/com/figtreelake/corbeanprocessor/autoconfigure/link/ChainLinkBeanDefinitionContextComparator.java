package com.figtreelake.corbeanprocessor.autoconfigure.link;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Comparator;
import java.util.Optional;

/**
 * A {@link Comparator} for {@link ChainLinkBeanDefinitionContext} elements.
 *
 * @author MarceloLeite2604
 */
public class ChainLinkBeanDefinitionContextComparator
    implements Comparator<ChainLinkBeanDefinitionContext<?>> {

  @Override
  public int compare(ChainLinkBeanDefinitionContext firstContext, ChainLinkBeanDefinitionContext secondContext) {
    if (firstContext.getDefinition()
        .isPrimary()) {
      if (secondContext.getDefinition()
          .isPrimary()) {
        return 0;
      }
      return -1;
    }

    if (secondContext.getDefinition()
        .isPrimary()) {
      return 1;
    }

    final var firstContextClassOrder = retrieveClassOrder(firstContext.getBeanClass());

    final var secondContextClassOrder = retrieveClassOrder(secondContext.getBeanClass());

    return Integer.compare(firstContextClassOrder, secondContextClassOrder);
  }

  private int retrieveClassOrder(Class<?> clazz) {
    return Optional.ofNullable(clazz.getAnnotation(Order.class))
        .map(Order::value)
        .orElse(Ordered.LOWEST_PRECEDENCE);
  }
}
