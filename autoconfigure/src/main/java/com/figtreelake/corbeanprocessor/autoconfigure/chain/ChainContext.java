package com.figtreelake.corbeanprocessor.autoconfigure.chain;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLinkBeanDefinitionContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChainContext<T extends ChainLink<T>> {

  private final List<String> beanNames;

  private final Map<String, T> chainLinkBeansByName;

  private final List<ChainLinkBeanDefinitionContext<T>> beanDefinitionContexts;

  @SuppressWarnings("unchecked")
  public void addChainLinkBean(String name, Object bean) {
    chainLinkBeansByName.put(name, (T) bean);
  }

  public boolean hasAllChainLinks() {
    return beanNames.size() == chainLinkBeansByName.size();
  }

  public List<T> getSortedChainLinks() {
    return beanNames.stream()
        .map(chainLinkBeansByName::get)
        .toList();
  }
}
