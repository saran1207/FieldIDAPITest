package com.n4systems.fieldid.wicket.components.text;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.LabeledWebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class LabelledComponent<T extends LabeledWebMarkupContainer/*sic*/, M > extends Panel {

    private static final String INPUT_ID = "input";

    protected T component;

    public LabelledComponent(String id, String key, IModel<M> model) {
        super(id, model);

        component = createLabelledComponent(INPUT_ID,model);
        Preconditions.checkState(component!=null && component.getId().equals(INPUT_ID), "you must use id of '"+INPUT_ID+"' for labelled component.");

        FormComponentLabel label = new FormComponentLabel("label",component);
        Label labelSpan = new Label("labelSpan", new FIDLabelModel(key));
        label.add(labelSpan.setRenderBodyOnly(true));
        label.add(component);
        add(label);

        component.setOutputMarkupId(true);
        label.add(new AttributeModifier("for", Model.of(getComponentMarkupId(component))));
    }

    protected String getComponentMarkupId(T component) {
        return component.getMarkupId();
    }

    protected abstract T createLabelledComponent(String id, IModel<M> model);

}
