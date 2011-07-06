package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.wicket.pages.HomePage;
import com.n4systems.fieldid.wicket.pages.reporting.ReturnToReportPage;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunSavedReportPage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.fieldid.wicket.pages.setup.DataLogPage;
import com.n4systems.fieldid.wicket.pages.setup.ImportPage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.fieldid.wicket.pages.setup.TemplatesPage;
import com.n4systems.fieldid.wicket.pages.setup.WidgetsPage;
import com.n4systems.fieldid.wicket.pages.setup.columnlayout.ColumnsLayoutPage;
import com.n4systems.fieldid.wicket.pages.setup.eventform.EventFormEditPage;
import com.n4systems.fieldid.wicket.resources.CacheInSessionLocalizer;
import com.n4systems.fieldid.wicket.resources.CustomerLanguageResourceLoader;
import com.n4systems.fieldid.wicket.resources.TenantOverridesResourceLoader;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.spring.injection.annot.AnnotSpringInjector;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

public class FieldIDWicketApp extends WebApplication {

    @Override
    protected void init() {
        mountBookmarkablePage("setup/eventFormEdit", EventFormEditPage.class);
        mountBookmarkablePage("setup/columnsLayout", ColumnsLayoutPage.class);

        mountBookmarkablePage("setup/settings", SettingsPage.class);
        mountBookmarkablePage("setup/ownersUsersLocations", OwnersUsersLocationsPage.class);
        mountBookmarkablePage("setup/assetsEvents", AssetsAndEventsPage.class);
        mountBookmarkablePage("setup/import", ImportPage.class);
        mountBookmarkablePage("setup/templates", TemplatesPage.class);
        mountBookmarkablePage("setup/widgets", WidgetsPage.class);
        mountBookmarkablePage("setup/dataLog", DataLogPage.class);

        mountBookmarkablePage("reporting", ReportingPage.class);
        mountBookmarkablePage("returnToReport", ReturnToReportPage.class);
        mountBookmarkablePage("savedReport", RunSavedReportPage.class);

        getMarkupSettings().setStripWicketTags(true);
        getResourceSettings().addStringResourceLoader(0, new CustomerLanguageResourceLoader());
        getResourceSettings().addStringResourceLoader(0, new TenantOverridesResourceLoader());
        getResourceSettings().setLocalizer(new CacheInSessionLocalizer());
        InjectorHolder.setInjector(new AnnotSpringInjector(new SpringContextLocator()));
        addComponentInstantiationListener(new SpringComponentInjector(this));

        getApplicationSettings().setPageExpiredErrorPage(HomePage.class);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
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
