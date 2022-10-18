package com.figtreelake.corbeanprocessor.autoconfigure.util.test.implementation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.core.ResolvableType;

import java.util.Map;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanDefinitionTestImplementation implements BeanDefinition {

  @Getter
  @Setter
  private String parentName;

  @Getter
  @Setter
  private String beanClassName;

  @Getter
  @Setter
  private String scope;

  @Getter
  @Setter
  private boolean lazyInit;

  @Getter
  @Setter
  private String[] dependsOn;

  @Getter
  @Setter
  private boolean autowireCandidate;

  @Getter
  @Setter
  private boolean primary;

  @Getter
  @Setter
  private String factoryBeanName;

  @Getter
  @Setter
  private String factoryMethodName;

  @Getter
  private final ConstructorArgumentValues constructorArgumentValues;

  @Getter
  private final MutablePropertyValues propertyValues;

  @Getter
  @Setter
  private String initMethodName;

  @Getter
  @Setter
  private String destroyMethodName;

  @Getter
  @Setter
  private int role;

  @Getter
  @Setter
  private String description;

  @Getter
  private ResolvableType resolvableType;

  @Getter
  private final boolean singleton;

  @Getter
  private final boolean prototype;

  private final boolean abstractBean;

  @Getter
  private final String resourceDescription;

  @Getter
  private final BeanDefinition originatingBeanDefinition;

  private final Map<String, Object> attributes;

  @Override
  public boolean isAbstract() {
    return abstractBean;
  }

  @Override
  public void setAttribute(String name, Object value) {
    attributes.put(name, value);
  }

  @Override
  public Object getAttribute(String name) {
    return attributes.get(name);
  }

  @Override
  public Object removeAttribute(String name) {
    return attributes.remove(name);
  }

  @Override
  public boolean hasAttribute(String name) {
    return attributes.containsKey(name);
  }

  @Override
  public String[] attributeNames() {
    return attributes.keySet()
        .toArray(String[]::new);
  }
}
