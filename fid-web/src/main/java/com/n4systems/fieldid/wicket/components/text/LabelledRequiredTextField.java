package com.n4systems.fieldid.wicket.components.text;


import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class LabelledRequiredTextField<M> extends LabelledComponent<TextField,M> {

    public LabelledRequiredTextField(String id, String key, IModel<M> model) {
        super(id, key, model);
    }

    @Override
    protected TextField createLabelledComponent(String id, IModel<M> model) {
        return new RequiredTextField(id, model);
    }

}
