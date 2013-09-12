package com.n4systems.model.utils;

import java.util.HashMap;

public class ClassMap<T> extends HashMap<Class<?>,T> {

    public ClassMap() {
    }

    @Override
    public T get(Object key) {
        if (!(key instanceof Class)) {
            return null;
        }
        for (Object k:keySet()) {
            if (k instanceof Class) {
                Class<?> clazz = (Class<?>) k;
                if (clazz.isAssignableFrom((Class)key)) {
                    return super.get(k);
                }
            }
        }
        return super.get(key);
    }
}
