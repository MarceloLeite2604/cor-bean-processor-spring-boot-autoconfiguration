package com.figtreelake.corbeanprocessor.autoconfigure;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChainContext<T extends ChainLink<T>> {

  private final List<String> beanNames;

  private final Map<String, T> chainLinkBeansByName;

  private final List<ChainLinkBeanDefinitionContext<T>> beanDefinitionContexts;

  @SuppressWarnings("unchecked")
  public void addChainLinkBean(String name, Object bean) {
    chainLinkBeansByName.put(name, (T) bean);
  }

  boolean hasAllChainLinks() {
    return beanNames.size() == chainLinkBeansByName.size();
  }

  public List<T> getSortedChainLinks() {
    return beanNames.stream()
        .map(chainLinkBeansByName::get)
        .toList();
  }
}
