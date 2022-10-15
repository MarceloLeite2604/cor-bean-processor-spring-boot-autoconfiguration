package com.figtreelake.corbeanprocessor.autoconfigure.link;

import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypeContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.config.BeanDefinition;


@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ChainLinkBeanDefinitionContext<T extends ChainLink<T>> {

    private final BeanDefinition definition;

    private final String name;

    private final Class<? extends T> beanClass;

    private final ParameterizedTypeContext chainLinkTypeContext;
}
