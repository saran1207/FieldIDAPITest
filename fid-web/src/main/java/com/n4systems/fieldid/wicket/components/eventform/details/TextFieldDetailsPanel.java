package com.n4systems.fieldid.wicket.components.eventform.details;

import com.n4systems.model.TextFieldCriteria;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class TextFieldDetailsPanel extends Panel {

    public TextFieldDetailsPanel(String id, IModel<TextFieldCriteria> model) {
        super(id, model);
    }

}
