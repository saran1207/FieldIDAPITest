package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.wicket.components.action.ViewActionsListPage;
import com.n4systems.fieldid.wicket.components.event.criteria.signature.resource.SignatureResourceReference;
import com.n4systems.fieldid.wicket.components.event.criteria.signature.resource.TemporarySignatureResourceReference;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.OopsPage;
import com.n4systems.fieldid.wicket.pages.SecretTestPage;
import com.n4systems.fieldid.wicket.pages.SelectLanguagePage;
import com.n4systems.fieldid.wicket.pages.admin.connections.ConnectionViewPage;
import com.n4systems.fieldid.wicket.pages.admin.languages.ConfigureLanguagesPage;
import com.n4systems.fieldid.wicket.pages.admin.tenants.AddTenantPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.*;
import com.n4systems.fieldid.wicket.pages.event.*;
import com.n4systems.fieldid.wicket.pages.event.criteriaimage.CriteriaImageViewListPage;
import com.n4systems.fieldid.wicket.pages.identify.IdentifyOrEditAssetPage;
import com.n4systems.fieldid.wicket.pages.identify.LimitedEditAsset;
import com.n4systems.fieldid.wicket.pages.loto.PreviouslyPublishedListPage;
import com.n4systems.fieldid.wicket.pages.loto.ProcedureDefinitionListPage;
import com.n4systems.fieldid.wicket.pages.loto.ProcedureResultsPage;
import com.n4systems.fieldid.wicket.pages.loto.ProceduresListPage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPrintPage;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateAssetsPage;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateEventsPage;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateOpenEventsPage;
import com.n4systems.fieldid.wicket.pages.org.PlaceEventTypesPage;
import com.n4systems.fieldid.wicket.pages.org.PlaceEventsPage;
import com.n4systems.fieldid.wicket.pages.org.PlacePeoplePage;
import com.n4systems.fieldid.wicket.pages.org.PlaceSummaryPage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.fieldid.wicket.pages.reporting.RunLastReportPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunReportPage;
import com.n4systems.fieldid.wicket.pages.saveditems.EditSavedItemPage;
import com.n4systems.fieldid.wicket.pages.saveditems.ManageSavedItemsPage;
import com.n4systems.fieldid.wicket.pages.saveditems.SavedItemsDropdownPage;
import com.n4systems.fieldid.wicket.pages.saveditems.ShareSavedItemPage;
import com.n4systems.fieldid.wicket.pages.saveditems.send.ManageSendItemSchedulesPage;
import com.n4systems.fieldid.wicket.pages.saveditems.send.SendSavedItemPage;
import com.n4systems.fieldid.wicket.pages.search.AdvancedAssetSearchPage;
import com.n4systems.fieldid.wicket.pages.search.AdvancedEventSearchPage;
import com.n4systems.fieldid.wicket.pages.setup.*;
import com.n4systems.fieldid.wicket.pages.setup.assettype.*;
import com.n4systems.fieldid.wicket.pages.setup.columnlayout.ColumnsLayoutPage;
import com.n4systems.fieldid.wicket.pages.setup.eventform.EventFormEditPage;
import com.n4systems.fieldid.wicket.pages.setup.eventstatus.EventStatusArchivedListPage;
import com.n4systems.fieldid.wicket.pages.setup.eventstatus.EventStatusFormPage;
import com.n4systems.fieldid.wicket.pages.setup.eventstatus.EventStatusListPage;
import com.n4systems.fieldid.wicket.pages.setup.eventtypegroup.ReassignEventTypeGroupPage;
import com.n4systems.fieldid.wicket.pages.setup.org.OrgViewPage;
import com.n4systems.fieldid.wicket.pages.setup.prioritycode.PriorityCodePage;
import com.n4systems.fieldid.wicket.pages.setup.score.ScoreGroupsPage;
import com.n4systems.fieldid.wicket.pages.setup.score.result.ScoreResultConfigurationPage;
import com.n4systems.fieldid.wicket.pages.setup.translations.*;
import com.n4systems.fieldid.wicket.pages.setup.user.ArchiveUserGroupPage;
import com.n4systems.fieldid.wicket.pages.setup.user.UserGroupsPage;
import com.n4systems.fieldid.wicket.pages.template.*;
import com.n4systems.fieldid.wicket.pages.trends.CriteriaTrendsPage;
import com.n4systems.fieldid.wicket.pages.user.*;
import com.n4systems.fieldid.wicket.pages.useraccount.MobileOfflineProfilePage;
import com.n4systems.fieldid.wicket.pages.useraccount.UserAccountSearchPage;
import com.n4systems.fieldid.wicket.resources.CacheInSessionLocalizer;
import com.n4systems.fieldid.wicket.resources.CustomerLanguageResourceLoader;
import com.n4systems.fieldid.wicket.resources.TenantOverridesResourceLoader;
import com.n4systems.fieldid.wicket.util.PagePerformanceListener;
import com.n4systems.fieldid.wicket.util.PlainDateConverter;
import com.n4systems.model.utils.PlainDate;
import org.apache.wicket.*;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.caching.QueryStringWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.MessageDigestResourceVersion;
import org.apache.wicket.settings.IRequestLoggerSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.file.IResourceFinder;

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
        mountPage("setup/priorityCodes", PriorityCodePage.class);
        mountPage("setup/reassignEventTypeGroups", ReassignEventTypeGroupPage.class);
        mountPage("setup/userGroups", UserGroupsPage.class);
        mountPage("setup/archiveUserGroup", ArchiveUserGroupPage.class);
        mountPage("setup/assetTypes", AssetTypeListPage.class);
        mountPage("setup/assetTypeForm", AddAssetTypePage.class);
        mountPage("setup/assetTypeEdit", EditAssetTypePage.class);
        mountPage("setup/assetTypeCopy", CopyAssetTypePage.class);
        mountPage("setup/assetTypeDelete", ConfirmDeleteAssetTypePage.class);
        mountPage("setup/selectEventTypes", EventTypeAssociationsPage.class);
        mountPage("setup/assetTypeSchedules", AssetTypeSchedulesPage.class);
        mountPage("setup/assetTypeGroupTranslations", AssetTypeGroupTranslationsPage.class);
        mountPage("setup/assetTypeTranslations", AssetTypeTranslationsPage.class);
        mountPage("setup/eventTypeTranslations", EventTypeTranslationsPage.class);
        mountPage("setup/eventTypeGroupTranslations", EventTypeGroupTranslationsPage.class);
        mountPage("setup/eventBookTranslations", EventBookTranslationsPage.class);
        mountPage("setup/languageConfiguration", LanguageConfigurationPage.class);
        mountPage("places", OrgViewPage.class);
        mountPage("placeSummary", PlaceSummaryPage.class);
        mountPage("placeEvents", PlaceEventsPage.class);
        mountPage("placePeople", PlacePeoplePage.class);
        mountPage("placeEventTypes", PlaceEventTypesPage.class);

        mountPage("dashboard", DashboardPage.class);

        //uggh: colorbox doesn't render absolute urls so we have to give it this url to make sense.  i.e. the "/wicket" prefix
        //  a preferred solution would be to put colorbox in iframe (which requires CSS love) or to force absolute urls to be rendered by wicket.
        mountPage("reporting", ReportPage.class);
        mountPage("procedure", ProcedureSearchPage.class);

        mountPage("advancedAssetSearch", AdvancedAssetSearchPage.class);
        mountPage("advancedEventSearch", AdvancedEventSearchPage.class);

        mountPage("procedureDef", ProcedureDefinitionPage.class);

        mountPage("quickEvent", QuickEventPage.class);
        mountPage("startEvent", StartRegularOrMasterEventPage.class);
        mountPage("performEvent", PerformEventPage.class);
        mountPage("editEvent", EditEventPage.class);

        mountPage("closeEvent", CloseEventPage.class);

        mountPage("sendSavedItem", SendSavedItemPage.class);
        mountPage("manageSendItemSchedules", ManageSendItemSchedulesPage.class);

        mountPage("recurringEvents", RecurringAssetTypeEventsPage.class);
        mountPage("search", SearchPage.class);
        mountPage("assetMassUpdate", MassUpdateAssetsPage.class);
        mountPage("eventMassUpdate", MassUpdateEventsPage.class);
        mountPage("openEventMassUpdate", MassUpdateOpenEventsPage.class);

        mountPage("savedReport", RunReportPage.class);
        mountPage("savedSearch", RunSearchPage.class);
        mountPage("massSchedule", MassSchedulePage.class);

        mountPage("savedItems", SavedItemsDropdownPage.class);
        mountPage("manageSavedItems", ManageSavedItemsPage.class);
        mountPage("shareSavedItem", ShareSavedItemPage.class);
        mountPage("editSavedItem", EditSavedItemPage.class);

        mountPage("runLastSearch", RunLastSearchPage.class);
        mountPage("runLastReport", RunLastReportPage.class);

        mountPage("admin/addTenant", AddTenantPage.class);
		mountPage("admin/connections", ConnectionViewPage.class);
        mountPage("admin/configureLanguages", ConfigureLanguagesPage.class);

        mountPage("secret/test", SecretTestPage.class);
        mountPage("assetSummary", AssetSummaryPage.class);
        mountPage("assetEvents", AssetEventsPage.class);

        mountPage("eventStatusList", EventStatusListPage.class);
        mountPage("eventStatusArchivedList", EventStatusArchivedListPage.class);
        mountPage("eventStatusForm", EventStatusFormPage.class);

        mountPage("criteriaImageList", CriteriaImageViewListPage.class);

        mountPage("viewActionsList", ViewActionsListPage.class);

        mountPage("searchOpenEventsForJob", SearchOpenEventsForJobPage.class);

        mountPage("addPerson", AddPersonPage.class);
        mountPage("editPerson", EditPersonPage.class);
        mountPage("addUsageBasedUser", AddUsageBasedUserPage.class);
        mountPage("editUsageBasedUser", EditUsageBasedUserPage.class);

        mountPage("procedureDefinitions", ProcedureDefinitionListPage.class);
        mountPage("procedureDefinitionPrint", ProcedureDefinitionPrintPage.class);
        mountPage("previouslyPublished", PreviouslyPublishedListPage.class);
        mountPage("completedProcedures", ProceduresListPage.class);
        mountPage("procedureResults", ProcedureResultsPage.class);
        mountPage("identify", IdentifyOrEditAssetPage.class);

        mountPage("limitedEditAsset", LimitedEditAsset.class);

        mountPage("criteriaTrends", CriteriaTrendsPage.class);

        mountPage("selectLanguage", SelectLanguagePage.class);

        mountPage("mobileOfflineProfile", MobileOfflineProfilePage.class);
        mountPage("userOfflineProfile", UserOfflineProfilePage.class);

        mountPage("userAccountSearch", UserAccountSearchPage.class);

        mountPage("template", TemplatePage.class);
        mountPage("template/assetSummary", TemplateAssetSummaryPage.class);
        mountPage("template/twoColumnLeft", TwoColumnLeft.class);
        mountPage("template/twoColumnRight", TwoColumnRight.class);
        mountPage("template/noColumns", FirstTab.class);
        mountPage("template/noColumnsTab", SecondTab.class);

        mountPage("performPlaceEvent", PlaceEventPage.class);

        mountResource("/signature/${eventId}/${criteriaId}", new SignatureResourceReference());
        mountResource("/temporarySignature/${fileId}", new TemporarySignatureResourceReference());

        // TODO : this is a possible performance gain but would require full regression.
        // getMarkupSettings().setCompressWhitespace(true);

        getMarkupSettings().setStripWicketTags(true);
        getResourceSettings().getStringResourceLoaders().add(0, new CustomerLanguageResourceLoader());
        getResourceSettings().getStringResourceLoaders().add(0, new TenantOverridesResourceLoader());
        getResourceSettings().setLocalizer(new CacheInSessionLocalizer());
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));

        getApplicationSettings().setPageExpiredErrorPage(DashboardPage.class);
        getApplicationSettings().setInternalErrorPage(OopsPage.class);
        getApplicationSettings().setUploadProgressUpdatesEnabled(true);

        getRequestCycleListeners().add(new FieldIDRequestCycleListener());
        if (getConfigurationType().equals(RuntimeConfigurationType.DEVELOPMENT)) {
            getComponentInitializationListeners().add(new PagePerformanceListener());

            IRequestLoggerSettings reqLogger = getRequestLoggerSettings();
            reqLogger.setRequestLoggerEnabled(true);
            reqLogger.setRequestsWindowSize(500);
        }

        getResourceSettings().setCachingStrategy(new QueryStringWithVersionResourceCachingStrategy(new MessageDigestResourceVersion()));

    }

    private String getTemplateFolder() {
        return "/var/fieldid/common/templates/wicket";
    }

    @Override
    protected void validateInit() {
        super.validateInit();
        setHeaderResponseDecorator(CachingStrategyDecoratingHeaderResponse.createHeaderResponseDecorator());
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


    @Override
    protected IResourceFinder getResourceFinder() {
        return new DynamicWebApplicationPath(getServletContext());
    }


}
