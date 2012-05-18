package com.n4systems.util.collections;

public interface Prioritizer<T> {
    Object getPriority(T value);
    // used if two values have the same priority, decides where should i store them in the bucket.
    int getCollisionIndex(int size, T value);
}


