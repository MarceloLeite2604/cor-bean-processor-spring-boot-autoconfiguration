package com.figtreelake.corbeanprocessor.autoconfigure.util.test.dummy.link;

import com.figtreelake.corbeanprocessor.autoconfigure.ChainLink;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DummyAbstractChainLink implements ChainLink<DummyAbstractChainLink> {

  private DummyAbstractChainLink next;
}
