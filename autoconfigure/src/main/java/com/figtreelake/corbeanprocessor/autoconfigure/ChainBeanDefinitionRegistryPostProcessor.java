package com.figtreelake.corbeanprocessor.autoconfigure;

import com.figtreelake.corbeanprocessor.autoconfigure.util.ClassUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ChainBeanDefinitionRegistryPostProcessor<X extends ChainLink<X>> implements BeanDefinitionRegistryPostProcessor {

  private final ClassUtil classUtil;

  private final ChainAssembler chainAssembler;

  @Getter(AccessLevel.PACKAGE)
  private BeanDefinitionRegistry beanDefinitionRegistry;

  private ConfigurableListableBeanFactory configurableListableBeanFactory;


  public ChainBeanDefinitionRegistryPostProcessor() {
    this.classUtil = new ClassUtil();
    this.chainAssembler = new ChainAssembler();
  }

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
    this.beanDefinitionRegistry = beanDefinitionRegistry;
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    this.configurableListableBeanFactory = configurableListableBeanFactory;

    final var chainLinkBeanContexts = retrieveChainLinkBeanContexts();

    final var firstChainLinksBeanContext = assembleChains(chainLinkBeanContexts);

    firstChainLinksBeanContext.forEach(this::updateBeanDefinitionOnRegistryAndFactory);
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
    return classUtil.retrieveGenericInterfacesForClass(bean.getClass())
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
