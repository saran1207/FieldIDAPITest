package com.n4systems.fieldid.wicket.pages.setup;

import org.apache.wicket.markup.html.WebMarkupContainer;

public class TemplatesPage extends SetupPage {

    public TemplatesPage() {
        add(new WebMarkupContainer("assetCodeMappingContainer").setVisible(getSecurityGuard().isIntegrationEnabled()));
    }

}
