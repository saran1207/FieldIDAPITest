package com.n4systems.fieldid.wicket.model;

import org.apache.wicket.model.IModel;

public abstract class ReadOnlyModel<T> implements IModel<T> {
    @Override
    public void setObject(T object) {
    }

    @Override
    public void detach() {
    }
}
