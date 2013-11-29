package com.n4systems.util.collections;

import com.n4systems.util.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class PrioritizedList<T> extends ArrayList<T> implements Comparator<T> {
    
    private static final Logger logger = Logger.getLogger(PrioritizedList.class);
    
    private int threshold;
    private boolean atThreshold;
    private Comparator<T> comparator;
    private int originalSize;

    public PrioritizedList(List<? extends T> values, int threshold) {
        this(values, threshold, null);
    }

    public PrioritizedList(List<? extends T> values, int threshold, Comparator<T> comparator) {
        this.threshold = threshold;
        this.atThreshold = values.size() > threshold;
        this.comparator = comparator==null ? this : comparator;
        this.originalSize = values.size();
        init(values, threshold);
    }

    private void init(List<? extends T> values, int threshold) {
        TreeSet<T> priorities = new TreeSet<T>(getComparator());
        for (T value : values) {
            priorities.add(value);
        }

        int count = 0;
        for (T value : priorities) {
            add(value);
            count++;
            if (count>=threshold) {
                break;
            }
        }
    }

    protected Comparator<T> getComparator() {
        return comparator==null ? this : comparator;    // by default, just use this
    }
    
    public boolean isAtThreshold() {
        return atThreshold;
    }
    
    @Override
    public int compare(T o1, T o2) {
        return StringUtils.compareAsString(o1, o2);
    }

    public int getOriginalSize() {
        return originalSize;
    }
}



