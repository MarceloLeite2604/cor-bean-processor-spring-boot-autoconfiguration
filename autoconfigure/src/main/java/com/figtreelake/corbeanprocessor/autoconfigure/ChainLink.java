package com.figtreelake.corbeanprocessor.autoconfigure;

public interface ChainLink<T extends ChainLink<T>> {

    void setNext(T next);
}
