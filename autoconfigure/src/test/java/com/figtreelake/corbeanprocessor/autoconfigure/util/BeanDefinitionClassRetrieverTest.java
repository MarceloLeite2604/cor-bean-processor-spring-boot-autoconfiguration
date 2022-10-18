package com.figtreelake.corbeanprocessor.autoconfigure.util;

import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyAbstractChainLink;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeanDefinitionClassRetrieverTest {

  @InjectMocks
  private BeanDefinitionClassRetriever beanDefinitionClassRetriever;

  @Mock
  private BeanDefinitionRegistry beanDefinitionRegistry;

  @Mock
  private ClassRetriever classRetriever;

  @Mock
  private MethodRetriever methodRetriever;

  @Test
  void shouldReturnClassWhenBeanDefinitionContainsClassName() {
    final var expectedClass = DummyAbstractChainLink.class;
    final var className = "classNameValue";

    final var mockedBeanDefinition = mock(BeanDefinition.class);


    when(mockedBeanDefinition.getBeanClassName()).thenReturn(className);

    when(classRetriever.retrieve(className)).thenReturn(Optional.of(expectedClass));

    final var optionalClass = beanDefinitionClassRetriever.retrieve(mockedBeanDefinition);

    assertThat(optionalClass).isPresent()
        .contains(expectedClass);
  }

  @Test
  void shouldReturnClassWhenBeanDefinitionDoesNotContainClassNameButContainsFactory() {
    final var expectedClass = DummyAbstractChainLink.class;
    final var className = expectedClass.getName();
    final var factoryMethodName = "factoryMethodNameValue";
    final var factoryBeanName = "factoryBeanNameValue";
    final var mockedBeanDefinition = mock(BeanDefinition.class);

    final var mockedFactoryBeanDefinition = mock(BeanDefinition.class);
    final var factoryBeanClassName = "factoryBeanClassName";
    final var factoryBeanClass = Long.class;
    final var mockedMethod = mock(Method.class);
    final var mockedType = mock(Type.class);

    when(mockedBeanDefinition.getBeanClassName()).thenReturn(null);

    when(mockedBeanDefinition.getFactoryMethodName())
        .thenReturn(factoryMethodName);

    when(mockedBeanDefinition.getFactoryBeanName())
        .thenReturn(factoryBeanName);

    when(beanDefinitionRegistry.getBeanDefinition(factoryBeanName))
        .thenReturn(mockedFactoryBeanDefinition);

    when(mockedFactoryBeanDefinition.getBeanClassName())
        .thenReturn(factoryBeanClassName);

    when(classRetriever.retrieve(factoryBeanClassName))
        .thenReturn(Optional.of(factoryBeanClass));

    when(methodRetriever.retrieve(factoryBeanClass, factoryMethodName))
        .thenReturn(Optional.of(mockedMethod));

    when(mockedMethod.getGenericReturnType()).thenReturn(mockedType);

    when(mockedType.getTypeName()).thenReturn(className);

    when(classRetriever.retrieve(className))
        .thenReturn(Optional.of(expectedClass));

    final var optionalClass = beanDefinitionClassRetriever.retrieve(mockedBeanDefinition);

    assertThat(optionalClass).isPresent()
        .contains(expectedClass);
  }

  @Test
  void shouldReturnOptionalEmptyWhenBeanDefinitionDoesNotContainClassAndFactoryMethod() {
    final var mockedBeanDefinition = mock(BeanDefinition.class);

    when(mockedBeanDefinition.getBeanClassName()).thenReturn(null);

    when(mockedBeanDefinition.getFactoryMethodName())
        .thenReturn(null);

    final var optionalClass = beanDefinitionClassRetriever.retrieve(mockedBeanDefinition);

    assertThat(optionalClass).isEmpty();
  }

  @Test
  void shouldReturnOptionalEmptyWhenBeanDefinitionDoesNotContainClassNameAndFactoryBeanName() {
    final var factoryMethodName = "factoryMethodNameValue";
    final var mockedBeanDefinition = mock(BeanDefinition.class);

    when(mockedBeanDefinition.getBeanClassName()).thenReturn(null);

    when(mockedBeanDefinition.getFactoryMethodName())
        .thenReturn(factoryMethodName);

    when(mockedBeanDefinition.getFactoryBeanName())
        .thenReturn(null);

    final var optionalClass = beanDefinitionClassRetriever.retrieve(mockedBeanDefinition);

    assertThat(optionalClass).isEmpty();
  }

  @Test
  void shouldReturnOptionalEmptyWhenBeanDefinitionDoesNotContainClassNameAndFactoryBeanClassIsNotRecognized() {
    final var factoryMethodName = "factoryMethodNameValue";
    final var factoryBeanName = "factoryBeanNameValue";
    final var mockedBeanDefinition = mock(BeanDefinition.class);

    final var mockedFactoryBeanDefinition = mock(BeanDefinition.class);
    final var factoryBeanClassName = "factoryBeanClassName";

    when(mockedBeanDefinition.getBeanClassName()).thenReturn(null);

    when(mockedBeanDefinition.getFactoryMethodName())
        .thenReturn(factoryMethodName);

    when(mockedBeanDefinition.getFactoryBeanName())
        .thenReturn(factoryBeanName);

    when(beanDefinitionRegistry.getBeanDefinition(factoryBeanName))
        .thenReturn(mockedFactoryBeanDefinition);

    when(mockedFactoryBeanDefinition.getBeanClassName())
        .thenReturn(factoryBeanClassName);

    when(classRetriever.retrieve(factoryBeanClassName))
        .thenReturn(Optional.empty());

    final var optionalClass = beanDefinitionClassRetriever.retrieve(mockedBeanDefinition);

    assertThat(optionalClass).isEmpty();
  }

  @Test
  void shouldReturnOptionalEmptyWhenBeanDefinitionDoesNotContainClassAndCannotFindFactoryMethodOnBeanFactoryClass() {
    final var factoryMethodName = "factoryMethodNameValue";
    final var factoryBeanName = "factoryBeanNameValue";
    final var mockedBeanDefinition = mock(BeanDefinition.class);

    final var mockedFactoryBeanDefinition = mock(BeanDefinition.class);
    final var factoryBeanClassName = "factoryBeanClassName";
    final var factoryBeanClass = Long.class;

    when(mockedBeanDefinition.getBeanClassName()).thenReturn(null);

    when(mockedBeanDefinition.getFactoryMethodName())
        .thenReturn(factoryMethodName);

    when(mockedBeanDefinition.getFactoryBeanName())
        .thenReturn(factoryBeanName);

    when(beanDefinitionRegistry.getBeanDefinition(factoryBeanName))
        .thenReturn(mockedFactoryBeanDefinition);

    when(mockedFactoryBeanDefinition.getBeanClassName())
        .thenReturn(factoryBeanClassName);

    when(classRetriever.retrieve(factoryBeanClassName))
        .thenReturn(Optional.of(factoryBeanClass));

    when(methodRetriever.retrieve(factoryBeanClass, factoryMethodName))
        .thenReturn(Optional.empty());

    final var optionalClass = beanDefinitionClassRetriever.retrieve(mockedBeanDefinition);

    assertThat(optionalClass).isEmpty();
  }
}