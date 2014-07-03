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
        TextArea textArea = new TextArea(id, model);
        textArea.add(new AttributeAppender("maxlength", Integer.toString(getMaxLength())));
        return textArea;
    }

    //If you want to change the maxlength size, override this method when creating this object.
    public int getMaxLength() {
        return 255;
    }

}
