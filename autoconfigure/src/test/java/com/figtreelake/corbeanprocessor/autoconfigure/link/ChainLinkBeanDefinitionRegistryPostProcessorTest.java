package com.figtreelake.corbeanprocessor.autoconfigure.link;

import com.figtreelake.corbeanprocessor.autoconfigure.chain.ChainAssembler;
import com.figtreelake.corbeanprocessor.autoconfigure.chain.ChainContext;
import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypesRetriever;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyAbstractChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.BeanDefinitionFixture;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.BeanFixture;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.ChainContextFixture;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.ChainLinkBeanContextFixture;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.ParameterizedTypeContextFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChainLinkBeanDefinitionRegistryPostProcessorTest<T extends ChainLink<T>> {


  public static final Class<Float> CHAIN_LINK_ARGUMENT_CLASS = Float.class;

  @InjectMocks
  private ChainLinkBeanDefinitionRegistryPostProcessor<T> chainBeanDefinitionRegistryPostProcessor;

  @Mock
  private ParameterizedTypesRetriever parameterizedTypesRetriever;

  @Mock
  private ChainAssembler chainAssembler;

  @Mock
  private ChainLinkBeanDefinitionContextComparator chainLinkBeanDefinitionContextComparator;

  @SuppressWarnings("unchecked")
  public final Class<T> rootClass = (Class<T>) DummyAbstractChainLink.class;

  @Test
  void shouldCreateChainContexts() {

    final Set<ChainContext<T>> expectedChainContexts = createExpectedChainContexts();

    final var mockedBeanDefinitionRegistry = mock(BeanDefinitionRegistry.class);

    when(mockedBeanDefinitionRegistry.getBeanDefinitionNames()).thenReturn(BeanFixture.BEAN_NAMES_ARRAY);

    when(mockedBeanDefinitionRegistry.getBeanDefinition(BeanFixture.FIRST_BEAN_NAME))
        .thenReturn(BeanDefinitionFixture.create(BeanFixture.FIRST_BEAN_CLASS));

    when(mockedBeanDefinitionRegistry.getBeanDefinition(BeanFixture.SECOND_BEAN_NAME))
        .thenReturn(BeanDefinitionFixture.create(BeanFixture.SECOND_BEAN_CLASS));

    when(mockedBeanDefinitionRegistry.getBeanDefinition(BeanFixture.THIRD_BEAN_NAME))
        .thenReturn(BeanDefinitionFixture.create(BeanFixture.THIRD_BEAN_CLASS));

    when(mockedBeanDefinitionRegistry.getBeanDefinition(BeanFixture.FOURTH_BEAN_NAME))
        .thenReturn(BeanDefinitionFixture.create(BeanFixture.FOURTH_BEAN_CLASS));

    when(parameterizedTypesRetriever.retrieveForClass(any())).thenReturn(
        Set.of(ParameterizedTypeContextFixture.createForChainLink(CHAIN_LINK_ARGUMENT_CLASS)));

    chainBeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry(mockedBeanDefinitionRegistry);

    assertThat(chainBeanDefinitionRegistryPostProcessor.getChainContexts(rootClass))
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyElementsOf(expectedChainContexts);
  }

  private Set<ChainContext<T>> createExpectedChainContexts() {

    final var beanDefinitionContexts = BeanFixture.createBeansMap(rootClass)
        .entrySet()
        .stream()
        .map(entry -> ChainLinkBeanContextFixture.create(entry, ParameterizedTypeContextFixture.createForChainLink(CHAIN_LINK_ARGUMENT_CLASS)))
        .toList();

    beanDefinitionContexts.get(0)
        .getDefinition()
        .setPrimary(true);

    final var expectedChainContext = ChainContextFixture.create(rootClass)
        .toBuilder()
        .chainLinkBeansByName(null)
        .beanDefinitionContexts(beanDefinitionContexts)
        .build();

    return Set.of(expectedChainContext);
  }

  @SuppressWarnings("unchecked")
  @Test
  void shouldReturnBeanAndNotInvokeChainAssembleWhenBeanIsNotInChainContext() {

    final var mockedChainContexts = mock(Set.class);
    final var mockedChainContext = mock(ChainContext.class);
    final var beanName = "beanNameValue";
    final var bean = "thisIsABean";

    when(mockedChainContexts.stream()).thenReturn(Stream.of(mockedChainContext));

    when(mockedChainContext.getBeanNames()).thenReturn(BeanFixture.BEAN_NAMES);

    chainBeanDefinitionRegistryPostProcessor.setChainContexts(mockedChainContexts);

    final var actualBean = chainBeanDefinitionRegistryPostProcessor.postProcessAfterInitialization(bean, beanName);

    assertThat(actualBean).isEqualTo(bean);

    verify(mockedChainContext, never()).hasAllChainLinks();
    verify(chainAssembler, never()).assemble(any(ChainContext.class));
  }

  @SuppressWarnings("unchecked")
  @Test
  void shouldReturnChainLinkBeanAndAddItToChainContextAndNotInvokeChainAssembleWhenBeanIsChainLinkButChainIsNotComplete() {

    final Class<T> rootClass = (Class<T>) DummyAbstractChainLink.class;
    final var mockedChainContexts = mock(Set.class);
    final var mockedChainContext = mock(ChainContext.class);
    final var bean = BeanFixture.createFirstBean(rootClass);
    final var beanName = BeanFixture.FIRST_BEAN_NAME;

    when(mockedChainContexts.stream()).thenReturn(Stream.of(mockedChainContext));
    when(mockedChainContext.hasAllChainLinks()).thenReturn(false);

    when(mockedChainContext.getBeanNames()).thenReturn(BeanFixture.BEAN_NAMES);

    chainBeanDefinitionRegistryPostProcessor.setChainContexts(mockedChainContexts);

    final var actualBean = chainBeanDefinitionRegistryPostProcessor.postProcessAfterInitialization(bean, beanName);

    assertThat(actualBean).isEqualTo(bean);

    verify(mockedChainContext, times(1)).addChainLinkBean(beanName, bean);
    verify(chainAssembler, never()).assemble(any(ChainContext.class));
  }

  @SuppressWarnings("unchecked")
  @Test
  void shouldReturnChainLinkBeanAndAddItToChainContextAndInvokeChainAssembleWhenBeanIsChainLinkAndChainIsComplete() {

    final Class<T> rootClass = (Class<T>) DummyAbstractChainLink.class;
    final var mockedChainContexts = mock(Set.class);
    final var mockedChainContext = mock(ChainContext.class);
    final var bean = BeanFixture.createFourthBean(rootClass);
    final var beanName = BeanFixture.FOURTH_BEAN_NAME;

    when(mockedChainContexts.stream()).thenReturn(Stream.of(mockedChainContext));
    when(mockedChainContext.hasAllChainLinks()).thenReturn(true);

    when(mockedChainContext.getBeanNames()).thenReturn(BeanFixture.BEAN_NAMES);

    chainBeanDefinitionRegistryPostProcessor.setChainContexts(mockedChainContexts);

    final var actualBean = chainBeanDefinitionRegistryPostProcessor.postProcessAfterInitialization(bean, beanName);

    assertThat(actualBean).isEqualTo(bean);

    verify(mockedChainContext, times(1)).addChainLinkBean(beanName, bean);
    verify(chainAssembler, times(1)).assemble(mockedChainContext);
  }

  @Test
  void shouldInstantiateBeanDefinitionRegistryPostProcessorAndBeanPostProcessor() {
    final var chainLinkBeanDefinitionRegistryPostProcessor = new ChainLinkBeanDefinitionRegistryPostProcessor<>();

    assertThat(chainLinkBeanDefinitionRegistryPostProcessor).isInstanceOf(BeanDefinitionRegistryPostProcessor.class)
        .isInstanceOf(BeanPostProcessor.class);

  }
}