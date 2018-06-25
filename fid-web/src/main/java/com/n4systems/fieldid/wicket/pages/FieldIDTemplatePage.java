package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.UIConstants;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.version.FieldIdVersion;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.CachingStrategyLink;
import com.n4systems.fieldid.wicket.components.CustomJavascriptPanel;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.asset.AutoCompleteSmartSearch;
import com.n4systems.fieldid.wicket.components.feedback.TopFeedbackPanel;
import com.n4systems.fieldid.wicket.components.localization.SelectLanguagePanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.saveditems.SavedItemsDropdown;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetImportPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.ProcedureSearchPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.ReportPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.SearchPage;
import com.n4systems.fieldid.wicket.pages.event.EventImportPage;
import com.n4systems.fieldid.wicket.pages.event.StartEventPage;
import com.n4systems.fieldid.wicket.pages.identify.IdentifyOrEditAssetPage;
import com.n4systems.fieldid.wicket.pages.loto.ProcedureAuditListPage;
import com.n4systems.fieldid.wicket.pages.loto.ProcedureWaitingApprovalsPage;
import com.n4systems.fieldid.wicket.pages.loto.PublishedListAllPage;
import com.n4systems.fieldid.wicket.pages.org.OrgViewPage;
import com.n4systems.fieldid.wicket.pages.search.AdvancedEventSearchPage;
import com.n4systems.fieldid.wicket.pages.search.SmartSearchListPage;
import com.n4systems.fieldid.wicket.pages.setup.*;
import com.n4systems.fieldid.wicket.pages.setup.actionemailcustomization.ActionEmailSetupPage;
import com.n4systems.fieldid.wicket.pages.setup.assetstatus.AssetStatusListAllPage;
import com.n4systems.fieldid.wicket.pages.setup.assettype.AssetTypeListPage;
import com.n4systems.fieldid.wicket.pages.setup.assettypegroup.AssetTypeGroupListPage;
import com.n4systems.fieldid.wicket.pages.setup.columnlayout.ColumnsLayoutPage;
import com.n4systems.fieldid.wicket.pages.setup.comment.CommentTemplateListPage;
import com.n4systems.fieldid.wicket.pages.setup.eventbook.EventBooksListAllPage;
import com.n4systems.fieldid.wicket.pages.setup.eventstatus.EventStatusListPage;
import com.n4systems.fieldid.wicket.pages.setup.eventtypegroup.EventTypeGroupListPage;
import com.n4systems.fieldid.wicket.pages.setup.loto.EnableByAssetTypePage;
import com.n4systems.fieldid.wicket.pages.setup.loto.LotoSetupPage;
import com.n4systems.fieldid.wicket.pages.setup.loto.PrintoutTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.loto.ProcedureApproverPage;
import com.n4systems.fieldid.wicket.pages.setup.prioritycode.PriorityCodePage;
import com.n4systems.fieldid.wicket.pages.setup.security.AccountPolicyPage;
import com.n4systems.fieldid.wicket.pages.setup.security.PasswordPolicyPage;
import com.n4systems.fieldid.wicket.pages.setup.translations.AssetTypeGroupTranslationsPage;
import com.n4systems.fieldid.wicket.pages.setup.user.UserGroupsPage;
import com.n4systems.fieldid.wicket.pages.setup.user.UsersListPage;
import com.n4systems.fieldid.wicket.pages.setup.userregistration.UserRequestListPage;
import com.n4systems.fieldid.wicket.pages.trends.CriteriaTrendsPage;
import com.n4systems.model.Asset;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.columns.ReportType;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.uri.ActionURLBuilder;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.UrlEncoder;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplate;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;
import rfid.web.helper.SessionUser;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;

@SuppressWarnings("serial")
public class FieldIDTemplatePage extends FieldIDAuthenticatedPage implements UIConstants {

    @SpringBean
	protected ConfigService configService;

	@SpringBean
	private UserLimitService userLimitService;

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private OrgService orgService;

    private Asset smartSearchAsset;
    protected Component titleLabel;
	protected Component topTitleLabel;
    private TopFeedbackPanel topFeedbackPanel;
    private Component languageSelectionLink;
    private ModalWindow languageSelectionModalWindow;
    private final SelectLanguagePanel selectLanguagePanel;
    protected boolean showTitle = true;
    private Boolean googleTranslateEnabled = null;

    public FieldIDTemplatePage() {
        this(null);
    }

