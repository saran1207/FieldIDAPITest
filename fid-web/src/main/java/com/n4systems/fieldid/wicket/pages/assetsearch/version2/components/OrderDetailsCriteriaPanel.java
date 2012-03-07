package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;

public class OrderDetailsCriteriaPanel extends Panel {

    public OrderDetailsCriteriaPanel(String id) {
        super(id);

        SystemSecurityGuard securityGuard = FieldIDSession.get().getSecurityGuard();
        TextField<String> purchaseOrder;
        TextField<String> orderNumber;
        add(new TextField<String>("purchaseOrder"));
	 	add(new TextField<String>("orderNumber"));        
        setVisible(securityGuard.isOrderDetailsEnabled() || securityGuard.isIntegrationEnabled());
    }

}

