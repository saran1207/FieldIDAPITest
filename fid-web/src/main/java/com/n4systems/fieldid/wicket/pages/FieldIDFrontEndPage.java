package com.n4systems.fieldid.wicket.pages;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.UIConstants;
import com.n4systems.fieldid.service.amazon.S3Service;
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
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.saveditems.SavedItemsDropdown;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.ProcedureSearchPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.ReportPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.SearchPage;
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
import org.apache.wicket.markup.html.image.Image;
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
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplate;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;
import rfid.web.helper.SessionUser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;

@SuppressWarnings("serial")
public class FieldIDFrontEndPage extends FieldIDAuthenticatedPage implements UIConstants {

    public static final String SUB_MENU_ID = "subMenu";
    public static final String LEFT_PANEL_ID = "leftPanel";
    private static final String LEFT_PANEL_CONTROLLER_ID = "leftPanelController";
    public static final String BOTTOM_PANEL_ID="bottomPanel";

    @SpringBean
	private ConfigService configService;

	@SpringBean
	private UserLimitService userLimitService;

    @SpringBean
    private S3Service s3Service;

    private Asset smartSearchAsset;
    private Component titleLabel;
	private Component topTitleLabel;
    private TopFeedbackPanel topFeedbackPanel;
    private ModalWindow languageSelectionModalWindow;
    private final SelectLanguagePanel selectLanguagePanel;


    public FieldIDFrontEndPage() {
        this(null);
    }

    public FieldIDFrontEndPage(PageParameters params) {
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


        add(new Header("mainHeader"));
        add(new DebugBar("debugBar"));
        add(new CustomJavascriptPanel("customJsPanel"));
        add(new GoogleAnalyticsContainer("googleAnalyticsScripts"));
        add(new WebMarkupContainer(LEFT_PANEL_ID).setVisible(false));
        add(new WebMarkupContainer(SUB_MENU_ID).setVisible(false));
        add(new WebMarkupContainer(BOTTOM_PANEL_ID).setVisible(false));
        add(new WebMarkupContainer(LEFT_PANEL_CONTROLLER_ID).setVisible(false));

        // TODO DD : refactor this...override
        addCssContainers();

        add(topFeedbackPanel = new TopFeedbackPanel("topFeedbackPanel"));
        add(new Label("versionLabel", FieldIdVersion.getVersion()));

        String footerScript = configService.getString(ConfigEntry.FOOTER_SCRIPT, getTenantId());
        if (footerScript != null && !footerScript.isEmpty()) {
            add(new Label("footerScript", footerScript).setEscapeModelStrings(false));
        } else {
            add(new Label("footerScript").setVisible(false));
        }

        add(createBackToLink("backToLink", "backToLinkLabel"));
        add(createRelogLink());
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        // We add these components here rather than in the constructor
        // So subclasses that need to override them can finish their constructors before doing so
        // eg. Labels may require details of entities to be loaded.
        addNavBar("navBar");
        add(titleLabel = createTitleLabel("titleLabel"));
        titleLabel.setRenderBodyOnly(true);

        add(topTitleLabel = useTopTitleLabel() ? createTopTitleLabel("topTitleLabel") : createTitleLabel("topTitleLabel"));
        topTitleLabel.setRenderBodyOnly(true);
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

	private void addCssContainers() {
        add(new CachingStrategyLink("resetCss"));
        add(new WebMarkupContainer("metaIE").add(new AttributeAppender("content", getMetaIE())));

        WebMarkupContainer legacyCss = new WebMarkupContainer("legacyCss") {
            { setRenderBodyOnly(true);
            }
            @Override public boolean isVisible() {
                return useLegacyCss();
            }
        };
        legacyCss.add(new CachingStrategyLink("fieldIdCss"));
        legacyCss.add(new CachingStrategyLink("fieldIdIE6Css"));
        legacyCss.add(new CachingStrategyLink("fieldIdIE7Css"));
        add(legacyCss);

        WebMarkupContainer newCss = new WebMarkupContainer("newCss") {
            { setRenderBodyOnly(true); }

            @Override public boolean isVisible() {
                return !useLegacyCss();
            }
        };
        newCss.add(new CachingStrategyLink("layoutCss"));
        newCss.add(new CachingStrategyLink("feedbackErrorsCss"));
        add(newCss);

        WebMarkupContainer siteWideCss = new WebMarkupContainer("siteWideCss") {
            { setRenderBodyOnly(true); }

            @Override public boolean isVisible() {
                return useSiteWideCss();
            }
        };
        siteWideCss.add(new CachingStrategyLink("siteWideCss"));
        add(siteWideCss);

        add(new CachingStrategyLink("defaultCss"));
        add(new CachingStrategyLink("commonIEJs","src"));  ///FIX SO APPENDS TO SRC ATTRIBUTE!!!
        add(new CachingStrategyLink("dropDownCss"));
        add(new CachingStrategyLink("dropDownIECss"));
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
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("templatesLink", TemplatesPage.class));
        subMenuContainer.add(createTemplatesSubMenu());
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("widgetsLink", WidgetsPage.class));
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("securityLink", SecurityPage.class));
        subMenuContainer.add(createLotoSubMenu());
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("translationsLink", AssetTypeGroupTranslationsPage.class));
        subMenuContainer.add(createActionsSubMenu());
        //subMenuContainer.add(createSmartSearch("smartSearchContainer"));
        createSecuritySubMenu(subMenuContainer);
        
        container.add(subMenuContainer);
        
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
                setResponsePage(SmartSearchListPage.class, PageParametersBuilder.param("searchTerm", autoCompleteSearch.getAutocompleteField().getConvertedInput()));
            }
        };

        container.add(form);
        form.add(autoCompleteSearch);

        return container;
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
        container.add(new BookmarkablePageLink("eventTypeGroups", EventTypeGroupListPage.class));
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
        add(new NavigationBar(navBarId));
    }

