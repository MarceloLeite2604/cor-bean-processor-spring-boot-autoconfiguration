package com.figtreelake.corbeanprocessor.autoconfigure;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.config.BeanDefinition;


@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ChainLinkBeanDefinitionContext<T extends ChainLink<T>> {

    private final BeanDefinition definition;

    private final String name;

    private final Class<T> beanClass;

    private final ParameterizedTypeContext chainLinkTypeContext;
}
