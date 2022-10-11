package com.figtreelake.corbeanprocessor.autoconfigure.util.test.fixture;

import com.figtreelake.corbeanprocessor.autoconfigure.link.ChainLink;
import com.figtreelake.corbeanprocessor.autoconfigure.parameterizedtype.ParameterizedTypeContext;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ParameterizedTypeContextFixture {

  public static ParameterizedTypeContext createForChainLink(Class<?> argumentClass) {
    final var parameterizedType = ParameterizedTypeFixture.create(ChainLink.class);

    return ParameterizedTypeContext.builder()
        .parameterizedType(parameterizedType)
        .argument("T", argumentClass)
        .build();
  }
}