    public FieldIDTemplatePage(PageParameters params) {
        super(params);
        storePageParameters(params);

        add(languageSelectionModalWindow = new DialogModalWindow("languageSelectionModalWindow").setInitialWidth(480).setInitialHeight(280));

        languageSelectionModalWindow.setContent(selectLanguagePanel = new SelectLanguagePanel(languageSelectionModalWindow.getContentId()) {
            @Override
            public void onLanguageSelection(AjaxRequestTarget target) {
                languageSelectionModalWindow.close(target);
                setResponsePage(getPageClass(), getPageParameters());
            }
        });
        languageSelectionModalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                /* The google translate widget needs to be moved from the language preference dialog back to the
                   main page. */
                target.appendJavaScript("$('#google_translate_element').detach().prependTo('#google_translate_element_container');");
            }
        });

        add(new Header("mainHeader"));
        add(new DebugBar("debugBar"));
        add(new CustomJavascriptPanel("customJsPanel"));
        add(new GoogleAnalyticsContainer("googleAnalyticsScripts"));

        addCssContainers();

        add(new WebMarkupContainer("metaIE").add(new AttributeAppender("content", getMetaIE())));

        add(topFeedbackPanel = new TopFeedbackPanel("topFeedbackPanel"));
        add(new Label("versionLabel", getVersionLabelText()));

        String footerScript = configService.getString(ConfigEntry.FOOTER_SCRIPT, getTenantId());
        if (footerScript != null && !footerScript.isEmpty()) {
            add(new Label("footerScript", footerScript).setEscapeModelStrings(false));
        } else {
            add(new Label("footerScript").setVisible(false));
        }

        String walkmeScript = "<script type='text/javascript' id ='WALKME_INTEGRATION'>" +
                BASE_WALKME_SCRIPT.replace("${walkmeURL}",
                        configService.getConfig().getWeb().getWalkmeUrl()) +
                "</script>";

        String slasskJsInclude = "<script type='text/javascript' src='" + SLAASK_JS_URL + "'></script>";
        String slaaskJsInlineScript = "<script type='text/javascript'>" +
                SLAASK_JS_SCRIPT.replace("${UserName}",
                        getSessionUser().getUserID()).replace("${User_Email}", getSessionUser().getEmailAddress()) +
                "</script>";

        add(new Label("footer-js-container",
                walkmeScript + "\n\n" + slasskJsInclude + "\n" + slaaskJsInlineScript).
                setEscapeModelStrings(false));

        add(createHeaderLink("headerLink", "headerLinkLabel"));
        add(createRelogLink());
    }

    private void addCssContainers() {
        add(new CachingStrategyLink("globalCss"));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        // We add these components here rather than in the constructor
        // So subclasses that need to override them can finish their constructors before doing so
        // eg. Labels may require details of entities to be loaded.
        addNavBar("navBar");
        addBreadCrumbBar("breadCrumbBar");


        add(titleLabel = createTitleLabel("titleLabel"));
        titleLabel.setRenderBodyOnly(true);
        titleLabel.setVisible(isShowTitle());

        add(topTitleLabel = useTopTitleLabel() ? createTopTitleLabel("topTitleLabel") : createTitleLabel("topTitleLabel"));
        topTitleLabel.setRenderBodyOnly(true);

        add(createBackToLink("backToLink", "backToLinkLabel"));
        add(createSubHeader("subHeader"));

        add(createActionGroup("actionGroup"));
    }

    protected boolean forceDefaultLanguage() {
        // for now, all setup pages will be forced to english.
        boolean isSetupPage = getPageClass().getPackage().getName().startsWith("com.n4systems.fieldid.wicket.pages.setup");
        return isSetupPage;
    }

    private String getSupportUrl() {
    	TenantSettings settings = getTenant().getSettings();
		return settings.getSupportUrl()==null ? DEFAULT_SUPPORT_URL :settings.getSupportUrl();
	}

    protected IModel<String> getMetaIE() {
        return Model.of("IE=Edge");
    }

    private Component createRelogLink() {
        PageParameters pageParameters = new PageParameters();
        pageParameters.add("user", FieldIDSession.get().getSessionUser().getUserName());
        pageParameters.add("tenant", FieldIDSession.get().getSessionUser().getTenant().getName());
        return new NonWicketLink("relogLink", "aHtml/login.action");
    }

    protected void storePageParameters(PageParameters params) {
    }

    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, "Field ID");
    }

    protected Component createTopTitleLabel(String labelId) {
        return new Label(labelId, "Field ID");
    }

    protected boolean useTopTitleLabel() {
        return false;
    }

    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new WebMarkupContainer(linkId).setVisible(false);
    }
    
    protected Component createHeaderLink(String id, String label) {
		return new WebMarkupContainer(id).setVisible(false);
	}

    protected Component createSubHeader(String subHeaderId) {
        return new WebMarkupContainer(subHeaderId).setVisible(false);
    }

    protected Component createActionGroup(String actionGroupId) {
        return new WebMarkupContainer(actionGroupId).setVisible(false);
    }

    private Component createSetupLinkContainer(SessionUser sessionUser) {
        boolean hasSetupAccess = sessionUser.hasSetupAccess();
        boolean manageSystemConfig = sessionUser.hasAccess("managesystemconfig");
        WebMarkupContainer container = new WebMarkupContainer("setupLinkContainer");
        WebMarkupContainer subMenuContainer = new WebMarkupContainer("setupSubMenuContainer");
        if (hasSetupAccess && manageSystemConfig) {
            container.add(new BookmarkablePageLink<WebPage>("setupLink", SettingsPage.class));
        } else if (hasSetupAccess) {
            container.add(new BookmarkablePageLink<WebPage>("setupLink", OwnersUsersLocationsPage.class));
            subMenuContainer.setVisible(false);
        } else {
            container.setVisible(false);
            subMenuContainer.setVisible(false);
        }
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("settingsLink", SettingsPage.class));
        subMenuContainer.add(createSettingsSubMenu());
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("ownersUsersLocLink", OwnersUsersLocationsPage.class));
        subMenuContainer.add(createOwnersSubMenu());
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("assetsEventsLink", AssetsAndEventsPage.class));
        subMenuContainer.add(createAssetEventsSubMenu());
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("importLink", ImportPage.class));
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("assetImportLink", AssetImportPage.class));
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("eventImportLink", EventImportPage.class));
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("templatesLink", TemplatesPage.class));
        subMenuContainer.add(createTemplatesSubMenu());
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("widgetsLink", WidgetsPage.class));
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("securityLink", SecurityPage.class));
        subMenuContainer.add(createLotoSubMenu());
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("translationsLink", AssetTypeGroupTranslationsPage.class));
        subMenuContainer.add(createActionsSubMenu());
        createSecuritySubMenu(subMenuContainer);
        
        container.add(subMenuContainer);
        
        return container;
    }
    
	private void createSecuritySubMenu(WebMarkupContainer container) {
		container.add(new BookmarkablePageLink<ColumnsLayoutPage>("passwordPolicyLink", PasswordPolicyPage.class));
		container.add(new BookmarkablePageLink<ColumnsLayoutPage>("accountPolicyLink", AccountPolicyPage.class));

	}

	private Component createTemplatesSubMenu() {
        boolean intergrationEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.Integration);

        WebMarkupContainer container = new WebMarkupContainer("templatesSubMenuContainer");
        WebMarkupContainer assetCodeMappingcontainer = new WebMarkupContainer("assetCodeMappingContainer");

        container.setVisible(getSessionUser().hasAccess("managesystemconfig"));
        assetCodeMappingcontainer.setVisible(intergrationEnabled);
        container.add(assetCodeMappingcontainer);

        container.add(new BookmarkablePageLink("commentTemplateLink",
                                               CommentTemplateListPage.class));
        
        container.add(new BookmarkablePageLink<ColumnsLayoutPage>("assetLayoutLink", ColumnsLayoutPage.class, param("type", ReportType.ASSET)));
        container.add(new BookmarkablePageLink<ColumnsLayoutPage>("eventLayoutLink", ColumnsLayoutPage.class, param("type", ReportType.EVENT)).setVisible(getSecurityGuard().isInspectionsEnabled()));

        return container;
	}

	private Component createAssetEventsSubMenu() {
        WebMarkupContainer container = new WebMarkupContainer("assetsEventsSubMenuContainer");
        container.add(new BookmarkablePageLink("eventTypeGroupLink", EventTypeGroupListPage.class));
        container.add(new BookmarkablePageLink("eventStatusListLink", EventStatusListPage.class));
        container.add(new BookmarkablePageLink("eventBooksListLink", EventBooksListAllPage.class));
        container.add(new BookmarkablePageLink("assetTypeGroupsListLink", AssetTypeGroupListPage.class));
        container.add(new BookmarkablePageLink("assetTypesList", AssetTypeListPage.class));
        container.add(new BookmarkablePageLink("assetStatusList", AssetStatusListAllPage.class));
        container.setVisible(getSessionUser().hasAccess("managesystemconfig"));
        
        return container;
    }

	private Component createSettingsSubMenu() {
    	WebMarkupContainer container = new WebMarkupContainer("settingsSubMenuContainer");
    	
    	container.add(new BookmarkablePageLink<Void>("systemSettingsLink", SystemSettingsPage.class));
    	container.add(new BookmarkablePageLink<Void>("yourPlanLink", YourPlanPage.class));

    	return container;
    }

    private Component createSmartSearch(String id) {
        WebMarkupContainer container = new WebMarkupContainer("smartSearchContainer");

        AutoCompleteSmartSearch autoCompleteSearch = (AutoCompleteSmartSearch) new AutoCompleteSmartSearch("autocompletesearch", new PropertyModel<Asset>(this, "smartSearchAsset")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target, String hiddenInput, String fieldInput) {
                if(!hiddenInput.equals("")) {
                    setResponsePage(AssetSummaryPage.class, PageParametersBuilder.uniqueId(Long.valueOf(hiddenInput)));
                }
            }
        }.withAutoUpdate(true);

        autoCompleteSearch.getAutocompleteField().setMarkupId("searchText");

        Form<?> form = new Form<Void>("userForm") {
            @Override
            protected void onSubmit() {
                String convertedInput = autoCompleteSearch.getAutocompleteField().getConvertedInput();
                if (convertedInput != null)
                    setResponsePage(SmartSearchListPage.class, PageParametersBuilder.param("searchTerm", convertedInput));
            }
        };

        container.add(form);

        form.add(autoCompleteSearch);
        return container;
    }

    private Component createOwnersSubMenu() {
    	WebMarkupContainer container = new WebMarkupContainer("ownersSubMenuContainer");
    	
	    boolean canManageSystemUsers = getSessionUser().hasAccess("managesystemusers");
        boolean advancedLocationEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.AdvancedLocation);

        container.add(new WebMarkupContainer("manageCustomersContainer").setVisible(getSessionUser().hasAccess("manageendusers")));
        container.add(new BookmarkablePageLink<UsersListPage>("manageUsersLink", UsersListPage.class).setVisible(canManageSystemUsers));
        container.add(new BookmarkablePageLink<UserGroupsPage>("userGroupsLink", UserGroupsPage.class).setVisible(canManageSystemUsers));
        container.add(new BookmarkablePageLink<UserRequestListPage>("userRegistrationsLink", UserRequestListPage.class).setVisible(canManageSystemUsers && userLimitService.isReadOnlyUsersEnabled()));
        container.add(new WebMarkupContainer("managePredefinedLocationsContainer").setVisible(getSessionUser().hasAccess("manageendusers") && advancedLocationEnabled));

    	return container;
    }

    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId).setVisible(false));
    }

    protected void addBreadCrumbBar(String breadCrumbBarId) {
        add(new BreadCrumbBar(breadCrumbBarId).setVisible(false));
    }

