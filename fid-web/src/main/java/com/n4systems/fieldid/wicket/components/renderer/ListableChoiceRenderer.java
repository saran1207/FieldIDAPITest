package com.n4systems.fieldid.wicket.components.renderer;

import com.n4systems.model.api.Listable;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class ListableChoiceRenderer<T extends Listable> implements IChoiceRenderer<T> {

    @Override
    public Object getDisplayValue(T listable) {
        return listable.getDisplayName();
    }

    @Override
    public String getIdValue(T listable, int index) {
        return listable.getId() + "";
    }

}
