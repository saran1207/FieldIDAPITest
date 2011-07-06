package com.n4systems.fieldid.wicket.components.renderer;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Status;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class StatusChoiceRenderer implements IChoiceRenderer<Status> {

    @Override
    public Object getDisplayValue(Status object) {
        return new FIDLabelModel(object.getLabel()).getObject();
    }

    @Override
    public String getIdValue(Status object, int index) {
        return object.getId();
    }

}
