package com.n4systems.fieldid.wicket.components.text;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;

public class LabelledTextArea<M> extends LabelledComponent<TextArea,M> {

    public LabelledTextArea(String id, String key, IModel<M> model) {
        super(id, key, model);
    }

    @Override
    protected TextArea<M> createLabelledComponent(String id, IModel<M> model) {
        return new TextArea(id, model);
    }

    public void setModelValue(String value) {
        component.setModelValue(new String[] {value});
    }

}
