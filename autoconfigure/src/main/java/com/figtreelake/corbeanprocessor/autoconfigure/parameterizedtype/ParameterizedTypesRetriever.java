package com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Contains all logic to retrieve information about parameterized types
 * implemented by a specific class.
 * Parameterized types are Java classes and interfaces that uses generic types.
 * Its context also provides information about how such generic types are
 * resolved (which types were used to implement generic types).
 *
 * @author MarceloLeite2604
 */
public class ParameterizedTypesRetriever {

  /**
   * Retrieve the parameterized type context of all generic interfaces
   * implemented by such class, its superclasses and interfaces
   * implemented by them.
   * @param rootClass Root class to retrieve all parameterized context.
   * @return The context of all parameterized classes implemented.
   */
  public Set<ParameterizedTypeContext> retrieveForClass(Class<?> rootClass) {
    if (rootClass.isAnnotation() || rootClass.isInterface() || rootClass.isEnum()) {
      throw new IllegalArgumentException("Argument is not a class.");
    }

    final var classes = retrieveClassHierarchy(rootClass);

    final var interfaces = retrieveInterfaceFromClasses(classes);

    classes.addAll(interfaces);

    final Map<Type, ParameterizedTypeContext> parameterizedTypeContextsByType = new HashMap<>();

    for (Class<?> clazz : classes) {

      final var parentGenericInterfaceContext = parameterizedTypeContextsByType.get(clazz);
      Stream.of(clazz.getGenericInterfaces())
          .forEach(genericInterface -> {
            if (genericInterface instanceof ParameterizedType parameterizedType) {
              final var genericInterfaceContextBuilder = ParameterizedTypeContext.builder()
                  .parameterizedType(parameterizedType);

              final var parameterizedTypeArguments = retrieveParameterizedTypeArguments(
                  parameterizedType, parentGenericInterfaceContext);
              genericInterfaceContextBuilder.arguments(parameterizedTypeArguments);

              parameterizedTypeContextsByType.put(parameterizedType.getRawType(), genericInterfaceContextBuilder.build());
            }
          });
    }

    return new HashSet<>(parameterizedTypeContextsByType.values());
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
    for (Class<?> clazz : classes) {
      interfaces.addAll(retrieveInterfacesFromClass(clazz));
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

    final List<Class<?>> interfaces = new LinkedList<>();
    for (Class<?> anInterface : clazz.getInterfaces()) {
      interfaces.addAll(retrieveInterfacesFromInterface(anInterface));
      interfaces.add(anInterface);
    }

    return interfaces;
  }

  private List<Class<?>> retrieveInterfacesFromInterface(Class<?> iface) {

    final List<Class<?>> interfaces = new LinkedList<>();
    for (Class<?> i : iface.getInterfaces()) {
      interfaces.addAll(retrieveInterfacesFromInterface(i));
    }
    return interfaces;
  }
}
