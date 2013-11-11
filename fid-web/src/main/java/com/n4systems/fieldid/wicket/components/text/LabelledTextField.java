package com.n4systems.fieldid.wicket.components.text;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class LabelledTextField<M> extends LabelledComponent<TextField,M> {

    public LabelledTextField(String id, String key, IModel<M> model) {
        super(id, key, model);
    }

    @Override
    protected TextField<M> createLabelledComponent(String id, IModel<M> model) {
        return new TextField(id, model);
    }

}
