package com.n4systems.util.collections;

public interface Prioritizer<T> {
    int getPriority(T value);
}

