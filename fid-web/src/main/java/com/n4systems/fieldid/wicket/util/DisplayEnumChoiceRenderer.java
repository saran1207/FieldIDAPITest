package com.n4systems.fieldid.wicket.util;

import com.n4systems.model.api.DisplayEnum;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class DisplayEnumChoiceRenderer implements IChoiceRenderer<DisplayEnum> {
    @Override
    public Object getDisplayValue(DisplayEnum object) {
        return object.getLabel();
    }

    @Override
    public String getIdValue(DisplayEnum object, int index) {
        return object.name();
    }
}