//    protected ConfigurationProvider getConfigurationProvider() {
//		if (configurationProvider==null) {
//			configurationProvider = ConfigContext.getCurrentContext();
//		}
//		return configurationProvider;
//	}

    protected ActionURLBuilder createActionUrlBuilder() {
        return new ActionURLBuilder(getBaseURI(), configService);
    }

    private URI getBaseURI() {
        return URI.create(getServletRequest().getRequestURL().toString()).resolve(getServletRequest().getContextPath() + "/");
    }
	
//	@Deprecated // for testing only to get around static implementation of configContext.
//    public void setConfigurationProvider(ConfigurationProvider configurationProvider) {
//		this.configurationProvider = configurationProvider;
//	}

    @Override
    public void renderHead(IHeaderResponse response) {
        StringBuffer javascriptBuffer = new StringBuffer();
        Integer timeoutTime = configService.getInteger(ConfigEntry.ACTIVE_SESSION_TIME_OUT);
        String loginLightboxTitle = getApplication().getResourceSettings().getLocalizer().getString("title.sessionexpired", null);
        javascriptBuffer.append("loggedInUserName = '").append(getSessionUser().getUserName()).append("';\n");
        javascriptBuffer.append("tenantName = '").append(getTenant().getName()).append("';\n");
        javascriptBuffer.append("sessionTimeOut = ").append(timeoutTime).append(";\n");
        javascriptBuffer.append("sessionTestUrl = '/fieldid/ajax/testSession.action';").append(";\n");
        javascriptBuffer.append("loginWindowTitle = '").append(loginLightboxTitle).append("';\n");

        response.renderJavaScript(javascriptBuffer.toString(), null);

        renderJqueryJavaScriptReference(response);

        response.renderJavaScriptReference("javascript/common-jquery.js");
        response.renderJavaScriptReference("javascript/sessionTimeout-jquery.js");
        response.renderJavaScriptReference("javascript/json2.js");
        response.renderJavaScriptReference("javascript/jquery.at_intervals.js");
        response.renderJavaScriptReference("javascript/jquery.colorbox.js");
        response.renderJavaScriptReference("javascript/jquery.dropdown.js");

        //Adding NewRelic Browser Agent
        StringBuffer newRelicJavascriptBuffer = new StringBuffer();
        String newRelicJSDefault = "window.NREUM||(NREUM={}),__nr_require=function(t,n,e){function r(e){if(!n[e]){var o=n[e]={exports:{}};t[e][0].call(o.exports,function(n){var o=t[e][1][n];return r(o||n)},o,o.exports)}return n[e].exports}if(\"function\"==typeof __nr_require)return __nr_require;for(var o=0;o<e.length;o++)r(e[o]);return r}({1:[function(t,n,e){function r(t){try{s.console&&console.log(t)}catch(n){}}var o,i=t(\"ee\"),a=t(15),s={};try{o=localStorage.getItem(\"__nr_flags\").split(\",\"),console&&\"function\"==typeof console.log&&(s.console=!0,o.indexOf(\"dev\")!==-1&&(s.dev=!0),o.indexOf(\"nr_dev\")!==-1&&(s.nrDev=!0))}catch(c){}s.nrDev&&i.on(\"internal-error\",function(t){r(t.stack)}),s.dev&&i.on(\"fn-err\",function(t,n,e){r(e.stack)}),s.dev&&(r(\"NR AGENT IN DEVELOPMENT MODE\"),r(\"flags: \"+a(s,function(t,n){return t}).join(\", \")))},{}],2:[function(t,n,e){function r(t,n,e,r,s){try{p?p-=1:o(s||new UncaughtException(t,n,e),!0)}catch(f){try{i(\"ierr\",[f,c.now(),!0])}catch(d){}}return\"function\"==typeof u&&u.apply(this,a(arguments))}function UncaughtException(t,n,e){this.message=t||\"Uncaught error with no additional information\",this.sourceURL=n,this.line=e}function o(t,n){var e=n?null:c.now();i(\"err\",[t,e])}var i=t(\"handle\"),a=t(16),s=t(\"ee\"),c=t(\"loader\"),f=t(\"gos\"),u=window.onerror,d=!1,l=\"nr@seenError\",p=0;c.features.err=!0,t(1),window.onerror=r;try{throw new Error}catch(h){\"stack\"in h&&(t(8),t(7),\"addEventListener\"in window&&t(5),c.xhrWrappable&&t(9),d=!0)}s.on(\"fn-start\",function(t,n,e){d&&(p+=1)}),s.on(\"fn-err\",function(t,n,e){d&&!e[l]&&(f(e,l,function(){return!0}),this.thrown=!0,o(e))}),s.on(\"fn-end\",function(){d&&!this.thrown&&p>0&&(p-=1)}),s.on(\"internal-error\",function(t){i(\"ierr\",[t,c.now(),!0])})},{}],3:[function(t,n,e){t(\"loader\").features.ins=!0},{}],4:[function(t,n,e){function r(t){}if(window.performance&&window.performance.timing&&window.performance.getEntriesByType){var o=t(\"ee\"),i=t(\"handle\"),a=t(8),s=t(7),c=\"learResourceTimings\",f=\"addEventListener\",u=\"resourcetimingbufferfull\",d=\"bstResource\",l=\"resource\",p=\"-start\",h=\"-end\",m=\"fn\"+p,w=\"fn\"+h,v=\"bstTimer\",y=\"pushState\",g=t(\"loader\");g.features.stn=!0,t(6);var b=NREUM.o.EV;o.on(m,function(t,n){var e=t[0];e instanceof b&&(this.bstStart=g.now())}),o.on(w,function(t,n){var e=t[0];e instanceof b&&i(\"bst\",[e,n,this.bstStart,g.now()])}),a.on(m,function(t,n,e){this.bstStart=g.now(),this.bstType=e}),a.on(w,function(t,n){i(v,[n,this.bstStart,g.now(),this.bstType])}),s.on(m,function(){this.bstStart=g.now()}),s.on(w,function(t,n){i(v,[n,this.bstStart,g.now(),\"requestAnimationFrame\"])}),o.on(y+p,function(t){this.time=g.now(),this.startPath=location.pathname+location.hash}),o.on(y+h,function(t){i(\"bstHist\",[location.pathname+location.hash,this.startPath,this.time])}),f in window.performance&&(window.performance[\"c\"+c]?window.performance[f](u,function(t){i(d,[window.performance.getEntriesByType(l)]),window.performance[\"c\"+c]()},!1):window.performance[f](\"webkit\"+u,function(t){i(d,[window.performance.getEntriesByType(l)]),window.performance[\"webkitC\"+c]()},!1)),document[f](\"scroll\",r,{passive:!0}),document[f](\"keypress\",r,!1),document[f](\"click\",r,!1)}},{}],5:[function(t,n,e){function r(t){for(var n=t;n&&!n.hasOwnProperty(u);)n=Object.getPrototypeOf(n);n&&o(n)}function o(t){s.inPlace(t,[u,d],\"-\",i)}function i(t,n){return t[1]}var a=t(\"ee\").get(\"events\"),s=t(18)(a,!0),c=t(\"gos\"),f=XMLHttpRequest,u=\"addEventListener\",d=\"removeEventListener\";n.exports=a,\"getPrototypeOf\"in Object?(r(document),r(window),r(f.prototype)):f.prototype.hasOwnProperty(u)&&(o(window),o(f.prototype)),a.on(u+\"-start\",function(t,n){var e=t[1],r=c(e,\"nr@wrapped\",function(){function t(){if(\"function\"==typeof e.handleEvent)return e.handleEvent.apply(e,arguments)}var n={object:t,\"function\":e}[typeof e];return n?s(n,\"fn-\",null,n.name||\"anonymous\"):e});this.wrapped=t[1]=r}),a.on(d+\"-start\",function(t){t[1]=this.wrapped||t[1]})},{}],6:[function(t,n,e){var r=t(\"ee\").get(\"history\"),o=t(18)(r);n.exports=r,o.inPlace(window.history,[\"pushState\",\"replaceState\"],\"-\")},{}],7:[function(t,n,e){var r=t(\"ee\").get(\"raf\"),o=t(18)(r),i=\"equestAnimationFrame\";n.exports=r,o.inPlace(window,[\"r\"+i,\"mozR\"+i,\"webkitR\"+i,\"msR\"+i],\"raf-\"),r.on(\"raf-start\",function(t){t[0]=o(t[0],\"fn-\")})},{}],8:[function(t,n,e){function r(t,n,e){t[0]=a(t[0],\"fn-\",null,e)}function o(t,n,e){this.method=e,this.timerDuration=isNaN(t[1])?0:+t[1],t[0]=a(t[0],\"fn-\",this,e)}var i=t(\"ee\").get(\"timer\"),a=t(18)(i),s=\"setTimeout\",c=\"setInterval\",f=\"clearTimeout\",u=\"-start\",d=\"-\";n.exports=i,a.inPlace(window,[s,\"setImmediate\"],s+d),a.inPlace(window,[c],c+d),a.inPlace(window,[f,\"clearImmediate\"],f+d),i.on(c+u,r),i.on(s+u,o)},{}],9:[function(t,n,e){function r(t,n){d.inPlace(n,[\"onreadystatechange\"],\"fn-\",s)}function o(){var t=this,n=u.context(t);t.readyState>3&&!n.resolved&&(n.resolved=!0,u.emit(\"xhr-resolved\",[],t)),d.inPlace(t,y,\"fn-\",s)}function i(t){g.push(t),h&&(x?x.then(a):w?w(a):(E=-E,O.data=E))}function a(){for(var t=0;t<g.length;t++)r([],g[t]);g.length&&(g=[])}function s(t,n){return n}function c(t,n){for(var e in t)n[e]=t[e];return n}t(5);var f=t(\"ee\"),u=f.get(\"xhr\"),d=t(18)(u),l=NREUM.o,p=l.XHR,h=l.MO,m=l.PR,w=l.SI,v=\"readystatechange\",y=[\"onload\",\"onerror\",\"onabort\",\"onloadstart\",\"onloadend\",\"onprogress\",\"ontimeout\"],g=[];n.exports=u;var b=window.XMLHttpRequest=function(t){var n=new p(t);try{u.emit(\"new-xhr\",[n],n),n.addEventListener(v,o,!1)}catch(e){try{u.emit(\"internal-error\",[e])}catch(r){}}return n};if(c(p,b),b.prototype=p.prototype,d.inPlace(b.prototype,[\"open\",\"send\"],\"-xhr-\",s),u.on(\"send-xhr-start\",function(t,n){r(t,n),i(n)}),u.on(\"open-xhr-start\",r),h){var x=m&&m.resolve();if(!w&&!m){var E=1,O=document.createTextNode(E);new h(a).observe(O,{characterData:!0})}}else f.on(\"fn-end\",function(t){t[0]&&t[0].type===v||a()})},{}],10:[function(t,n,e){function r(t){var n=this.params,e=this.metrics;if(!this.ended){this.ended=!0;for(var r=0;r<d;r++)t.removeEventListener(u[r],this.listener,!1);if(!n.aborted){if(e.duration=a.now()-this.startTime,4===t.readyState){n.status=t.status;var i=o(t,this.lastSize);if(i&&(e.rxSize=i),this.sameOrigin){var c=t.getResponseHeader(\"X-NewRelic-App-Data\");c&&(n.cat=c.split(\", \").pop())}}else n.status=0;e.cbTime=this.cbTime,f.emit(\"xhr-done\",[t],t),s(\"xhr\",[n,e,this.startTime])}}}function o(t,n){var e=t.responseType;if(\"json\"===e&&null!==n)return n;var r=\"arraybuffer\"===e||\"blob\"===e||\"json\"===e?t.response:t.responseText;return h(r)}function i(t,n){var e=c(n),r=t.params;r.host=e.hostname+\":\"+e.port,r.pathname=e.pathname,t.sameOrigin=e.sameOrigin}var a=t(\"loader\");if(a.xhrWrappable){var s=t(\"handle\"),c=t(11),f=t(\"ee\"),u=[\"load\",\"error\",\"abort\",\"timeout\"],d=u.length,l=t(\"id\"),p=t(14),h=t(13),m=window.XMLHttpRequest;a.features.xhr=!0,t(9),f.on(\"new-xhr\",function(t){var n=this;n.totalCbs=0,n.called=0,n.cbTime=0,n.end=r,n.ended=!1,n.xhrGuids={},n.lastSize=null,p&&(p>34||p<10)||window.opera||t.addEventListener(\"progress\",function(t){n.lastSize=t.loaded},!1)}),f.on(\"open-xhr-start\",function(t){this.params={method:t[0]},i(this,t[1]),this.metrics={}}),f.on(\"open-xhr-end\",function(t,n){\"loader_config\"in NREUM&&\"xpid\"in NREUM.loader_config&&this.sameOrigin&&n.setRequestHeader(\"X-NewRelic-ID\",NREUM.loader_config.xpid)}),f.on(\"send-xhr-start\",function(t,n){var e=this.metrics,r=t[0],o=this;if(e&&r){var i=h(r);i&&(e.txSize=i)}this.startTime=a.now(),this.listener=function(t){try{\"abort\"===t.type&&(o.params.aborted=!0),(\"load\"!==t.type||o.called===o.totalCbs&&(o.onloadCalled||\"function\"!=typeof n.onload))&&o.end(n)}catch(e){try{f.emit(\"internal-error\",[e])}catch(r){}}};for(var s=0;s<d;s++)n.addEventListener(u[s],this.listener,!1)}),f.on(\"xhr-cb-time\",function(t,n,e){this.cbTime+=t,n?this.onloadCalled=!0:this.called+=1,this.called!==this.totalCbs||!this.onloadCalled&&\"function\"==typeof e.onload||this.end(e)}),f.on(\"xhr-load-added\",function(t,n){var e=\"\"+l(t)+!!n;this.xhrGuids&&!this.xhrGuids[e]&&(this.xhrGuids[e]=!0,this.totalCbs+=1)}),f.on(\"xhr-load-removed\",function(t,n){var e=\"\"+l(t)+!!n;this.xhrGuids&&this.xhrGuids[e]&&(delete this.xhrGuids[e],this.totalCbs-=1)}),f.on(\"addEventListener-end\",function(t,n){n instanceof m&&\"load\"===t[0]&&f.emit(\"xhr-load-added\",[t[1],t[2]],n)}),f.on(\"removeEventListener-end\",function(t,n){n instanceof m&&\"load\"===t[0]&&f.emit(\"xhr-load-removed\",[t[1],t[2]],n)}),f.on(\"fn-start\",function(t,n,e){n instanceof m&&(\"onload\"===e&&(this.onload=!0),(\"load\"===(t[0]&&t[0].type)||this.onload)&&(this.xhrCbStart=a.now()))}),f.on(\"fn-end\",function(t,n){this.xhrCbStart&&f.emit(\"xhr-cb-time\",[a.now()-this.xhrCbStart,this.onload,n],n)})}},{}],11:[function(t,n,e){n.exports=function(t){var n=document.createElement(\"a\"),e=window.location,r={};n.href=t,r.port=n.port;var o=n.href.split(\"://\");!r.port&&o[1]&&(r.port=o[1].split(\"/\")[0].split(\"@\").pop().split(\":\")[1]),r.port&&\"0\"!==r.port||(r.port=\"https\"===o[0]?\"443\":\"80\"),r.hostname=n.hostname||e.hostname,r.pathname=n.pathname,r.protocol=o[0],\"/\"!==r.pathname.charAt(0)&&(r.pathname=\"/\"+r.pathname);var i=!n.protocol||\":\"===n.protocol||n.protocol===e.protocol,a=n.hostname===document.domain&&n.port===e.port;return r.sameOrigin=i&&(!n.hostname||a),r}},{}],12:[function(t,n,e){function r(){}function o(t,n,e){return function(){return i(t,[f.now()].concat(s(arguments)),n?null:this,e),n?void 0:this}}var i=t(\"handle\"),a=t(15),s=t(16),c=t(\"ee\").get(\"tracer\"),f=t(\"loader\"),u=NREUM;\"undefined\"==typeof window.newrelic&&(newrelic=u);var d=[\"setPageViewName\",\"setCustomAttribute\",\"setErrorHandler\",\"finished\",\"addToTrace\",\"inlineHit\",\"addRelease\"],l=\"api-\",p=l+\"ixn-\";a(d,function(t,n){u[n]=o(l+n,!0,\"api\")}),u.addPageAction=o(l+\"addPageAction\",!0),u.setCurrentRouteName=o(l+\"routeName\",!0),n.exports=newrelic,u.interaction=function(){return(new r).get()};var h=r.prototype={createTracer:function(t,n){var e={},r=this,o=\"function\"==typeof n;return i(p+\"tracer\",[f.now(),t,e],r),function(){if(c.emit((o?\"\":\"no-\")+\"fn-start\",[f.now(),r,o],e),o)try{return n.apply(this,arguments)}catch(t){throw c.emit(\"fn-err\",[arguments,this,t],e),t}finally{c.emit(\"fn-end\",[f.now()],e)}}}};a(\"setName,setAttribute,save,ignore,onEnd,getContext,end,get\".split(\",\"),function(t,n){h[n]=o(p+n)}),newrelic.noticeError=function(t){\"string\"==typeof t&&(t=new Error(t)),i(\"err\",[t,f.now()])}},{}],13:[function(t,n,e){n.exports=function(t){if(\"string\"==typeof t&&t.length)return t.length;if(\"object\"==typeof t){if(\"undefined\"!=typeof ArrayBuffer&&t instanceof ArrayBuffer&&t.byteLength)return t.byteLength;if(\"undefined\"!=typeof Blob&&t instanceof Blob&&t.size)return t.size;if(!(\"undefined\"!=typeof FormData&&t instanceof FormData))try{return JSON.stringify(t).length}catch(n){return}}}},{}],14:[function(t,n,e){var r=0,o=navigator.userAgent.match(/Firefox[\\/\\s](\\d+\\.\\d+)/);o&&(r=+o[1]),n.exports=r},{}],15:[function(t,n,e){function r(t,n){var e=[],r=\"\",i=0;for(r in t)o.call(t,r)&&(e[i]=n(r,t[r]),i+=1);return e}var o=Object.prototype.hasOwnProperty;n.exports=r},{}],16:[function(t,n,e){function r(t,n,e){n||(n=0),\"undefined\"==typeof e&&(e=t?t.length:0);for(var r=-1,o=e-n||0,i=Array(o<0?0:o);++r<o;)i[r]=t[n+r];return i}n.exports=r},{}],17:[function(t,n,e){n.exports={exists:\"undefined\"!=typeof window.performance&&window.performance.timing&&\"undefined\"!=typeof window.performance.timing.navigationStart}},{}],18:[function(t,n,e){function r(t){return!(t&&t instanceof Function&&t.apply&&!t[a])}var o=t(\"ee\"),i=t(16),a=\"nr@original\",s=Object.prototype.hasOwnProperty,c=!1;n.exports=function(t,n){function e(t,n,e,o){function nrWrapper(){var r,a,s,c;try{a=this,r=i(arguments),s=\"function\"==typeof e?e(r,a):e||{}}catch(f){l([f,\"\",[r,a,o],s])}u(n+\"start\",[r,a,o],s);try{return c=t.apply(a,r)}catch(d){throw u(n+\"err\",[r,a,d],s),d}finally{u(n+\"end\",[r,a,c],s)}}return r(t)?t:(n||(n=\"\"),nrWrapper[a]=t,d(t,nrWrapper),nrWrapper)}function f(t,n,o,i){o||(o=\"\");var a,s,c,f=\"-\"===o.charAt(0);for(c=0;c<n.length;c++)s=n[c],a=t[s],r(a)||(t[s]=e(a,f?s+o:o,i,s))}function u(e,r,o){if(!c||n){var i=c;c=!0;try{t.emit(e,r,o,n)}catch(a){l([a,e,r,o])}c=i}}function d(t,n){if(Object.defineProperty&&Object.keys)try{var e=Object.keys(t);return e.forEach(function(e){Object.defineProperty(n,e,{get:function(){return t[e]},set:function(n){return t[e]=n,n}})}),n}catch(r){l([r])}for(var o in t)s.call(t,o)&&(n[o]=t[o]);return n}function l(n){try{t.emit(\"internal-error\",n)}catch(e){}}return t||(t=o),e.inPlace=f,e.flag=a,e}},{}],ee:[function(t,n,e){function r(){}function o(t){function n(t){return t&&t instanceof r?t:t?c(t,s,i):i()}function e(e,r,o,i){if(!l.aborted||i){t&&t(e,r,o);for(var a=n(o),s=h(e),c=s.length,f=0;f<c;f++)s[f].apply(a,r);var d=u[y[e]];return d&&d.push([g,e,r,a]),a}}function p(t,n){v[t]=h(t).concat(n)}function h(t){return v[t]||[]}function m(t){return d[t]=d[t]||o(e)}function w(t,n){f(t,function(t,e){n=n||\"feature\",y[e]=n,n in u||(u[n]=[])})}var v={},y={},g={on:p,emit:e,get:m,listeners:h,context:n,buffer:w,abort:a,aborted:!1};return g}function i(){return new r}function a(){(u.api||u.feature)&&(l.aborted=!0,u=l.backlog={})}var s=\"nr@context\",c=t(\"gos\"),f=t(15),u={},d={},l=n.exports=o();l.backlog=u},{}],gos:[function(t,n,e){function r(t,n,e){if(o.call(t,n))return t[n];var r=e();if(Object.defineProperty&&Object.keys)try{return Object.defineProperty(t,n,{value:r,writable:!0,enumerable:!1}),r}catch(i){}return t[n]=r,r}var o=Object.prototype.hasOwnProperty;n.exports=r},{}],handle:[function(t,n,e){function r(t,n,e,r){o.buffer([t],r),o.emit(t,n,e)}var o=t(\"ee\").get(\"handle\");n.exports=r,r.ee=o},{}],id:[function(t,n,e){function r(t){var n=typeof t;return!t||\"object\"!==n&&\"function\"!==n?-1:t===window?0:a(t,i,function(){return o++})}var o=1,i=\"nr@id\",a=t(\"gos\");n.exports=r},{}],loader:[function(t,n,e){function r(){if(!x++){var t=b.info=NREUM.info,n=l.getElementsByTagName(\"script\")[0];if(setTimeout(u.abort,3e4),!(t&&t.licenseKey&&t.applicationID&&n))return u.abort();f(y,function(n,e){t[n]||(t[n]=e)}),c(\"mark\",[\"onload\",a()+b.offset],null,\"api\");var e=l.createElement(\"script\");e.src=\"https://\"+t.agent,n.parentNode.insertBefore(e,n)}}function o(){\"complete\"===l.readyState&&i()}function i(){c(\"mark\",[\"domContent\",a()+b.offset],null,\"api\")}function a(){return E.exists&&performance.now?Math.round(performance.now()):(s=Math.max((new Date).getTime(),s))-b.offset}var s=(new Date).getTime(),c=t(\"handle\"),f=t(15),u=t(\"ee\"),d=window,l=d.document,p=\"addEventListener\",h=\"attachEvent\",m=d.XMLHttpRequest,w=m&&m.prototype;NREUM.o={ST:setTimeout,SI:d.setImmediate,CT:clearTimeout,XHR:m,REQ:d.Request,EV:d.Event,PR:d.Promise,MO:d.MutationObserver};var v=\"\"+location,y={beacon:\"bam.nr-data.net\",errorBeacon:\"bam.nr-data.net\",agent:\"js-agent.newrelic.com/nr-1071.min.js\"},g=m&&w&&w[p]&&!/CriOS/.test(navigator.userAgent),b=n.exports={offset:s,now:a,origin:v,features:{},xhrWrappable:g};t(12),l[p]?(l[p](\"DOMContentLoaded\",i,!1),d[p](\"load\",r,!1)):(l[h](\"onreadystatechange\",o),d[h](\"onload\",r)),c(\"mark\",[\"firstbyte\",s],null,\"api\");var x=0,E=t(17)},{}]},{},[\"loader\",2,10,4,3]);\n" +
                ";NREUM.info={beacon:\"bam.nr-data.net\",errorBeacon:\"bam.nr-data.net\",";

        newRelicJavascriptBuffer.append(newRelicJSDefault);
        newRelicJavascriptBuffer.append("licenseKey:\"" + configService.getConfig().getSystem().getNewRelicLicenseKey() + "\",");
        newRelicJavascriptBuffer.append("applicationID:\"" + configService.getConfig().getSystem().getNewRelicApplicationId() + "\",");
        newRelicJavascriptBuffer.append("sa:1}");

        response.renderJavaScript(newRelicJavascriptBuffer.toString(), null);
        renderJqueryJavaScriptReference(response);

        response.renderCSSReference("style/legacy/colorbox.css");

        if (configService.getBoolean(ConfigEntry.APPTEGIC_ENABLED)) {
            SessionUser sessionUser = getSessionUser();

            Map<String, String> apptegicParams = new HashMap<String, String>();
            apptegicParams.put("user", sessionUser.getName());
            apptegicParams.put("company", sessionUser.getTenant().getName());
            apptegicParams.put("userType", sessionUser.getUserTypeLabel());
            apptegicParams.put("accountType", sessionUser.getAccountType());
            apptegicParams.put("apptegicDataset", configService.getString(ConfigEntry.APPTEGIC_DATASET));

            TextTemplate apptegicTemplate = new PackageTextTemplate(FieldIDTemplatePage.class, "apptegic.js");
            response.renderJavaScript(apptegicTemplate.asString(apptegicParams), null);
        }

        if(!getMainCss().isEmpty())
            response.renderOnDomReadyJavaScript("$('main[role=\"main\"]').addClass('"+getMainCss()+"');");

        if(!getWrapperCss().isEmpty())
            response.renderOnDomReadyJavaScript("$('wrapper[role=\"wrapper\"]').addClass('"+getWrapperCss()+"');");

        String headerScript = configService.getString(ConfigEntry.HEADER_SCRIPT, getTenantId());

        if (headerScript != null && !headerScript.isEmpty()) {
            response.renderOnDomReadyJavaScript(headerScript);
        }

        if (orgService.getPrimaryOrgForTenant(getTenant().getId()).hasExtendedFeature(ExtendedFeature.GoogleTranslate)) {
            response.renderOnDomReadyJavaScript(
                    "if (isGoogleTranslateAllowedForCurrentLanguage())\n" +
                        "loadGoogleTranslate();\n" +
                    "else {\n" +
                        "hideGoogleTranslateWidget();\n" +
                            // If there are no languages in our translation facility then only if Google Translate is
                            // active should the language dialog be available.
                            (!selectLanguagePanel.hasLanguagesToDisplay() ?
                                "document.getElementById('" + languageSelectionLink.getMarkupId() + "').style.display = 'none';\n" : "") +
                    "}");
        }
        else {
            response.renderOnDomReadyJavaScript("hideGoogleTranslateWidget();");
        }

    }

    public boolean isGoogleTranslateEnabled() {
        if (googleTranslateEnabled == null)
            googleTranslateEnabled = orgService.getPrimaryOrgForTenant(getTenant().getId()).
                    hasExtendedFeature(ExtendedFeature.GoogleTranslate);
        return googleTranslateEnabled.booleanValue();
    }

    protected void renderJqueryJavaScriptReference(IHeaderResponse response) {
        response.renderJavaScriptReference(CoreJavaScriptResourceReference.get(), "CoreJS");
    }

    protected Long getTenantId() {
        return getTenant().getId();
    }

    protected Tenant getTenant() {
        return getSessionUser().getTenant();
    }

	class GoogleAnalyticsContainer extends WebMarkupContainer {

		public GoogleAnalyticsContainer(String id) {
			super(id);
			setRenderBodyOnly(true);
		}

        @Override
        protected void onBeforeRender() {
            FieldIDSession.get().getSessionUser().getTenant();
        	if (!hasBeenRendered()) {
        		setVisible(configService.getBoolean(ConfigEntry.GOOGLE_ANALYTICS_ENABLED));    	    		
        	}
        	super.onBeforeRender();  // note : call super at END of override.  see wicket docs.
        }
    	
    }

    public TopFeedbackPanel getTopFeedbackPanel() {
        return topFeedbackPanel;
    }


    class Header extends Fragment {

        public Header(String id) {
            super(id, "globalHeader", FieldIDTemplatePage.this);

            SessionUser sessionUser = getSessionUser();

            boolean inspectionEnabled = getSecurityGuard().isInspectionsEnabled();
            boolean trendingEnabled = getSecurityGuard().isCriteriaTrendsEnabled();
            boolean advancedEventSearchEnabled = getSecurityGuard().isAdvancedEventSearchEnabled();
            boolean extraEventLinksAvailable = trendingEnabled || advancedEventSearchEnabled;

            // header logo and links
            add(new StaticImage("tenantLogo", new Model<String>(s3Service.getBrandingLogoURL().toString())).setEscapeModelStrings(false));

            add(new Label("loggedInUsernameLabel", sessionUser.getName()));
            addSpeedIdentifyLinks(sessionUser);

            languageSelectionLink = new AjaxLink<Void>("languageSelection") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    languageSelectionModalWindow.show(target);
                }
            }.setVisible(selectLanguagePanel.hasLanguagesToDisplay() || isGoogleTranslateEnabled());
            add(languageSelectionLink);

            add(new ExternalLink("support", getSupportUrl(), new FIDLabelModel("label.support").getObject()));

            add(new SavedItemsDropdown("savedItemsDropdown"));

            // menu bar links: Start Event, Search, Reporting, Places, LOTO, Safety Network, Jobs, Setup
            BookmarkablePageLink startEventLink = new BookmarkablePageLink<Void>("startEventLinkContainer", StartEventPage.class);
            startEventLink.setVisible(sessionUser.hasAccess("createevent") && inspectionEnabled);
            add(startEventLink);

            add(new BookmarkablePageLink<Void>("assetSearchLink", SearchPage.class));

            add(new BookmarkablePageLink<Void>("reportingLink", ReportPage.class).setVisible(inspectionEnabled));
            WebMarkupContainer extraEventLinksContainer = new WebMarkupContainer("extraReportingLinksContainer");
            extraEventLinksContainer.setVisible(extraEventLinksAvailable && inspectionEnabled);
            extraEventLinksContainer.add(new BookmarkablePageLink<Void>("advancedEventSearchLink", AdvancedEventSearchPage.class).setVisible(advancedEventSearchEnabled));
            extraEventLinksContainer.add(new BookmarkablePageLink<Void>("criteriaTrendsLink", CriteriaTrendsPage.class).setVisible(trendingEnabled));
            add(extraEventLinksContainer);

            add(new BookmarkablePageLink<Void>("placesLink", OrgViewPage.class));

            add(createLotoLinkContainer());

            add(new WebMarkupContainer("safetyNetworkLinkContainer").setVisible(getUserSecurityGuard().isAllowedManageSafetyNetwork() && inspectionEnabled));

            add(new WebMarkupContainer("jobsLinkContainer").setVisible(getSecurityGuard().isProjectsEnabled() && inspectionEnabled));

            add(createSetupLinkContainer(sessionUser));

            add(createSmartSearch("smartSearch"));
            add(new HiddenField<String>("walkMeUserEmailAddress", Model.of(getSessionUser().getEmailAddress())).setMarkupId("walkMeUserEmailAddress"));
        }

        private void addSpeedIdentifyLinks(SessionUser sessionUser) {
            WebMarkupContainer identifyMenuContainer = new WebMarkupContainer("identifyMenuContainer");
            identifyMenuContainer.setVisible(sessionUser.hasAccess("tag"));

            if (getSecurityGuard().isIntegrationEnabled()) {
                identifyMenuContainer.add(new BookmarkablePageLink<Void>("identifyLink", AssetImportPage.class,
                        PageParametersBuilder.param(AssetImportPage.INITIAL_TAB, AssetImportPage.ADD_WITH_ORDER_TAB)));
            } else {
                identifyMenuContainer.add(new BookmarkablePageLink("identifyLink", IdentifyOrEditAssetPage.class));
            }

            add(identifyMenuContainer);
        }

    }

    private Component createLotoSubMenu() {
        WebMarkupContainer container = new WebMarkupContainer("lotoSubMenuContainer");
        container.add(new BookmarkablePageLink<ProcedureApproverPage>("procedureApproverLink", ProcedureApproverPage.class));
        container.add(new BookmarkablePageLink<EnableByAssetTypePage>("enableByAssetTypeLink", EnableByAssetTypePage.class));
        container.add(new BookmarkablePageLink<PrintoutTemplatePage>("printoutTemplateLink", PrintoutTemplatePage.class));
        container.add(new BookmarkablePageLink<LotoSetupPage>("lotoSetupLink", LotoSetupPage.class));

        container.setVisible(getSecurityGuard().isLotoEnabled());
        return container;
    }

    private Component createActionsSubMenu() {
        WebMarkupContainer container = new WebMarkupContainer("actionsSubMenuContainer");
        container.add(new BookmarkablePageLink<ActionEmailSetupPage>("actionEmailCustomizationLink", ActionEmailSetupPage.class));
        container.add(new BookmarkablePageLink("priorityCodeListLink", PriorityCodePage.class).setVisible(getSecurityGuard().isInspectionsEnabled()));
        return container;
    }


    private Component createLotoLinkContainer() {
        WebMarkupContainer lotoLinkContainer = new WebMarkupContainer("lotoLinkContainer");

        lotoLinkContainer.add(new BookmarkablePageLink<Void>("procedureSearchLink", ProcedureSearchPage.class));
        lotoLinkContainer.add(new BookmarkablePageLink<Void>("procedureList", PublishedListAllPage.class));
        lotoLinkContainer.add(new BookmarkablePageLink<Void>("upcomingAuditsLink", ProcedureAuditListPage.class) {
            @Override
            public boolean isVisible() {
                return getUserSecurityGuard().isAllowedProcedureAudit();
            }
        });
        lotoLinkContainer.add(new BookmarkablePageLink<Void>("procedureAwaitingApprovalLink", ProcedureWaitingApprovalsPage.class));
        lotoLinkContainer.setVisible(getSecurityGuard().isLotoEnabled());

        return lotoLinkContainer;
    }

    private String getVersionLabelText() {
        return String.format("%s (%s)", FieldIdVersion.getVersion(), configService.getConfig().getSystem().getNodeName());
    }

    public boolean isShowTitle() {
        return showTitle;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    static class StaticImage extends WebComponent {
        public StaticImage(String id, IModel<String> urlModel) {
            super( id, urlModel );
        }

        @Override
        protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag( tag );
            checkComponentTag( tag, "img" );
            tag.put( "src", getDefaultModelObjectAsString() );
        }
    }

    protected String getMainCss() {
        return "";
    }

    protected String getWrapperCss() {
        return "";
    }

    /**
     * Theoretically, we can call-back to this method from any Component/Panel as long as the base page is, in fact,
     * this Page class.
     *
     * This method was ripped out of some other Wicket components and allows the developer to send a file downstream
     * to the user by directly manipulating the request cycle.  This functionality is offered by pre-canned components
     * starting in Wicket 6, so the use of this method may be rather short-lived.
     *
     * The File which the user will download is represented by fileToDownload, and the name that will be given to
     * the download is described by fileName.
     *
     * @param fileToDownload - A File object that will be downloaded by the user.
     * @param fileName - A String representing the desired name for the File object.
     */
    public void handleDownload(File fileToDownload, String fileName) {
        fileName = UrlEncoder.QUERY_INSTANCE.encode(fileName, getRequest().getCharset());

        IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(fileToDownload));

        getRequestCycle().scheduleRequestHandlerAfterCurrent(
                new ResourceStreamRequestHandler(resourceStream) {
                    @Override
                    public void respond(IRequestCycle requestCycle) {
                        super.respond(requestCycle);

                        Files.remove(fileToDownload);
                    }
                }.setFileName(fileName).setContentDisposition(ContentDisposition.ATTACHMENT)
        );
    }
}
