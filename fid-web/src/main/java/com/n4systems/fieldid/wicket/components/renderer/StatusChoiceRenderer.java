package com.n4systems.fieldid.wicket.components.renderer;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.EventResult;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class StatusChoiceRenderer implements IChoiceRenderer<EventResult> {

    @Override
    public Object getDisplayValue(EventResult object) {
        return new FIDLabelModel(object.getLabel()).getObject();
    }

    @Override
    public String getIdValue(EventResult object, int index) {
        return object.getId();
    }

}
