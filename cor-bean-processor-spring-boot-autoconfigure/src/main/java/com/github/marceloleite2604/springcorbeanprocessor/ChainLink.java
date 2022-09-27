package com.github.marceloleite2604.springcorbeanprocessor;

public interface ChainLink<T extends ChainLink<T>> {

    void setNext(T next);
}
