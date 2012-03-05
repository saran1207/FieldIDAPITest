package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

public class OrderDetailsCriteriaPanel extends Panel {

    public OrderDetailsCriteriaPanel(String id) {
        super(id);

        SystemSecurityGuard securityGuard = FieldIDSession.get().getSecurityGuard();
        TextField<String> purchaseOrder;
        TextField<String> orderNumber;
        add(purchaseOrder = new TextField<String>("purchaseOrder"));
        add(orderNumber = new TextField<String>("orderNumber"));
        purchaseOrder.add(createFieldUpdatingBehavior());
        orderNumber.add(createFieldUpdatingBehavior());
        
        setVisible(securityGuard.isOrderDetailsEnabled() || securityGuard.isIntegrationEnabled());
    }

    private AjaxFormComponentUpdatingBehavior createFieldUpdatingBehavior() {
    	return new AjaxFormComponentUpdatingBehavior("onblur"){

    		@Override
    		protected void onUpdate(AjaxRequestTarget target) {}
        	
        };
    }
}

