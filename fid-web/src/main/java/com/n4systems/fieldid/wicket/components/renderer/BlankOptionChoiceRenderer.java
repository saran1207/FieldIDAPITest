package com.n4systems.fieldid.wicket.components.renderer;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.io.Serializable;

public class BlankOptionChoiceRenderer<T extends Serializable> implements IChoiceRenderer<T> {

    private IModel<String> blankLabel;
    private IChoiceRenderer<T> wrappedRenderer;
    private T referenceToConsiderBlank;

    public BlankOptionChoiceRenderer(IModel<String> blankLabel, IChoiceRenderer<T> wrappedRenderer, T referenceToConsiderBlank) {
        this.blankLabel = blankLabel;
        this.wrappedRenderer = wrappedRenderer;
        this.referenceToConsiderBlank = referenceToConsiderBlank;
    }

    @Override
    public Object getDisplayValue(T object) {
        if (referenceToConsiderBlank == object) {
            return blankLabel.getObject();
        }
        return wrappedRenderer.getDisplayValue(object);
    }

    @Override
    public String getIdValue(T object, int index) {
        return wrappedRenderer.getIdValue(object, index);
    }
}