//	protected ConfigurationProvider getConfigurationProvider() {
//		if (configurationProvider==null) {
//			configurationProvider = ConfigService.getInstance();
//		}
//		return configurationProvider;
//	}
//
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

        response.renderCSSReference("style/legacy/colorbox.css");

        if (configService.getBoolean(ConfigEntry.APPTEGIC_ENABLED)) {
            SessionUser sessionUser = getSessionUser();

            Map<String, String> apptegicParams = new HashMap<String, String>();
            apptegicParams.put("user", sessionUser.getName());
            apptegicParams.put("company", sessionUser.getTenant().getName());
            apptegicParams.put("userType", sessionUser.getUserTypeLabel());
            apptegicParams.put("accountType", sessionUser.getAccountType());
            apptegicParams.put("apptegicDataset", configService.getString(ConfigEntry.APPTEGIC_DATASET));

            TextTemplate apptegicTemplate = new PackageTextTemplate(FieldIDFrontEndPage.class, "apptegic.js");
            response.renderJavaScript(apptegicTemplate.asString(apptegicParams), null);
        }

        String headerScript = configService.getString(ConfigEntry.HEADER_SCRIPT, getTenantId());

        if (headerScript != null && !headerScript.isEmpty()) {
            response.renderOnDomReadyJavaScript(headerScript);
        }
    }

    protected void renderJqueryJavaScriptReference(IHeaderResponse response) {
        response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
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

    // Ideally these will both be unneeded by all pages, After we convert to layout.css from site_wide.css and fieldid.css
    // (both site_wide and fieldid have accumulated a ton of irrelevant stuff that may be used only on one page and breaks new pages).
    protected boolean useLegacyCss() {
        return true;
    }

    protected boolean useSiteWideCss() {
        return true;
    }
    
    protected Component setLeftPanelContent(Component c) {
        Preconditions.checkArgument(LEFT_PANEL_ID.equals(c.getId()), " you must use '" + LEFT_PANEL_ID + "' as your left panel id");
    	replace(c.setVisible(true));
        if (c instanceof HasLeftPanelController) {
            HasLeftPanelController lpc = (HasLeftPanelController)c;
            Component controller = lpc.getLeftPanelController(LEFT_PANEL_CONTROLLER_ID);
            replace(controller.setVisible(true));
        }
        return c;
    }

    protected Component getBottomPanelContent() {
        return get(BOTTOM_PANEL_ID);
    }

    protected Component setBottomPanelContent(Component c) {
        Preconditions.checkArgument(BOTTOM_PANEL_ID.equals(c.getId()), " you must use '" + BOTTOM_PANEL_ID + "' as your bottom panel id");
        replace(c.setVisible(true));
        return c;
    }

    protected Component setSubMenuContent(Component c) {
        Preconditions.checkArgument(SUB_MENU_ID.equals(c.getId()), "you must use " + SUB_MENU_ID + "'as your sub menu id.");
        replace(c.setVisible(true));
        return c;
    }

    public TopFeedbackPanel getTopFeedbackPanel() {
        return topFeedbackPanel;
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


    class Header extends Fragment {

        public Header(String id) {
            super(id, "legacyHeader", FieldIDFrontEndPage.this);

            SessionUser sessionUser = getSessionUser();

            boolean inspectionEnabled = getSecurityGuard().isInspectionsEnabled();
            boolean trendingEnabled = getSecurityGuard().isCriteriaTrendsEnabled();
            boolean advancedEventSearchEnabled = getSecurityGuard().isAdvancedEventSearchEnabled();
            boolean extraEventLinksAvailable = trendingEnabled || advancedEventSearchEnabled;

            // header logo and links
            add(new StaticImage("tenantLogo", new Model<String>(s3Service.getBrandingLogoURL().toString())).setEscapeModelStrings(false));

            add(new Label("loggedInUsernameLabel", sessionUser.getName()));
            addSpeedIdentifyLinks(sessionUser);

            add(new AjaxLink<Void>("languageSelection") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    languageSelectionModalWindow.show(target);
                }
            }.setVisible(selectLanguagePanel.hasLanguagesToDisplay()));

            add(new ExternalLink("support", getSupportUrl(), new FIDLabelModel("label.support").getObject()));

            add(new SavedItemsDropdown("savedItemsDropdown"));

            // menu bar links: Start Event, Search, Reporting, Places, LOTO, Safety Network, Jobs, Setup
            add(new WebMarkupContainer("startEventLinkContainer").setVisible(sessionUser.hasAccess("createevent") && inspectionEnabled));

            add(new BookmarkablePageLink<Void>("assetSearchLink", SearchPage.class));

            BookmarkablePageLink reportingLink = new BookmarkablePageLink<Void>("reportingLink", ReportPage.class);
            reportingLink.setVisible(inspectionEnabled);
            reportingLink.add(new Image("reporting-down-arrow", new ContextRelativeResource("/images/down-arrow.png")).setVisible(extraEventLinksAvailable && inspectionEnabled));
            add(reportingLink);

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
        }


        private void addSpeedIdentifyLinks(SessionUser sessionUser) {
            WebMarkupContainer identifyMenuContainer = new WebMarkupContainer("identifyMenuContainer");
            identifyMenuContainer.setVisible(sessionUser.hasAccess("tag"));

            if (getSecurityGuard().isIntegrationEnabled()) {
                identifyMenuContainer.add(new ExternalLink("identifyLink", "/fieldid/identify.action"));
            } else {
                identifyMenuContainer.add(new BookmarkablePageLink("identifyLink", IdentifyOrEditAssetPage.class));
            }

            add(identifyMenuContainer);
        }

        private Component createLotoLinkContainer() {
            WebMarkupContainer lotoLinkContainer = new WebMarkupContainer("lotoLinkContainer");

            lotoLinkContainer.add(new BookmarkablePageLink<Void>("procedureSearchLink", ProcedureSearchPage.class));
            lotoLinkContainer.add(new BookmarkablePageLink<Void>("procedureList", PublishedListAllPage.class));
            lotoLinkContainer.add(new BookmarkablePageLink<Void>("upcomingAuditsLink", ProcedureAuditListPage.class));
            lotoLinkContainer.add(new BookmarkablePageLink<Void>("procedureAwaitingApprovalLink", ProcedureWaitingApprovalsPage.class));
            lotoLinkContainer.setVisible(getSecurityGuard().isLotoEnabled());

            return lotoLinkContainer;
        }

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




}
