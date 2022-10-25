package com.figtreelake.corbeanprocessor.autoconfigure.link;

import com.figtreelake.corbeanprocessor.autoconfigure.chain.ChainAssembler;
import com.figtreelake.corbeanprocessor.autoconfigure.chain.ChainContext;
import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypeContext;
import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypesRetriever;
import com.figtreelake.corbeanprocessor.autoconfigure.util.BeanDefinitionClassRetriever;
import com.figtreelake.corbeanprocessor.autoconfigure.util.ClassRetriever;
import com.figtreelake.corbeanprocessor.autoconfigure.util.MethodRetriever;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

/**
 * A {@link BeanDefinitionRegistryPostProcessor} which maps all beans that
 * extends/implements {@link ChainLink} interface and splitting them according
 * to their contexts. It also implements {@link BeanDefinitionRegistryPostProcessor}
 * to retrieve all bean implementations and create each chain of responsibility
 * according to its context.
 *
 * @param <T> Element that either implements or extends the {@link ChainLink}
 *            interface.
 * @author MarceloLeite2604
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
public class ChainLinkBeanDefinitionRegistryPostProcessor<T extends ChainLink<T>> implements BeanDefinitionRegistryPostProcessor, BeanPostProcessor {

  private final ParameterizedTypesRetriever parameterizedTypesRetriever;

  private final ChainAssembler chainAssembler;

  private final ChainLinkBeanDefinitionContextComparator chainLinkBeanDefinitionContextComparator;

  private final BeanDefinitionClassRetriever beanDefinitionClassRetrieverTemplate;

  private final ChainLinkBeanDefinitionContextFactory<T> chainLinkBeanDefinitionContextFactoryTemplate;


  @Setter(AccessLevel.PACKAGE)
  private Set<ChainContext<T>> chainContexts;

  /**
   * Instantiates an object of type {@link ChainLinkBeanDefinitionRegistryPostProcessor}.
   */
  public ChainLinkBeanDefinitionRegistryPostProcessor() {
    parameterizedTypesRetriever = new ParameterizedTypesRetriever();
    chainAssembler = new ChainAssembler();
    chainLinkBeanDefinitionContextComparator = new ChainLinkBeanDefinitionContextComparator();

    beanDefinitionClassRetrieverTemplate = BeanDefinitionClassRetriever.builder()
        .classRetriever(new ClassRetriever())
        .methodRetriever(new MethodRetriever())
        .build();

    chainLinkBeanDefinitionContextFactoryTemplate = ChainLinkBeanDefinitionContextFactory.<T>builder()
        .parameterizedTypesRetriever(parameterizedTypesRetriever)
        .build();
  }

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
    final var chainLinkBeansDefinitionsContextsByArgumentType = createChainLinkBeansDefinitionsContextsByArgumentTypeMap(beanDefinitionRegistry);
    chainContexts = createChainContexts(chainLinkBeansDefinitionsContextsByArgumentType);
  }

  private Set<ChainContext<T>> createChainContexts(Map<Class<?>, List<ChainLinkBeanDefinitionContext<T>>> chainLinkBeansDefinitionsContextsByArgumentType) {
    return chainLinkBeansDefinitionsContextsByArgumentType.values()
        .stream()
        .map(this::sortChainLinkBeansDefinitionsContexts)
        .map(this::updateFirstLinkBeanDefinition)
        .map(this::createChainContext)
        .collect(Collectors.toSet());
  }

  private List<ChainLinkBeanDefinitionContext<T>> updateFirstLinkBeanDefinition(List<ChainLinkBeanDefinitionContext<T>> chainLinkBeanDefinitionContexts) {
    updateBeanDefinition(chainLinkBeanDefinitionContexts.get(0));
    return chainLinkBeanDefinitionContexts;
  }

  private ChainContext<T> createChainContext(List<ChainLinkBeanDefinitionContext<T>> contexts) {

    final var beanNames = contexts.stream()
        .map(ChainLinkBeanDefinitionContext::getName)
        .toList();

    return ChainContext.<T>builder()
        .beanDefinitionContexts(contexts)
        .beanNames(beanNames)
        .build();
  }

  private void updateBeanDefinition(ChainLinkBeanDefinitionContext<T> chainLinkBeanDefinitionContext) {
    chainLinkBeanDefinitionContext.getDefinition()
        .setPrimary(true);
  }

  private Map<Class<?>, List<ChainLinkBeanDefinitionContext<T>>> createChainLinkBeansDefinitionsContextsByArgumentTypeMap(BeanDefinitionRegistry beanDefinitionRegistry) {

    final var beanDefinitionClassRetriever = beanDefinitionClassRetrieverTemplate.toBuilder()
        .beanDefinitionRegistry(beanDefinitionRegistry)
        .build();

    final var chainLinkBeanDefinitionContextFactory = chainLinkBeanDefinitionContextFactoryTemplate.toBuilder()
        .beanDefinitionRegistry(beanDefinitionRegistry)
        .beanDefinitionClassRetriever(beanDefinitionClassRetriever)
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


  private Map.Entry<Class<?>, List<ChainLinkBeanDefinitionContext<T>>> createChainLinkContextByArgumentMapEntry(ChainLinkBeanDefinitionContext<T> context) {
    final var key = retrieveChainLinkArgumentClass(context.getChainLinkTypeContext());
    return Map.entry(key, new ArrayList<>(List.of(context)));
  }

  private List<ChainLinkBeanDefinitionContext<T>> sortChainLinkBeansDefinitionsContexts(List<ChainLinkBeanDefinitionContext<T>> chainLinkBeansDefinitionsContexts) {
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
  public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {/* Not used. */}

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    final var optionalChainContext = chainContexts.stream()
        .filter(chainContext ->
            chainContext.getBeanNames()
                .contains(beanName)
        )
        .findFirst();

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
  Set<ChainContext<T>> getChainContexts(Class<T> clazz) {
    return (Set<ChainContext<T>>) chainContexts;
  }
}
