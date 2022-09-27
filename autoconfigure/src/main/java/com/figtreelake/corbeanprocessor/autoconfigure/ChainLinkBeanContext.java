package com.figtreelake.corbeanprocessor.autoconfigure;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.config.BeanDefinition;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ChainLinkBeanContext<T extends ChainLink<T>> {

    private final T bean;

    private final String name;

    private final BeanDefinition definition;
}
