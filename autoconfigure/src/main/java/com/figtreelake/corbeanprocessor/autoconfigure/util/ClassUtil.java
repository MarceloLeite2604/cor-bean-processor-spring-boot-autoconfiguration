package com.figtreelake.corbeanprocessor.autoconfigure.util;

import com.figtreelake.corbeanprocessor.autoconfigure.ParameterizedTypeContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.stream.Stream;

public class ClassUtil {

  public Set<ParameterizedTypeContext> retrieveGenericInterfacesForClass(Class<?> rootClass) {
    if (rootClass.isAnnotation() || rootClass.isInterface() || rootClass.isEnum()) {
      throw new IllegalArgumentException("Argument is not a class.");
    }

    final var classes = retrieveClassHierarchy(rootClass);

    final var interfaces = retrieveInterfaceFromClasses(classes);

    classes.addAll(interfaces);

    final Map<Type, ParameterizedTypeContext> genericInterfaceContexts = new HashMap<>();

    for (Class<?> clazz : classes) {

      final var parentGenericInterfaceContext = genericInterfaceContexts.get(clazz);
      Stream.of(clazz.getGenericInterfaces())
          .forEach(genericInterface -> {
            if (genericInterface instanceof ParameterizedType parameterizedType) {
              final var genericInterfaceContextBuilder = ParameterizedTypeContext.builder()
                  .parameterizedType(parameterizedType);

              final var parameterizedTypeArguments = retrieveParameterizedTypeArguments(
                  parameterizedType, parentGenericInterfaceContext);
              genericInterfaceContextBuilder.arguments(parameterizedTypeArguments);

              genericInterfaceContexts.put(parameterizedType.getRawType(), genericInterfaceContextBuilder.build());
            }
          });
    }

    return new HashSet<>(genericInterfaceContexts.values());
  }

  private Map<String, Class<?>> retrieveParameterizedTypeArguments(
      ParameterizedType parameterizedType,
      ParameterizedTypeContext parentParameterizedTypeContext) {
    final var typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
    final var typeArguments = parameterizedType.getActualTypeArguments();

    final Map<String, Class<?>> parameterizedTypeArguments = new HashMap<>();
    for (int index = 0; index < typeParameters.length; index++) {
      final var typeParameter = typeParameters[index];
      var typeArgument = typeArguments[index];

      if (typeArgument instanceof TypeVariable<?> typeVariableArgument) {
        typeArgument = parentParameterizedTypeContext.getArguments()
            .get(typeVariableArgument.getName());
      }

      parameterizedTypeArguments.put(typeParameter.getTypeName(), (Class<?>) typeArgument);
    }

    return parameterizedTypeArguments;
  }

  private LinkedList<Class<?>> retrieveInterfaceFromClasses(List<Class<?>> classes) {
    final var interfaces = new LinkedList<Class<?>>();
    for (Class<?> c : classes) {
      interfaces.addAll(retrieveInterfacesFromClass(c));
    }
    return interfaces;
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
