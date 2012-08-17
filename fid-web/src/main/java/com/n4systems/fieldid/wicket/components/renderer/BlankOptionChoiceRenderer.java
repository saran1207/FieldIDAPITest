package com.n4systems.fieldid.wicket.components.renderer;

import com.n4systems.model.api.Listable;
import com.n4systems.util.GroupedListingPair;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

public class BlankOptionChoiceRenderer<T extends GroupedListingPair> implements IChoiceRenderer<T> {

    private IModel<String> blankLabel;
    private IChoiceRenderer<T> wrappedRenderer;

    public BlankOptionChoiceRenderer(IModel<String> blankLabel, IChoiceRenderer<T> wrappedRenderer) {
        this.blankLabel = blankLabel;
        this.wrappedRenderer = wrappedRenderer;
    }

    @Override
    public Object getDisplayValue(T object) {
        if (object.getId().equals(0L)) {
            return blankLabel.getObject();
        }
        return wrappedRenderer.getDisplayValue(object);
    }

    @Override
    public String getIdValue(T object, int index) {
        return wrappedRenderer.getIdValue(object, index);
    }
}
