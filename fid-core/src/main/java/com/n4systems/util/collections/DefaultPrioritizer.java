package com.n4systems.util.collections;

public class DefaultPrioritizer<T> implements Prioritizer<T> {

    protected final int HIGH_PRIORITY = 100000;
    protected final int LOW_PRIORITY = -123456;

    @Override
    public Object getPriority(T value) {
        return HIGH_PRIORITY;   // defaults all to same priority...override to give some meaning.
    }

    @Override
    public int getCollisionIndex(int size, T value) {
        return value.hashCode() % size;
    }

}

        
