package com.n4systems.fieldid.wicket.model;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.ArrayList;
import java.util.List;

public class ListWithBlankOptionModel<T> extends LoadableDetachableModel<List<T>> {

    private IModel<List<T>> wrappedModel;
    private T blankOption;

    public ListWithBlankOptionModel(IModel<List<T>> wrappedModel, T blankOption) {
        this.wrappedModel = wrappedModel;
        this.blankOption = blankOption;
    }

    @Override
    protected List<T> load() {
        List<T> list = new ArrayList<T>(wrappedModel.getObject());
        list.add(0, blankOption);

        return list;
    }

}
