package com.n4systems.fieldid.wicket.data;

import com.n4systems.util.persistence.search.ResultTransformer;

import java.util.List;

public class IdentityTransformer<T> implements ResultTransformer<List<T>> {

    @Override
    @SuppressWarnings("unchecked")
    public List<T> transform(List list) {
        return list;
    }

}
