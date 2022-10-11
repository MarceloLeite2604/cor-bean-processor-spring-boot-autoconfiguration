package com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link;

import org.springframework.core.annotation.Order;

@Order(10)
public class DummyHighPrecedenceOrderChainLink extends DummyAbstractChainLink {
}
