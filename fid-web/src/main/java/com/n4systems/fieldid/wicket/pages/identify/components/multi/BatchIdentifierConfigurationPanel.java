package com.n4systems.fieldid.wicket.pages.identify.components.multi;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class BatchIdentifierConfigurationPanel extends Panel {

    public BatchIdentifierConfigurationPanel(String id, IModel<String> identifierModel) {
        super(id);

        add(new TextField<String>("identifier", identifierModel));
    }

}
