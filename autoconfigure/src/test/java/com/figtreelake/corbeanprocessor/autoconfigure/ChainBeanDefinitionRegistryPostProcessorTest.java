package com.figtreelake.corbeanprocessor.autoconfigure;

import com.figtreelake.corbeanprocessor.autoconfigure.util.ClassUtil;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.*;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.BeanDefinitionFixture;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.ParameterizedTypeContextFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChainBeanDefinitionRegistryPostProcessorTest {

  @InjectMocks
  private ChainBeanDefinitionRegistryPostProcessor<?> chainBeanDefinitionRegistryPostProcessor;

  @Mock
  private ClassUtil classUtil;

  @Mock
  private ChainAssembler chainAssembler;

  @Test
  void shouldUpdateBeanDefinitionRegistryField() {

    final var mockedBeanDefinitionRegistry = mock(BeanDefinitionRegistry.class);

    chainBeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry(mockedBeanDefinitionRegistry);

    assertThat(chainBeanDefinitionRegistryPostProcessor.getBeanDefinitionRegistry())
        .isEqualTo(mockedBeanDefinitionRegistry);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Test
  void shouldUpdateFirstChainLinkBeanDefinitionOnRegistryAndFactory() {

    final var mockedBeanDefinitionRegistry = mock(BeanDefinitionRegistry.class);
    final var mockedConfigurableListableBeanFactory = mock(ConfigurableListableBeanFactory.class);

    final var dummyChainLinkABeanName = DummyChainLinkA.class.getSimpleName();
    final var dummyChainLinkBBeanName = DummyChainLinkB.class.getSimpleName();
    final var dummyChainLinkCBeanName = DummyChainLinkC.class.getSimpleName();
    final var dummyChainLinkDBeanName = DummyChainLinkD.class.getSimpleName();

    final var dummyChainLinkA = new DummyChainLinkA();
    final var dummyChainLinkB = new DummyChainLinkB();
    final var dummyChainLinkC = new DummyChainLinkC();
    final var dummyChainLinkD = new DummyChainLinkD();

    final var dummyChainLinkABeanDefinition = BeanDefinitionFixture.create();
    final var dummyChainLinkBBeanDefinition = BeanDefinitionFixture.create();
    final var dummyChainLinkCBeanDefinition = BeanDefinitionFixture.create();
    final var dummyChainLinkDBeanDefinition = BeanDefinitionFixture.create();

    final var chainLinks = Set.of(dummyChainLinkA, dummyChainLinkB, dummyChainLinkC, dummyChainLinkD);

    Map<String, ChainLink> chainLinksByName = Map.ofEntries(
        Map.entry(dummyChainLinkABeanName, dummyChainLinkA),
        Map.entry(dummyChainLinkBBeanName, dummyChainLinkB),
        Map.entry(dummyChainLinkCBeanName, dummyChainLinkC),
        Map.entry(dummyChainLinkDBeanName, dummyChainLinkD)
    );

    final var parameterizedTypeContext = ParameterizedTypeContextFixture.createForChainLink(DummyAbstractChainLink.class);

    when(mockedConfigurableListableBeanFactory.getBeansOfType(ChainLink.class)).thenReturn(chainLinksByName);

    when(mockedBeanDefinitionRegistry.getBeanDefinition(dummyChainLinkABeanName))
        .thenReturn(dummyChainLinkABeanDefinition);

    when(mockedBeanDefinitionRegistry.getBeanDefinition(dummyChainLinkBBeanName))
        .thenReturn(dummyChainLinkBBeanDefinition);

    when(mockedBeanDefinitionRegistry.getBeanDefinition(dummyChainLinkCBeanName))
        .thenReturn(dummyChainLinkCBeanDefinition);

    when(mockedBeanDefinitionRegistry.getBeanDefinition(dummyChainLinkDBeanName))
        .thenReturn(dummyChainLinkDBeanDefinition);

    when(classUtil.retrieveGenericInterfacesForClass(any()))
        .thenReturn(Set.of(parameterizedTypeContext));


    when(chainAssembler.assemble(any())).thenAnswer(invocationOnMock -> {
      final var firstArgument = (Iterable<ChainLinkBeanContext<DummyAbstractChainLink>>) invocationOnMock.getArgument(0);
      return firstArgument.iterator()
          .next();
    });

    chainBeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry(mockedBeanDefinitionRegistry);
    chainBeanDefinitionRegistryPostProcessor.postProcessBeanFactory(mockedConfigurableListableBeanFactory);

    final var removeBeanDefinitionBeanNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
    verify(mockedBeanDefinitionRegistry, times(1)).removeBeanDefinition(removeBeanDefinitionBeanNameArgumentCaptor.capture());

    final var registerBeanDefinitionBeanNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
    final var beanDefinitionArgumentCaptor = ArgumentCaptor.forClass(BeanDefinition.class);

    verify(mockedBeanDefinitionRegistry, times(1))
        .registerBeanDefinition(
            registerBeanDefinitionBeanNameArgumentCaptor.capture(),
            beanDefinitionArgumentCaptor.capture());

    final var recordedBeanDefinition = beanDefinitionArgumentCaptor.getValue();

    assertThat(registerBeanDefinitionBeanNameArgumentCaptor.getValue())
        .isEqualTo(removeBeanDefinitionBeanNameArgumentCaptor.getValue());

    assertThat(recordedBeanDefinition.isPrimary()).isTrue();

    final var registerSingletonBeanNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
    final var beanArgumentCaptor = ArgumentCaptor.forClass(Object.class);
    verify(mockedConfigurableListableBeanFactory, times(1)).registerSingleton(
        registerSingletonBeanNameArgumentCaptor.capture(),
        beanArgumentCaptor.capture()
    );

    assertThat(registerSingletonBeanNameArgumentCaptor.getValue())
        .isEqualTo(removeBeanDefinitionBeanNameArgumentCaptor.getValue());

    assertThat(beanArgumentCaptor.getValue()).isIn(chainLinks);
  }

}