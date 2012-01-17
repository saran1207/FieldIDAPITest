package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.OopsPage;
import com.n4systems.fieldid.wicket.pages.admin.tenants.AddTenantPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchPage;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdatePage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingPage;
import com.n4systems.fieldid.wicket.pages.reporting.ReturnToReportPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunSavedReportPage;
import com.n4systems.fieldid.wicket.pages.saveditems.SavedItemsPage;
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
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

public class FieldIDWicketApp extends WebApplication {

    @Override
    protected void init() {
        mountPage("setup/eventFormEdit", EventFormEditPage.class);
        mountPage("setup/columnsLayout", ColumnsLayoutPage.class);

        mountPage("setup/settings", SettingsPage.class);
        mountPage("setup/ownersUsersLocations", OwnersUsersLocationsPage.class);
        mountPage("setup/assetsEvents", AssetsAndEventsPage.class);
        mountPage("setup/import", ImportPage.class);
        mountPage("setup/templates", TemplatesPage.class);
        mountPage("setup/widgets", WidgetsPage.class);
        mountPage("setup/systemSettings", SystemSettingsPage.class);
        mountPage("setup/yourPlan", YourPlanPage.class);
        mountPage("setup/identifierOverrides", IdentifierOverridesPage.class);
        mountPage("setup/passwordPolicy", PasswordPolicyPage.class);
        mountPage("setup/accountPolicy", AccountPolicyPage.class);
        mountPage("setup/security", SecurityPage.class);
        mountPage("setup/scoreGroups", ScoreGroupsPage.class);
        mountPage("setup/scoreResults", ScoreResultConfigurationPage.class);

        mountPage("dashboard", DashboardPage.class);
        mountPage("reporting", ReportingPage.class);
        mountPage("search", AssetSearchPage.class);
        mountPage("massupdate", MassUpdatePage.class);

        mountPage("returnToReport", ReturnToReportPage.class);
        mountPage("savedReport", RunSavedReportPage.class);
        mountPage("massSchedule", MassSchedulePage.class);

        mountPage("savedItems", SavedItemsPage.class);

        mountPage("admin/addTenant", AddTenantPage.class);
             
        getMarkupSettings().setStripWicketTags(true);
        getResourceSettings().getStringResourceLoaders().add(0, new CustomerLanguageResourceLoader());
        getResourceSettings().getStringResourceLoaders().add(0, new TenantOverridesResourceLoader());
        getResourceSettings().setLocalizer(new CacheInSessionLocalizer());
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));

        getApplicationSettings().setPageExpiredErrorPage(DashboardPage.class);
        getApplicationSettings().setInternalErrorPage(OopsPage.class);

        getRequestCycleListeners().add(new FieldIDRequestCycleListener());
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return DashboardPage.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new FieldIDSession(request);
    }

}
