package com.n4systems.fieldid.wicket.model;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.ArrayList;
import java.util.List;

public class CombinedListModel<T> extends LoadableDetachableModel<List<T>> {

    private IModel<List<T>>[] models;

    public CombinedListModel(IModel<List<T>>... models) {
        this.models = models;
    }

    @Override
    protected List<T> load() {
        List<T> results = new ArrayList<T>();
        for (IModel<List<T>> model : models) {
            results.addAll(model.getObject());
        }
        return results;
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        for (IModel<List<T>> model : models) {
            model.detach();
        }
    }

}
