package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyChainLinkA;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyChainLinkB;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyChainLinkC;
import com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link.DummyChainLinkD;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class BeanFixture {

  public static final ChainLink<?> FIRST_BEAN = new DummyChainLinkA();
  public static final Class<?> FIRST_BEAN_CLASS = FIRST_BEAN.getClass();
  public static final String FIRST_BEAN_NAME = FIRST_BEAN.getClass()
      .getSimpleName();

  public static final ChainLink<?> SECOND_BEAN = new DummyChainLinkB();
  public static final Class<?> SECOND_BEAN_CLASS = SECOND_BEAN.getClass();
  public static final String SECOND_BEAN_NAME = SECOND_BEAN.getClass()
      .getSimpleName();

  public static final ChainLink<?> THIRD_BEAN = new DummyChainLinkC();
  public static final Class<?> THIRD_BEAN_CLASS = THIRD_BEAN.getClass();
  public static final String THIRD_BEAN_NAME = THIRD_BEAN.getClass()
      .getSimpleName();

  public static final ChainLink<?> FOURTH_BEAN = new DummyChainLinkD();
  public static final Class<?> FOURTH_BEAN_CLASS = FOURTH_BEAN.getClass();
  public static final String FOURTH_BEAN_NAME = FOURTH_BEAN.getClass()
      .getSimpleName();

  public static final List<String> BEAN_NAMES = List.of(
      FIRST_BEAN_NAME,
      SECOND_BEAN_NAME,
      THIRD_BEAN_NAME,
      FOURTH_BEAN_NAME);

  public static final String[] BEAN_NAMES_ARRAY = BEAN_NAMES.toArray(String[]::new);

  @SuppressWarnings({"unchecked", "unused"})
  public static <T extends ChainLink<T>> T createFirstBean(Class<T> beanClass) {
    return (T) FIRST_BEAN;
  }

  @SuppressWarnings({"unchecked", "unused"})
  public static <T extends ChainLink<T>> T createSecondBean(Class<T> beanClass) {
    return (T) SECOND_BEAN;
  }

  @SuppressWarnings({"unchecked", "unused"})
  public static <T extends ChainLink<T>> T createThirdBean(Class<T> beanClass) {
    return (T) THIRD_BEAN;
  }

  @SuppressWarnings({"unchecked", "unused"})
  public static <T extends ChainLink<T>> T createFourthBean(Class<T> beanClass) {
    return (T) FOURTH_BEAN;
  }

  @SuppressWarnings({"unchecked", "unused"})
  public static <T extends ChainLink<T>> Class<T> retrieveFirstBeanClass(Class<T> beanClass) {
    return (Class<T>) FIRST_BEAN_CLASS;
  }

  @SuppressWarnings({"unchecked", "unused"})
  public static <T extends ChainLink<T>> Class<T> retrieveSecondBeanClass(Class<T> beanClass) {
    return (Class<T>) SECOND_BEAN_CLASS;
  }

  @SuppressWarnings({"unchecked", "unused"})
  public static <T extends ChainLink<T>> Class<T> retrieveThirdBeanClass(Class<T> beanClass) {
    return (Class<T>) THIRD_BEAN_CLASS;
  }

  @SuppressWarnings({"unchecked", "unused"})
  public static <T extends ChainLink<T>> Class<T> retrieveFourthBeanClass(Class<T> beanClass) {
    return (Class<T>) FOURTH_BEAN_CLASS;
  }

  public static <T extends ChainLink<T>> Map<String, T> createBeansMap(Class<T> clazz) {
    final var input = Map.ofEntries(
        Map.entry(FIRST_BEAN_NAME, createFirstBean(clazz)),
        Map.entry(SECOND_BEAN_NAME, createSecondBean(clazz)),
        Map.entry(THIRD_BEAN_NAME, createThirdBean(clazz)),
        Map.entry(FOURTH_BEAN_NAME, createFourthBean(clazz))
    );
    return new HashMap<>(input);
  }

  public static <T extends ChainLink<T>> List<T> createBeans(Class<T> clazz) {
    return createBeansMap(clazz).values()
        .stream()
        .toList();
  }
}
