package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.OopsPage;
import com.n4systems.fieldid.wicket.pages.admin.tenants.AddTenantPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.RunLastSearchPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.RunSavedSearchPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchPage;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateAssetsPage;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateEventsPage;
import com.n4systems.fieldid.wicket.pages.reporting.*;
import com.n4systems.fieldid.wicket.pages.saveditems.EditSavedItemPage;
import com.n4systems.fieldid.wicket.pages.saveditems.ManageSavedItemsPage;
import com.n4systems.fieldid.wicket.pages.saveditems.SavedItemsDropdownPage;
import com.n4systems.fieldid.wicket.pages.saveditems.ShareSavedItemPage;
import com.n4systems.fieldid.wicket.pages.saveditems.send.ManageSendItemSchedulesPage;
import com.n4systems.fieldid.wicket.pages.saveditems.send.SendSavedItemPage;
import com.n4systems.fieldid.wicket.pages.setup.*;
import com.n4systems.fieldid.wicket.pages.setup.columnlayout.ColumnsLayoutPage;
import com.n4systems.fieldid.wicket.pages.setup.eventform.EventFormEditPage;
import com.n4systems.fieldid.wicket.pages.setup.score.ScoreGroupsPage;
import com.n4systems.fieldid.wicket.pages.setup.score.result.ScoreResultConfigurationPage;
import com.n4systems.fieldid.wicket.resources.CacheInSessionLocalizer;
import com.n4systems.fieldid.wicket.resources.CustomerLanguageResourceLoader;
import com.n4systems.fieldid.wicket.resources.TenantOverridesResourceLoader;
import com.n4systems.fieldid.wicket.util.PagePerformanceListener;
import com.n4systems.fieldid.wicket.util.PlainDateConverter;
import com.n4systems.model.utils.PlainDate;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
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

        //uggh: colorbox doesn't render absolute urls so we have to give it this url to make sense.  i.e. the "/wicket" prefix
        //  a preferred solution would be to put colorbox in iframe (which requires CSS love) or to force absolute urls to be rendered by wicket.
        mountPage("wicket/reporting2", ReportPage.class);
        
        mountPage("sendSavedItem", SendSavedItemPage.class);
        mountPage("manageSendItemSchedules", ManageSendItemSchedulesPage.class);

        mountPage("search2", SearchPage.class);
        mountPage("assetMassUpdate", MassUpdateAssetsPage.class);
        mountPage("eventMassUpdate", MassUpdateEventsPage.class);
        mountPage("returnToReport", ReturnToReportPage.class);

        mountPage("savedReport", RunSavedReportPage.class);
        mountPage("savedSearch", RunSavedSearchPage.class);
        mountPage("massSchedule", MassSchedulePage.class);
        mountPage("savedReportsDeprecated", SavedReportsDeprecatedPage.class);

        mountPage("savedItems", SavedItemsDropdownPage.class);
        mountPage("manageSavedItems", ManageSavedItemsPage.class);
        mountPage("shareSavedItem", ShareSavedItemPage.class);
        mountPage("editSavedItem", EditSavedItemPage.class);

        mountPage("runLastSearch", RunLastSearchPage.class);
        mountPage("runLastReport", RunLastReportPage.class);

        mountPage("admin/addTenant", AddTenantPage.class);
             
        getMarkupSettings().setStripWicketTags(true);
        getResourceSettings().getStringResourceLoaders().add(0, new CustomerLanguageResourceLoader());
        getResourceSettings().getStringResourceLoaders().add(0, new TenantOverridesResourceLoader());
        getResourceSettings().setLocalizer(new CacheInSessionLocalizer());
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));

        getApplicationSettings().setPageExpiredErrorPage(DashboardPage.class);
        getApplicationSettings().setInternalErrorPage(OopsPage.class);

        getRequestCycleListeners().add(new FieldIDRequestCycleListener());
        // use -Dpagetime to put JVM in permanent "render page time in debug window" mode.
        getComponentInitializationListeners().add(new PagePerformanceListener(System.getProperty("pagetime")!=null));
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
    protected IConverterLocator newConverterLocator() {
    	ConverterLocator converterLocator = new ConverterLocator();
    	converterLocator.set(PlainDate.class, new PlainDateConverter());
    	return converterLocator;
    }


}
