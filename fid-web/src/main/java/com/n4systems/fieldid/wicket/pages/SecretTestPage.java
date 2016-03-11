package com.n4systems.fieldid.wicket.pages;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;

public class SecretTestPage extends FieldIDAuthenticatedPage {

    public SecretTestPage() {
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
        response.renderCSSReference("style/legacy/reset.css");
        response.renderCSSReference("style/legacy/site_wide.css");
        response.renderCSSReference("style/legacy/fieldid.css");
    }

}
