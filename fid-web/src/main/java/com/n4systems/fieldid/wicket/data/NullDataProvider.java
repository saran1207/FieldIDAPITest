package com.n4systems.fieldid.wicket.data;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;

public class NullDataProvider<T extends Serializable> implements ISortableDataProvider<T> {

    @Override
    public Iterator<? extends T> iterator(int first, int count) {
        return Collections.<T>emptyList().iterator();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public IModel<T> model(T object) {
        return new Model<T>(object);
    }

    @Override
    public void detach() {
    }

    @Override
    public ISortState getSortState() {
        return null;
    }

    @Override
    public void setSortState(ISortState state) {
    }

}
