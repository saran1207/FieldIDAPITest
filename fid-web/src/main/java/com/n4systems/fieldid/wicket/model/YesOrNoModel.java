package com.n4systems.fieldid.wicket.model;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class YesOrNoModel extends LoadableDetachableModel<String> {

    private IModel<Boolean> booleanModel;

    public YesOrNoModel(IModel<Boolean> booleanModel) {
        this.booleanModel = booleanModel;
    }

    @Override
    protected String load() {
        return booleanModel.getObject() ? new FIDLabelModel("value.yes").getObject() : new FIDLabelModel("value.no").getObject();
    }

    @Override
    public void detach() {
        super.detach();
    }
}
