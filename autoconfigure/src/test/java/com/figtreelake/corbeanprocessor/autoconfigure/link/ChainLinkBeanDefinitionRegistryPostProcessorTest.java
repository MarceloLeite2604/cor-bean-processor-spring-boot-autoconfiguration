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
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChainLinkBeanDefinitionRegistryPostProcessorTest<T extends ChainLink<T>> {

  @InjectMocks
  private ChainLinkBeanDefinitionRegistryPostProcessor<T> chainBeanDefinitionRegistryPostProcessor;

  @Mock
  private ParameterizedTypesRetriever parameterizedTypesRetriever;

  @Mock
  private ChainAssembler chainAssembler;

  @Mock
  private ChainLinkBeanDefinitionContextComparator chainLinkBeanDefinitionContextComparator;

  @SuppressWarnings("unchecked")
  @Test
  void shouldCreateChainContexts() {

    final var rootClass = (Class<T>) DummyAbstractChainLink.class;

    final var chainLinkArgumentClass = Float.class;

    final Set<ChainContext<T>> expectedChainContexts = createExpectedChainContexts(rootClass, chainLinkArgumentClass);

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
        Set.of(ParameterizedTypeContextFixture.createForChainLink(chainLinkArgumentClass)));

    chainBeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry(mockedBeanDefinitionRegistry);

    assertThat(chainBeanDefinitionRegistryPostProcessor.getChainContexts(rootClass))
        .usingRecursiveFieldByFieldElementComparator()
        .containsExactlyElementsOf(expectedChainContexts);
  }

  private Set<ChainContext<T>> createExpectedChainContexts(Class<T> rootClass, Class<Float> chainLinkArgumentClass) {
    
    final var beanDefinitionContexts = BeanFixture.createBeansMap(rootClass)
        .entrySet()
        .stream()
        .map(entry -> ChainLinkBeanContextFixture.create(entry, ParameterizedTypeContextFixture.createForChainLink(chainLinkArgumentClass)))
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

//
//  @SuppressWarnings({"rawtypes", "unchecked"})
//  @Test
//  void shouldUpdateFirstChainLinkBeanDefinitionOnRegistryAndFactory() {
//
//    final var mockedBeanDefinitionRegistry = mock(BeanDefinitionRegistry.class);
//    final var mockedConfigurableListableBeanFactory = mock(ConfigurableListableBeanFactory.class);
//
//    final var dummyChainLinkABeanName = DummyChainLinkA.class.getSimpleName();
//    final var dummyChainLinkBBeanName = DummyChainLinkB.class.getSimpleName();
//    final var dummyChainLinkCBeanName = DummyChainLinkC.class.getSimpleName();
//    final var dummyChainLinkDBeanName = DummyChainLinkD.class.getSimpleName();
//
//    final var dummyChainLinkA = new DummyChainLinkA();
//    final var dummyChainLinkB = new DummyChainLinkB();
//    final var dummyChainLinkC = new DummyChainLinkC();
//    final var dummyChainLinkD = new DummyChainLinkD();
//
//    final var dummyChainLinkABeanDefinition = BeanDefinitionFixture.create();
//    final var dummyChainLinkBBeanDefinition = BeanDefinitionFixture.create();
//    final var dummyChainLinkCBeanDefinition = BeanDefinitionFixture.create();
//    final var dummyChainLinkDBeanDefinition = BeanDefinitionFixture.create();
//
//    final var chainLinks = Set.of(dummyChainLinkA, dummyChainLinkB, dummyChainLinkC, dummyChainLinkD);
//
//    Map<String, ChainLink> chainLinksByName = Map.ofEntries(
//        Map.entry(dummyChainLinkABeanName, dummyChainLinkA),
//        Map.entry(dummyChainLinkBBeanName, dummyChainLinkB),
//        Map.entry(dummyChainLinkCBeanName, dummyChainLinkC),
//        Map.entry(dummyChainLinkDBeanName, dummyChainLinkD)
//    );
//
//    final var parameterizedTypeContext = ParameterizedTypeContextFixture.createForChainLink(DummyAbstractChainLink.class);
//
//    when(mockedConfigurableListableBeanFactory.getBeansOfType(ChainLink.class)).thenReturn(chainLinksByName);
//
//    when(mockedBeanDefinitionRegistry.getBeanDefinition(dummyChainLinkABeanName))
//        .thenReturn(dummyChainLinkABeanDefinition);
//
//    when(mockedBeanDefinitionRegistry.getBeanDefinition(dummyChainLinkBBeanName))
//        .thenReturn(dummyChainLinkBBeanDefinition);
//
//    when(mockedBeanDefinitionRegistry.getBeanDefinition(dummyChainLinkCBeanName))
//        .thenReturn(dummyChainLinkCBeanDefinition);
//
//    when(mockedBeanDefinitionRegistry.getBeanDefinition(dummyChainLinkDBeanName))
//        .thenReturn(dummyChainLinkDBeanDefinition);
//
//    when(parameterizedTypesRetriever.retrieveForClass(any()))
//        .thenReturn(Set.of(parameterizedTypeContext));
//
//
//    when(chainAssembler.assemble(any())).thenAnswer(invocationOnMock -> {
//      final var firstArgument = (Iterable<ChainLinkBeanDefinitionContext<DummyAbstractChainLink>>) invocationOnMock.getArgument(0);
//      return firstArgument.iterator()
//          .next();
//    });
//
//    chainBeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry(mockedBeanDefinitionRegistry);
//    chainBeanDefinitionRegistryPostProcessor.postProcessBeanFactory(mockedConfigurableListableBeanFactory);
//
//    final var removeBeanDefinitionBeanNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
//    verify(mockedBeanDefinitionRegistry, times(1)).removeBeanDefinition(removeBeanDefinitionBeanNameArgumentCaptor.capture());
//
//    final var registerBeanDefinitionBeanNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
//    final var beanDefinitionArgumentCaptor = ArgumentCaptor.forClass(BeanDefinition.class);
//
//    verify(mockedBeanDefinitionRegistry, times(1))
//        .registerBeanDefinition(
//            registerBeanDefinitionBeanNameArgumentCaptor.capture(),
//            beanDefinitionArgumentCaptor.capture());
//
//    final var recordedBeanDefinition = beanDefinitionArgumentCaptor.getValue();
//
//    assertThat(registerBeanDefinitionBeanNameArgumentCaptor.getValue())
//        .isEqualTo(removeBeanDefinitionBeanNameArgumentCaptor.getValue());
//
//    assertThat(recordedBeanDefinition.isPrimary()).isTrue();
//
//    final var registerSingletonBeanNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
//    final var beanArgumentCaptor = ArgumentCaptor.forClass(Object.class);
//    verify(mockedConfigurableListableBeanFactory, times(1)).registerSingleton(
//        registerSingletonBeanNameArgumentCaptor.capture(),
//        beanArgumentCaptor.capture()
//    );
//
//    assertThat(registerSingletonBeanNameArgumentCaptor.getValue())
//        .isEqualTo(removeBeanDefinitionBeanNameArgumentCaptor.getValue());
//
//    assertThat(beanArgumentCaptor.getValue()).isIn(chainLinks);
//  }

}