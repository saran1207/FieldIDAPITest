package com.n4systems.util.collections;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PrioritizedList<T> extends ArrayList<T> {
    
    private static final Logger logger = Logger.getLogger(PrioritizedList.class);
    
    private TreeMap<Object,List<T>> priorities = new TreeMap<Object,List<T>>();
    private int threshold;
    private Prioritizer<T> prioritizer;
    
    public PrioritizedList(List<? extends T> values, Prioritizer<T> prioritizer, int threshold) {
        this.threshold = threshold;
        this.prioritizer = prioritizer;
        create(values);
    }
    
    private void create(List<? extends T> unprioritizedValues) {
        for (T value:unprioritizedValues) {
            prioritize(value, getPriority(value));
        }
        // now they're prioritized, lets add them and drop off ones after threshold.
        // note that we do descending order because TreeMaps are low...high.  we want the reverse.
        int count = 0;
        for (Object key:priorities.descendingKeySet()) {
            List<T> values = priorities.get(key);
            if (count+values.size()>threshold && isHardLimit()) { 
                addAll(values.subList(0, threshold-count));
            } else {
                addAll(values);
            }
            count+=values.size();
            if (count>=threshold) {
                break;
            }
        }
    }

    private Object getPriority(T value) {
        if (prioritizer!=null) {
            return prioritizer.getPriority(value);
        }
        return 0;  // otherwise give them all the same value.
    }

    private void prioritize(T value, Object priority) {
        List<T> values = priorities.get(priority);
        if (values == null) {
            priorities.put(priority, Lists.newArrayList(value));
        } else {
            values.add(getCollisionIndex(values.size(), value), value);
        }
    }

    protected int getCollisionIndex(int size, T value) {
        // note : we want to get some sort to randomness in our population of values so if they are truncated
        // we'll get a non-chronological distribution of nodes.
        return value.hashCode() % size;
    }

    protected boolean isHardLimit() {
        return true;
    }

    public boolean isAtThreshold() {
        return size()>=threshold;
    }

}



