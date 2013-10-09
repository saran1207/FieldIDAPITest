package com.n4systems.model.utils;

import java.util.LinkedHashMap;


// usage note : this maintains the insertion order so when you traverse keys/values it will be in order of how they are added.
public class ClassMap<T> extends LinkedHashMap<Class<?>,T> {

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
