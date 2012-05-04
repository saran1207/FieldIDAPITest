package com.n4systems.util.collections;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DefaultPrioritizer<T> implements Prioritizer<T> {

    protected final int HIGH_PRIORITY = 100000;
    protected final int LOW_PRIORITY = -123456;


    List<Prioritizer<T>> chain = new LinkedList<Prioritizer<T>>();
    private Map<Object, Integer> values = new HashMap<Object, Integer>();

    @Override
    public final int getPriority(T value) {
        if (value==null) {
            // this is not a well defined return value because it isn't guaranteed to be of same type as bean.property.
            //   in practice, you shouldn't be prioritizing nulls?
            return LOW_PRIORITY;
        }

        try {
            Object priority = getPriortyFor(value);
            if (values.get(priority)==null) {
                values.put(priority, HIGH_PRIORITY);
                return HIGH_PRIORITY;
            } else {
                int lowerValue = values.get(priority).intValue();
                values.put(priority, lowerValue);
                return lowerValue;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    protected Object getPriortyFor(T value) {
        return HIGH_PRIORITY;
    }

    public Prioritizer<T> add(Prioritizer<T> prioritizer) {
        chain.add(prioritizer);
        return this;
    }


    public Prioritizer<T> remove(Prioritizer<T> prioritizer) {
        chain.remove(prioritizer);
        return this;
    }
}

        
