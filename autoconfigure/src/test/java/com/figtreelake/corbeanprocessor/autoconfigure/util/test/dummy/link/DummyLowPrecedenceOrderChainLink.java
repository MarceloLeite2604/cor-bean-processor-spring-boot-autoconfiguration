package com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link;

import org.springframework.core.annotation.Order;

@Order(20)
public class DummyLowPrecedenceOrderChainLink  extends DummyAbstractChainLink {
}
