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
  public static final String FIRST_BEAN_NAME = FIRST_BEAN.getClass()
      .getSimpleName();

  public static final ChainLink<?> SECOND_BEAN = new DummyChainLinkB();
  public static final String SECOND_BEAN_NAME = SECOND_BEAN.getClass()
      .getSimpleName();

  public static final ChainLink<?> THIRD_BEAN = new DummyChainLinkC();
  public static final String THIRD_BEAN_NAME = THIRD_BEAN.getClass()
      .getSimpleName();

  public static final ChainLink<?> FOURTH_BEAN = new DummyChainLinkD();
  public static final String FOURTH_BEAN_NAME = FOURTH_BEAN.getClass()
      .getSimpleName();

  public static final List<String> BEAN_NAMES = List.of(
      FIRST_BEAN_NAME,
      SECOND_BEAN_NAME,
      THIRD_BEAN_NAME,
      FOURTH_BEAN_NAME);

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
