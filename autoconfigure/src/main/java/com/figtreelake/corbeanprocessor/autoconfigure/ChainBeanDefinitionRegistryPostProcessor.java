package com.figtreelake.corbeanprocessor.autoconfigure;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link BeanDefinitionRegistryPostProcessor} implementation to retrieve
 * {@link ChainLink} beans that implements the same type and concatenate them
 * to build a chain of responsibility.
 *
 * @param <X> Defines a consistent {@link ChainLink} implementation that will
 *            be used throughout all methods on this class. It can be defined
 *            as a wildcard on the actual implementation.
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ChainBeanDefinitionRegistryPostProcessor<X extends ChainLink<X>> implements BeanDefinitionRegistryPostProcessor {

  private final ParameterizedTypesRetriever parameterizedTypesRetriever;

  private final ChainAssembler chainAssembler;

  @Getter(AccessLevel.PACKAGE)
  private BeanDefinitionRegistry beanDefinitionRegistry;

  private ConfigurableListableBeanFactory configurableListableBeanFactory;

  /**
   * Instantiates an object of type {@link ChainBeanDefinitionRegistryPostProcessor}.
   */
  public ChainBeanDefinitionRegistryPostProcessor() {
    this.parameterizedTypesRetriever = new ParameterizedTypesRetriever();
    this.chainAssembler = new ChainAssembler();
  }

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
    this.beanDefinitionRegistry = beanDefinitionRegistry;
    Set<Class> arguments = new HashSet<>();
    for (final String beanDefinitionName : beanDefinitionRegistry.getBeanDefinitionNames()) {
//      System.out.println(beanDefinitionName);
      final var beanDefinition = beanDefinitionRegistry.getBeanDefinition(beanDefinitionName);
      if (beanDefinition.getBeanClassName() != null) {
        try {
//          System.out.println(beanDefinition.getBeanClassName());
          final var clazz = Class.forName(beanDefinition.getBeanClassName());
          if (ChainLink.class.isAssignableFrom(clazz)) {
            final var parameterizedTypeContext1 = parameterizedTypesRetriever.retrieveForClass(clazz)
                .stream()
                .filter(parameterizedTypeContext ->
                    ChainLink.class.equals(parameterizedTypeContext.getParameterizedType()
                        .getRawType()))
                .findFirst()
                .orElseThrow();

            final var argument = parameterizedTypeContext1.getArguments()
                .values()
                .iterator()
                .next();

            if (!arguments.contains(argument)) {
              System.out.println(clazz);
              beanDefinition.setPrimary(true);
              arguments.add(argument);
            }
          }
        } catch (ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    // TODO Refactor logic to postProcessBeanDefinitionRegistry
//    this.configurableListableBeanFactory = configurableListableBeanFactory;
//
//    final var chainLinkBeanContexts = retrieveChainLinkBeanContexts();
//
//    final var firstChainLinksBeanContext = assembleChains(chainLinkBeanContexts);
//
//    firstChainLinksBeanContext.forEach(this::updateBeanDefinitionOnRegistryAndFactory);
  }

  @SuppressWarnings("unchecked")
  private Set<ChainLinkBeanContext<X>> retrieveChainLinkBeanContexts() {
    return configurableListableBeanFactory.getBeansOfType(ChainLink.class)
        .entrySet()
        .stream()
        .map(entry -> {
          final var name = entry.getKey();
          final var beanDefinition = beanDefinitionRegistry.getBeanDefinition(name);
          final var bean = (X) entry.getValue();

          return ChainLinkBeanContext.<X>builder()
              .name(name)
              .bean(bean)
              .definition(beanDefinition)
              .build();

        })
        .collect(Collectors.toSet());
  }

  private Set<ChainLinkBeanContext<X>> assembleChains(Set<ChainLinkBeanContext<X>> chainLinkBeanContexts) {
    final var chainLinkBeanContextsMapByChainLinkArgumentType = mapChainLinkBeanContextsByChainLinkArgumentType(chainLinkBeanContexts);

    return chainLinkBeanContextsMapByChainLinkArgumentType.values()
        .stream()
        .map(chainAssembler::assemble)
        .collect(Collectors.toSet());
  }

  private Map<Type, List<ChainLinkBeanContext<X>>> mapChainLinkBeanContextsByChainLinkArgumentType(Set<ChainLinkBeanContext<X>> chainLinkBeanContexts) {
    return chainLinkBeanContexts.stream()
        .collect(Collectors.groupingBy(chainLinkBeanContext -> {
          final var parameterizedTypeContext = retrieveChainLinkInterfaceAsParameterizedTypeContext(chainLinkBeanContext.getBean());
          return parameterizedTypeContext.getArguments()
              .values()
              .iterator()
              .next();
        }));
  }

  private ParameterizedTypeContext retrieveChainLinkInterfaceAsParameterizedTypeContext(X bean) {
    return parameterizedTypesRetriever.retrieveForClass(bean.getClass())
        .stream()
        .filter(context -> {
          final var rawType = context.getParameterizedType()
              .getRawType();
          return ChainLink.class.equals(rawType);
        })
        .findFirst()
        .orElseThrow();
  }

  private void updateBeanDefinitionOnRegistryAndFactory(ChainLinkBeanContext<X> firstLinkChainLinkBeanContext) {
    final var name = firstLinkChainLinkBeanContext.getName();
    final var beanDefinition = firstLinkChainLinkBeanContext.getDefinition();
    final var bean = firstLinkChainLinkBeanContext.getBean();

    beanDefinitionRegistry.removeBeanDefinition(name);
    beanDefinition.setPrimary(true);
    beanDefinitionRegistry.registerBeanDefinition(name, beanDefinition);
    configurableListableBeanFactory.registerSingleton(name, bean);
  }
}
