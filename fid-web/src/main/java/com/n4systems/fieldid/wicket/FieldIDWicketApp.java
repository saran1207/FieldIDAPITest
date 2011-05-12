package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.wicket.pages.setup.columnlayout.ColumnsLayoutPage;
import com.n4systems.fieldid.wicket.pages.setup.eventform.EventFormEditPage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.fieldid.wicket.pages.setup.TemplatesPage;
import com.n4systems.fieldid.wicket.resources.CachePerTenantLocalizer;
import com.n4systems.fieldid.wicket.resources.CustomerLanguageResourceLoader;
import com.n4systems.fieldid.wicket.resources.TenantOverridesResourceLoader;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;

public class FieldIDWicketApp extends WebApplication {

    @Override
    protected void init() {
        mountBookmarkablePage("setup/eventFormEdit", EventFormEditPage.class);
        mountBookmarkablePage("setup/columnsLayout", ColumnsLayoutPage.class);

        mountBookmarkablePage("setup/settings", SettingsPage.class);
        mountBookmarkablePage("setup/ownersUsersLocations", OwnersUsersLocationsPage.class);
        mountBookmarkablePage("setup/assetsEvents", AssetsAndEventsPage.class);
        mountBookmarkablePage("setup/templates", TemplatesPage.class);

        getMarkupSettings().setStripWicketTags(true);
        getResourceSettings().addStringResourceLoader(0, new CustomerLanguageResourceLoader());
        getResourceSettings().addStringResourceLoader(0, new TenantOverridesResourceLoader());
        getResourceSettings().setLocalizer(new CachePerTenantLocalizer());
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return EventFormEditPage.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new FieldIDSession(request);
    }

    @Override
    public RequestCycle newRequestCycle(Request request, Response response) {
        return new FieldIDRequestCycle(this, (WebRequest) request, response);
    }

}
