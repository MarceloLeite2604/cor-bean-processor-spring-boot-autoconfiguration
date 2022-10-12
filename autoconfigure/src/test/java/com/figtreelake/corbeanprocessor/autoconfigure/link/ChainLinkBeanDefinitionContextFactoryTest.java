package com.figtreelake.corbeanprocessor.autoconfigure.link;

import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypesRetriever;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyAbstractChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.BeanDefinitionFixture;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.BeanFixture;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.ChainLinkBeanDefinitionContextFixture;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture.ParameterizedTypeContextFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChainLinkBeanDefinitionContextFactoryTest {

  @InjectMocks
  private ChainLinkBeanDefinitionContextFactory<?> chainLinkBeanDefinitionContextFactory;

  @Mock
  private BeanDefinitionRegistry beanDefinitionRegistry;

  @Mock
  private ParameterizedTypesRetriever parameterizedTypesRetriever;

  @Test
  void shouldCreateChainLinkBeanDefinitionContext() {

    final var beanClass = DummyAbstractChainLink.class;

    final var expectedChainLinkBeanDefinitionContext = ChainLinkBeanDefinitionContextFixture.create(beanClass, BeanFixture.FIRST_BEAN_NAME);

    when(beanDefinitionRegistry.getBeanDefinition(any())).thenReturn(BeanDefinitionFixture.create(beanClass));


    when(parameterizedTypesRetriever.retrieveForClass(beanClass)).thenReturn(Set.of(ParameterizedTypeContextFixture.createForChainLink(beanClass)));

    final var optionalChainLinkBeanDefinitionContext = chainLinkBeanDefinitionContextFactory.create(BeanFixture.FIRST_BEAN_NAME);

    assertThat(optionalChainLinkBeanDefinitionContext).isPresent();

    assertThat(optionalChainLinkBeanDefinitionContext.get()).usingRecursiveComparison()
        .isEqualTo(expectedChainLinkBeanDefinitionContext);
  }

  @Test
  void shouldReturnOptionalEmptyWhenBeanDefinitionDoesNotHaveBeanClassNameDefined() {

    when(beanDefinitionRegistry.getBeanDefinition(any())).thenReturn(BeanDefinitionFixture.create((String)null));

    final var optionalChainLinkBeanDefinitionContext = chainLinkBeanDefinitionContextFactory.create(BeanFixture.FIRST_BEAN_NAME);

    assertThat(optionalChainLinkBeanDefinitionContext).isEmpty();
  }

  @Test
  void shouldReturnOptionalEmptyWhenBeanIsNotInstanceOfChainLink() {

    when(beanDefinitionRegistry.getBeanDefinition(any())).thenReturn(BeanDefinitionFixture.create(String.class));

    final var optionalChainLinkBeanDefinitionContext = chainLinkBeanDefinitionContextFactory.create(BeanFixture.FIRST_BEAN_NAME);

    assertThat(optionalChainLinkBeanDefinitionContext).isEmpty();
  }

  @Test
  void shouldReturnOptionalEmptyWhenBeanClassIsNotRecognized() {

    when(beanDefinitionRegistry.getBeanDefinition(any())).thenReturn(BeanDefinitionFixture.create("unknownBeanClassName"));

    final var optionalChainLinkBeanDefinitionContext = chainLinkBeanDefinitionContextFactory.create(BeanFixture.FIRST_BEAN_NAME);

    assertThat(optionalChainLinkBeanDefinitionContext).isEmpty();
  }

  @Test
  void shouldReturnOptionalEmptyParameterizedTypeChainLinkIsNotFoundOnBeanClass() {

    final var beanClass = DummyAbstractChainLink.class;

    final var expectedChainLinkBeanDefinitionContext = ChainLinkBeanDefinitionContextFixture.create(beanClass, BeanFixture.FIRST_BEAN_NAME);

    when(beanDefinitionRegistry.getBeanDefinition(any())).thenReturn(BeanDefinitionFixture.create(beanClass));


    when(parameterizedTypesRetriever.retrieveForClass(beanClass)).thenReturn(Collections.emptySet());

    final var optionalChainLinkBeanDefinitionContext = chainLinkBeanDefinitionContextFactory.create(BeanFixture.FIRST_BEAN_NAME);

    assertThat(optionalChainLinkBeanDefinitionContext).isEmpty();
  }
}