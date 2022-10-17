package com.figtreelake.corbeanprocessor.autoconfigure.chain;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLinkBeanDefinitionContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChainContext<T extends ChainLink<T>> {

  private final List<String> beanNames;

  private Map<String, T> chainLinkBeansByName;

  private final List<ChainLinkBeanDefinitionContext<T>> beanDefinitionContexts;

  @SuppressWarnings("unchecked")
  public void addChainLinkBean(String name, Object bean) {
    getChainLinkBeansByName().put(name, (T) bean);
  }

  public boolean hasAllChainLinks() {
    return beanNames.size() == chainLinkBeansByName.size();
  }

  public List<T> getSortedChainLinks() {
    return beanNames.stream()
        .map(getChainLinkBeansByName()::get)
        .toList();
  }

  Map<String, T> getChainLinkBeansByName() {
    if (chainLinkBeansByName == null) {
      chainLinkBeansByName = new HashMap<>();
    }
    return chainLinkBeansByName;
  }
}
