package com.n4systems.fieldid.wicket.components.text;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class LabelledComponent<T extends FormComponent, M > extends Panel {

    private static final String INPUT_ID = "input";

    protected T component;

    public LabelledComponent(String id, String key, IModel<M> model) {
        super(id, model);

        component = createLabelledComponent(INPUT_ID,model);
        component.setLabel(new FIDLabelModel(key));
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

    public <T extends LabelledComponent> T required() {
        component.setRequired(true);
        return (T) this;
    }

    /**
     * Some components support the "maxlength" attribute.  This simply uses an AttributeModifier to
     * tack this value onto the Text Input component wrapped inside the LabelledComponent.
     *
     * @param maxLength - An integer value representing the maximum amount of characters the component should hold.
     * @param <T> - The type of component (LabelledTextArea, etc.) that is being modified.
     * @return A reference to the LabelledComponent being modified.
     */
    public <T extends LabelledComponent> T setMaxLength(int maxLength) {
        component.add(new AttributeModifier("maxlength", Integer.toString(maxLength)));
        return (T) this;
    }

    public void clearInput() {
        component.clearInput();
    }

    public void addBehavior(Behavior behavior) {
        component.add(behavior);
    }

}
