package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.OopsPage;
import com.n4systems.fieldid.wicket.pages.admin.tenants.AddTenantPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingPage;
import com.n4systems.fieldid.wicket.pages.reporting.ReturnToReportPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunSavedReportPage;
import com.n4systems.fieldid.wicket.pages.setup.AccountPolicyPage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.fieldid.wicket.pages.setup.IdentifierOverridesPage;
import com.n4systems.fieldid.wicket.pages.setup.ImportPage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.fieldid.wicket.pages.setup.PasswordPolicyPage;
import com.n4systems.fieldid.wicket.pages.setup.SecurityPage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.fieldid.wicket.pages.setup.SystemSettingsPage;
import com.n4systems.fieldid.wicket.pages.setup.TemplatesPage;
import com.n4systems.fieldid.wicket.pages.setup.WidgetsPage;
import com.n4systems.fieldid.wicket.pages.setup.YourPlanPage;
import com.n4systems.fieldid.wicket.pages.setup.columnlayout.ColumnsLayoutPage;
import com.n4systems.fieldid.wicket.pages.setup.eventform.EventFormEditPage;
import com.n4systems.fieldid.wicket.pages.setup.score.ScoreGroupsPage;
import com.n4systems.fieldid.wicket.pages.setup.score.result.ScoreResultConfigurationPage;
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
import org.odlabs.wiquery.core.commons.IWiQuerySettings;
import org.odlabs.wiquery.core.commons.WiQuerySettings;

public class FieldIDWicketApp extends WebApplication implements IWiQuerySettings {

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
        mountBookmarkablePage("setup/systemSettings", SystemSettingsPage.class);
        mountBookmarkablePage("setup/yourPlan", YourPlanPage.class);        
        mountBookmarkablePage("setup/identifierOverrides", IdentifierOverridesPage.class);
        mountBookmarkablePage("setup/passwordPolicy", PasswordPolicyPage.class);
        mountBookmarkablePage("setup/accountPolicy", AccountPolicyPage.class);
        mountBookmarkablePage("setup/security", SecurityPage.class);
        mountBookmarkablePage("setup/scoreGroups", ScoreGroupsPage.class);
        mountBookmarkablePage("setup/scoreResults", ScoreResultConfigurationPage.class);

        mountBookmarkablePage("dashboard", DashboardPage.class);
        mountBookmarkablePage("reporting", ReportingPage.class);
        mountBookmarkablePage("returnToReport", ReturnToReportPage.class);
        mountBookmarkablePage("savedReport", RunSavedReportPage.class);

        mountBookmarkablePage("massSchedule", MassSchedulePage.class);

        mountBookmarkablePage("search", AssetSearchPage.class);
        mountBookmarkablePage("searchResults", AssetSearchResultsPage.class);

        mountBookmarkablePage("admin/addTenant", AddTenantPage.class);
             
        getMarkupSettings().setStripWicketTags(true);
        getResourceSettings().addStringResourceLoader(0, new CustomerLanguageResourceLoader());
        getResourceSettings().addStringResourceLoader(0, new TenantOverridesResourceLoader());
        getResourceSettings().setLocalizer(new CacheInSessionLocalizer());
        InjectorHolder.setInjector(new AnnotSpringInjector(new SpringContextLocator()));
        addComponentInstantiationListener(new SpringComponentInjector(this));

        getApplicationSettings().setPageExpiredErrorPage(DashboardPage.class);
        getApplicationSettings().setInternalErrorPage(OopsPage.class);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return DashboardPage.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new FieldIDSession(request);
    }

    @Override
    public RequestCycle newRequestCycle(Request request, Response response) {
        return new FieldIDRequestCycle(this, (WebRequest) request, response);
    }

	@Override
	public WiQuerySettings getWiQuerySettings() {
		WiQuerySettings wiQuerySettings = new WiQuerySettings();
		// we do *NOT* want jquery imported again.  that is already done by base page.
		wiQuerySettings.setAutoImportJQueryResource(false);
		return wiQuerySettings;
	}

}
