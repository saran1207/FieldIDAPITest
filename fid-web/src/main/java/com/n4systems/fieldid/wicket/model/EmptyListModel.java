package com.n4systems.fieldid.wicket.model;

import org.apache.wicket.model.IModel;

import java.util.Collections;
import java.util.List;

public class EmptyListModel<T> implements IModel<List<T>> {
    @Override
    public List<T> getObject() {
        return Collections.emptyList();
    }

    @Override
    public void setObject(List<T> object) {
    }

    @Override
    public void detach() {
    }
}
