package com.n4systems.util.collections;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

// gives a higher priority to items that are unique according to given property.
// for example, if the bean is person:{firstname, lastname, phone}
// and you use UniquePrioritizer("firstname")
// for the collection {bob foo, sue q, john doe, bob apple} then the second bob will be given a lower priority.
// the cardinality of this will be quite low as values become more unique.  you may want to chain with other prioritizer.
public class UniquePrioritizer<T> extends DefaultPrioritizer<T> {

    private static final Logger logger=Logger.getLogger(UniquePrioritizer.class);
    private String property;

    private Map<Object, Integer> values = new HashMap<Object, Integer>();

    public UniquePrioritizer(String property) {
        super();
        this.property = property;
    }

    @Override
    public Object getPriority(T value) {
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
                // if an object with same priority already exists, we'll bump this one down a notch.
                int lowerValue = values.get(priority).intValue();
                values.put(priority, lowerValue);
                return lowerValue;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    private Object getPriortyFor(T bean) {
        try {
            return PropertyUtils.getProperty(bean, property);
        } catch (Exception e) {
            logger.error("error while trying to prioritize [" + bean.getClass().getSimpleName() + "] = " + bean );
            return LOW_PRIORITY;
        }
    }
}

