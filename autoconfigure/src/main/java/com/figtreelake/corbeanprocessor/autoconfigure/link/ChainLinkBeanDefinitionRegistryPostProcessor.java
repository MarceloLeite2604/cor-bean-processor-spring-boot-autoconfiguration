package com.figtreelake.corbeanprocessor.autoconfigure.link;

import com.figtreelake.corbeanprocessor.autoconfigure.chain.ChainAssembler;
import com.figtreelake.corbeanprocessor.autoconfigure.chain.ChainContext;
import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypeContext;
import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypesRetriever;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
public class ChainLinkBeanDefinitionRegistryPostProcessor<X extends ChainLink<X>> implements BeanDefinitionRegistryPostProcessor, BeanPostProcessor {

  private final ParameterizedTypesRetriever parameterizedTypesRetriever;

  private final ChainAssembler chainAssembler;

  private final ChainLinkBeanDefinitionContextComparator chainLinkBeanDefinitionContextComparator;

  private Set<ChainContext<X>> chainContexts;

  /**
   * Instantiates an object of type {@link ChainLinkBeanDefinitionRegistryPostProcessor}.
   */
  public ChainLinkBeanDefinitionRegistryPostProcessor() {
    parameterizedTypesRetriever = new ParameterizedTypesRetriever();
    chainAssembler = new ChainAssembler();
    chainLinkBeanDefinitionContextComparator = new ChainLinkBeanDefinitionContextComparator();
  }

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
    final var chainLinkBeansDefinitionsContextsByArgumentType = createChainLinkBeansDefinitionsContextsByArgumentTypeMap(beanDefinitionRegistry);
    chainContexts = createChainContexts(chainLinkBeansDefinitionsContextsByArgumentType);
  }

  private Set<ChainContext<X>> createChainContexts(Map<Class<?>, List<ChainLinkBeanDefinitionContext<X>>> chainLinkBeansDefinitionsContextsByArgumentType) {
    return chainLinkBeansDefinitionsContextsByArgumentType.values()
        .stream()
        .map(this::sortChainLinkBeansDefinitionsContexts)
        .map(this::updateFirstLinkBeanDefinition)
        .map(this::createChainContext)
        .collect(Collectors.toSet());
  }

  private List<ChainLinkBeanDefinitionContext<X>> updateFirstLinkBeanDefinition(List<ChainLinkBeanDefinitionContext<X>> chainLinkBeanDefinitionContexts) {
    updateBeanDefinition(chainLinkBeanDefinitionContexts.get(0));
    return chainLinkBeanDefinitionContexts;
  }

  private ChainContext<X> createChainContext(List<ChainLinkBeanDefinitionContext<X>> contexts) {

    final var beanNames = contexts.stream()
        .map(ChainLinkBeanDefinitionContext::getName)
        .toList();

    return ChainContext.<X>builder()
        .beanDefinitionContexts(contexts)
        .beanNames(beanNames)
        .build();
  }

  private void updateBeanDefinition(ChainLinkBeanDefinitionContext<X> chainLinkBeanDefinitionContext) {
    chainLinkBeanDefinitionContext.getDefinition()
        .setPrimary(true);
  }

  private Map<Class<?>, List<ChainLinkBeanDefinitionContext<X>>> createChainLinkBeansDefinitionsContextsByArgumentTypeMap(BeanDefinitionRegistry beanDefinitionRegistry) {

    final var chainLinkBeanDefinitionContextFactory = ChainLinkBeanDefinitionContextFactory.<X>builder()
        .beanDefinitionRegistry(beanDefinitionRegistry)
        .parameterizedTypesRetriever(parameterizedTypesRetriever)
        .build();

    return Arrays.stream(beanDefinitionRegistry.getBeanDefinitionNames())
        .map(chainLinkBeanDefinitionContextFactory::create)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(this::createChainLinkContextByArgumentMapEntry)
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (l1, l2) -> {
              final var result = new ArrayList<>(l1);
              result.addAll(l2);
              return result;
            },
            HashMap::new));
  }


  private Map.Entry<Class<?>, List<ChainLinkBeanDefinitionContext<X>>> createChainLinkContextByArgumentMapEntry(ChainLinkBeanDefinitionContext<X> context) {
    final var key = retrieveChainLinkArgumentClass(context.getChainLinkTypeContext());
    return Map.entry(key, new ArrayList<>(List.of(context)));
  }

  private List<ChainLinkBeanDefinitionContext<X>> sortChainLinkBeansDefinitionsContexts(List<ChainLinkBeanDefinitionContext<X>> chainLinkBeansDefinitionsContexts) {
    chainLinkBeansDefinitionsContexts.sort(chainLinkBeanDefinitionContextComparator);
    return chainLinkBeansDefinitionsContexts;
  }


  private Class<?> retrieveChainLinkArgumentClass(ParameterizedTypeContext parameterizedTypeContext) {
    return parameterizedTypeContext.getArguments()
        .values()
        .iterator()
        .next();
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    /* Not used. */
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    final var optionalChainContext = chainContexts.stream()
        .filter(chainContext ->
            chainContext.getBeanNames()
                .contains(beanName)
        ).findFirst();

    if (optionalChainContext.isEmpty()) {
      return bean;
    }

    final var chainContext = optionalChainContext.get();

    chainContext.addChainLinkBean(beanName, bean);

    if (chainContext.hasAllChainLinks()) {
      chainAssembler.assemble(chainContext);
    }
    return bean;
  }

  @SuppressWarnings({"unused", "java:S1905", "RedundantCast"})
  Set<ChainContext<X>> getChainContexts(Class<X> clazz) {
    return (Set<ChainContext<X>>) chainContexts;
  }
}
