package com.figtreelake.corbeanprocessor.autoconfigure;

import com.figtreelake.corbeanprocessor.autoconfigure.util.StreamUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
public class ChainBeanDefinitionRegistryPostProcessor<X extends ChainLink<X>> implements BeanDefinitionRegistryPostProcessor, BeanPostProcessor {

  private final ParameterizedTypesRetriever parameterizedTypesRetriever;

  private final ChainAssembler chainAssembler;

  private Set<ChainContext<X>> chainContexts;

  private ChainLinkBeanDefinitionContextComparator chainLinkBeanDefinitionContextComparator;

  /**
   * Instantiates an object of type {@link ChainBeanDefinitionRegistryPostProcessor}.
   */
  public ChainBeanDefinitionRegistryPostProcessor() {
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

    final var chainLinkChainLinkBeanDefinitionContextFactory = ChainLinkBeanDefinitionContextFactory.<X>builder()
        .beanDefinitionRegistry(beanDefinitionRegistry)
        .parameterizedTypesRetriever(parameterizedTypesRetriever)
        .build();

    return Arrays.stream(beanDefinitionRegistry.getBeanDefinitionNames())
        .map(chainLinkChainLinkBeanDefinitionContextFactory::create)
        .flatMap(StreamUtil::getOptionalIfPresent)
        .map(this::createChainLinkContextByArgumentMapEntry)
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (l1, l2) -> {
              l1.addAll(l2);
              return l1;
            },
            HashMap::new));
  }


  private Map.Entry<Class<?>, List<ChainLinkBeanDefinitionContext<X>>> createChainLinkContextByArgumentMapEntry(ChainLinkBeanDefinitionContext<X> context) {
    final var key = retrieveChainLinkArgumentClass(context.getChainLinkTypeContext());
    return Map.entry(key, List.of(context));
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
    // Unused.
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
}
