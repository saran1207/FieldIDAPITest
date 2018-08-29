package com.n4systems.fieldid.service.search.columns;

import java.util.List;

/**
 * Take a tuple of objects - one of which is the specified entity and return that entity. Used
 * when we only want that object from the result tuple.
 */
public class GetEntityFromResultTransformer implements org.hibernate.transform.ResultTransformer {

    private Class desiredEntity;

    public GetEntityFromResultTransformer(Class desiredEntity) {
        this.desiredEntity = desiredEntity;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        for (Object obj: tuple) {
            if (obj != null && obj.getClass().equals(desiredEntity))
                return obj;
        }
        return null;
    }

    @Override
    public List transformList(List collection) {
        return collection;
    }
}
