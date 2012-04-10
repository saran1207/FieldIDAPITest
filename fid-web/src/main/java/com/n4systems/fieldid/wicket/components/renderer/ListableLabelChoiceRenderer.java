package com.n4systems.fieldid.wicket.components.renderer;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.api.Listable;

// To be used when the listable returns a resource key instead of the actual string to display
public class ListableLabelChoiceRenderer<T extends Listable> extends ListableChoiceRenderer<T> {

    @Override
    public Object getDisplayValue(T listable) {
        return new FIDLabelModel(super.getDisplayValue(listable).toString()).getObject();
    }

}
