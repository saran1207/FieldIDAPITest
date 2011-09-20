package com.n4systems.fieldid.wicket.model;

import org.apache.wicket.model.IModel;

public class TrimmedStringModel implements IModel<String> {

    private IModel<String> model;
    private int limit;

    public TrimmedStringModel(IModel<String> model, int limit) {
        this.model = model;
        this.limit = limit;
    }

    @Override
    public void detach() { }

    @Override
    public String getObject() {
        return getTrimmedString(model.getObject());
    }

    @Override
    public void setObject(String object) {
        model.setObject(object);
    }

    protected String getTrimmedString(String str) {
        return str != null && str.length() > limit ? str.substring(0,limit)+"..." : str;
    }
}
