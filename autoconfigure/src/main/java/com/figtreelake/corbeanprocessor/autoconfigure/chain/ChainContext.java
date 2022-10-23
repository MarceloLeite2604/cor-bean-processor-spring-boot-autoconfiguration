package com.figtreelake.corbeanprocessor.autoconfigure.chain;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLinkBeanDefinitionContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Store all information related to a chain of responsibility.
 *
 * @param <T> Element that either implements or extends the {@link ChainLink} interface.
 */
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChainContext<T extends ChainLink<T>> {

  private final List<String> beanNames;

  private Map<String, T> chainLinkBeansByName;

  private final List<ChainLinkBeanDefinitionContext<T>> beanDefinitionContexts;

  /**
   * Add a new chain link to the beans map.
   *
   * @param name The chain link bean name.
   * @param bean The bean object.
   */
  @SuppressWarnings("unchecked")
  public void addChainLinkBean(String name, Object bean) {
    getChainLinkBeansByName().put(name, (T) bean);
  }

  /**
   * Inform if the context already has all links required to build the chain.
   *
   * @return True if context already has all links, false otherwise.
   */
  public boolean hasAllChainLinks() {
    return beanNames.size() == chainLinkBeansByName.size();
  }

  /**
   * Retrieve chain links stored on this context that matches the bean names stored.
   *
   * @return The chain links stored on this context that matches the bean names stored.
   */
  public List<T> retrieveChainLinks() {
    return beanNames.stream()
        .map(getChainLinkBeansByName()::get)
        .toList();
  }

  /**
   * Return a map of all chain links stored in the context indexed by their names.
   *
   * @return A map of all chain links stored in the context indexed by their names.
   */
  Map<String, T> getChainLinkBeansByName() {
    if (chainLinkBeansByName == null) {
      chainLinkBeansByName = new HashMap<>();
    }
    return chainLinkBeansByName;
  }
}
