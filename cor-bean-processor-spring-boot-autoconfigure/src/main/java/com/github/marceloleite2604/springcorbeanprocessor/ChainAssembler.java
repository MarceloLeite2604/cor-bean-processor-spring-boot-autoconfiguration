package com.github.marceloleite2604.springcorbeanprocessor;

import org.springframework.util.Assert;

import java.util.List;

public class ChainAssembler {

    public <U extends ChainLink<U>, T extends ChainLinkBeanContext<U>> T assemble(List<T> chainBeanContexts) {
        Assert.notEmpty(chainBeanContexts, "Chain bean contexts cannot be null.");

        T first = null;
        T previous = null;
        for (T chainBeanContext : chainBeanContexts) {
            if (first == null) {
                first = chainBeanContext;
            } else {
                previous.getBean().setNext(chainBeanContext.getBean());
            }
            previous = chainBeanContext;
        }

        return first;
    }
}
