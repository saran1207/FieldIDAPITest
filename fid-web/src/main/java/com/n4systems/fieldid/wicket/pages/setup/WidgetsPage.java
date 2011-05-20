package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.model.Tenant;
import com.n4systems.util.uri.ActionURLBuilder;
import org.apache.wicket.markup.html.basic.Label;

import java.net.URI;

public class WidgetsPage extends SetupPage {

    public WidgetsPage() {
        add(new Label("loginUrl", getLoginUrl()).setRenderBodyOnly(true));
        add(new Label("embeddedLoginUrl", getEmbeddedLoginUrl()).setRenderBodyOnly(true));
    }

    public String getLoginUrl() {
		return createActionURI(getTenant(), "login");
	}

	public String getEmbeddedLoginUrl() {
		return createActionURI(getTenant(), "embedded/login");
	}

	public String createActionURI(Tenant tenant, String action) {
		return createActionUrlBuilder().setAction(action).setCompany(tenant).build();
	}

    protected ActionURLBuilder createActionUrlBuilder() {
        return new ActionURLBuilder(getBaseURI(), getConfigContext());
    }

	public URI getBaseURI() {
		// creates a URI based on the current url, and resolved against the context path which should be /fieldid.  We add on the extra / since we currently need it.
		return URI.create(getServletRequest().getRequestURL().toString()).resolve(getServletRequest().getContextPath() + "/");
	}

}
