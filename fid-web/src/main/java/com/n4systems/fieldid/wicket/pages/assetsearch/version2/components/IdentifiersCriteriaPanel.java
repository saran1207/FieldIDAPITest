package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.n4systems.fieldid.wicket.components.IdentifierLabel;

@SuppressWarnings("serial")
public class IdentifiersCriteriaPanel extends Panel {

	public IdentifiersCriteriaPanel(String id, final IModel<?> model) {
        super(id, model);
        add(new IdentifierLabel("identifierLabel"));
        
        TextField<String> rfidNumber;
        TextField<String> identifier;
        TextField<String> referenceNumber;
        add(rfidNumber = new TextField<String>("rfidNumber"));
        add(identifier = new TextField<String>("identifier"));
        add(referenceNumber = new TextField<String>("referenceNumber"));
        
        rfidNumber.add(createFieldUpdatingBehavior());
        identifier.add(createFieldUpdatingBehavior());
        referenceNumber.add(createFieldUpdatingBehavior());
        
    }
	
	private AjaxFormComponentUpdatingBehavior createFieldUpdatingBehavior() {
		return new AjaxFormComponentUpdatingBehavior("onblur"){

			@Override
			protected void onUpdate(AjaxRequestTarget target) {}
        	
        };
	}

}
