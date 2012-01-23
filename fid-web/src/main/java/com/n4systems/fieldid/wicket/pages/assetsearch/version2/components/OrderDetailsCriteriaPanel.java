package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

public class OrderDetailsCriteriaPanel extends Panel {

    public OrderDetailsCriteriaPanel(String id) {
        super(id);

        SystemSecurityGuard securityGuard = FieldIDSession.get().getSecurityGuard();

        add(new TextField<String>("purchaseOrder"));
        add(new TextField<String>("orderNumber"));
        setVisible(securityGuard.isOrderDetailsEnabled() || securityGuard.isIntegrationEnabled());
    }

}
