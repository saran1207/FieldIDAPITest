package com.n4systems.fieldid.wicket.components.text;


import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class LabelledRequiredTextField<M> extends LabelledComponent<TextField,M> {

    public LabelledRequiredTextField(String id, String key, IModel<M> model) {
        super(id, key, model);
    }

    @Override
    protected TextField<M> createLabelledComponent(String id, IModel<M> model) {
        RequiredTextField<M> textField = new RequiredTextField(id, model);
        textField.add(new AttributeAppender("maxlength", Integer.toString(getMaxLength())));
        return textField;
    }

    //If you want to change the maxlength size, override this method when creating this object.
    public int getMaxLength() {
        return 255;
    }

}