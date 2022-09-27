package com.figtreelake.corbeanprocessor.autoconfigure.util;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ClassUtil {

  public List<Type> retrieveGenericInterfaces(Class<?> clazz) {
    final var classes = retrieveClassHierarchy(clazz);
    final var interfaces = new LinkedList<Class<?>>();
    for (Class<?> c : classes) {
      interfaces.addAll(retrieveInterfacesFromClass(c));
    }

    classes.addAll(interfaces);

    final var genericInterfaces = new LinkedList<Type>();
    for (Class<?> c : classes) {
      genericInterfaces.addAll(List.of(c.getGenericInterfaces()));
    }

    return genericInterfaces;
  }

  private List<Class<?>> retrieveClassHierarchy(Class<?> clazz) {

    if (clazz == null) {
      return Collections.emptyList();
    }

    final var classes = new LinkedList<>(retrieveClassHierarchy(clazz.getSuperclass()));
    classes.push(clazz);
    return classes;
  }

  private List<Class<?>> retrieveInterfacesFromClass(Class<?> clazz) {

    if (clazz == null) {
      return Collections.emptyList();
    }

    final List<Class<?>> interfaces = new LinkedList<>();
    for (Class<?> anInterface : clazz.getInterfaces()) {
      interfaces.addAll(retrieveInterfacesFromInterface(anInterface));
      interfaces.add(anInterface);
    }

    return interfaces;
  }

  private List<Class<?>> retrieveInterfacesFromInterface(Class<?> iface) {

    if (iface == null) {
      return Collections.singletonList(iface);
    }

    final List<Class<?>> interfaces = new LinkedList<>();
    for (Class<?> i : iface.getInterfaces()) {
      interfaces.addAll(retrieveInterfacesFromInterface(i));
    }
    return interfaces;
  }
}
