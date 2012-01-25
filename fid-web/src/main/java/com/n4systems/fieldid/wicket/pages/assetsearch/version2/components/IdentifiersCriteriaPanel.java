package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.components.IdentifierLabel;

@SuppressWarnings("serial")
public class IdentifiersCriteriaPanel extends Panel {

	public IdentifiersCriteriaPanel(String id, IModel<?> model) {
        super(id, model);
        add(new IdentifierLabel("identifierLabel"));
        add(new TextField<String>("rfidNumber"));
        add(new TextField<String>("identifier"));
        add(new TextField<String>("referenceNumber"));
    }

}
