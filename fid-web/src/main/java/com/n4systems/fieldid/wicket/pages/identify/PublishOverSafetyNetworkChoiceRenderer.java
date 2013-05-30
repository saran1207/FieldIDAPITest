package com.n4systems.fieldid.wicket.pages.identify;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class PublishOverSafetyNetworkChoiceRenderer implements IChoiceRenderer<Boolean> {

    @Override
    public Object getDisplayValue(Boolean object) {
        if (object == null) {
            // Theoretically impossible(tm)
            return "Unknown";
        } else if (object) {
            return new FIDLabelModel("label.publishedstateselector").getObject();
        } else {
            return new FIDLabelModel("label.unpublishedstateselector").getObject();
        }
    }

    @Override
    public String getIdValue(Boolean object, int index) {
        return object.toString();
    }

}
