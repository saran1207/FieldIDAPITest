package com.n4systems.fieldid.wicket.model;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class BooleanModel extends LoadableDetachableModel<String> {

    private IModel<Boolean> booleanModel;
	private IModel<String> trueLabelModel;
	private IModel<String> falseLabelModel;

    public BooleanModel(IModel<Boolean> booleanModel, IModel<String> trueLabelModel, IModel<String> falseLabelModel) {
        this.booleanModel = booleanModel;
		this.trueLabelModel = trueLabelModel;
		this.falseLabelModel = falseLabelModel;
    }

	public BooleanModel(IModel<Boolean> booleanModel) {
		this(booleanModel, new FIDLabelModel("value.yes"), new FIDLabelModel("value.no"));
	}

    @Override
    protected String load() {
        return booleanModel.getObject() ? trueLabelModel.getObject() : falseLabelModel.getObject();
    }

    @Override
    public void detach() {
        super.detach();
    }
}
