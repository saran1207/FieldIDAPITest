package com.n4systems.fieldid.wicket.pages;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.UIConstants;
import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.version.FieldIdVersion;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.feedback.TopFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.saveditems.SavedItemsDropdown;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchPage;
import com.n4systems.fieldid.wicket.pages.setup.*;
import com.n4systems.fieldid.wicket.pages.setup.columnlayout.ColumnsLayoutPage;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.columns.ReportType;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.services.ConfigService;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;
import rfid.web.helper.SessionUser;

import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;

@SuppressWarnings("serial")
public class FieldIDFrontEndPage extends FieldIDAuthenticatedPage implements UIConstants {

    public static final String SUB_MENU_ID = "subMenu";
    public static final String LEFT_PANEL_ID = "leftPanel";
    private static final String LEFT_PANEL_CONTROLLER_ID = "leftPanelController";

    @SpringBean
	private ConfigService configService;

	@SpringBean
	private UserLimitService userLimitService;


    private Label titleLabel;
	private Label topTitleLabel;
    private ConfigurationProvider configurationProvider;


    public FieldIDFrontEndPage() {
        this(null, null);
    }

    public FieldIDFrontEndPage(ConfigurationProvider configurationProvider) {
        this(null, configurationProvider);
    }

    public FieldIDFrontEndPage(PageParameters params) {
    	this(params,null);
    }
    
    public FieldIDFrontEndPage(PageParameters params, ConfigurationProvider configurationProvider) {
        super(params);
        setConfigurationProvider(configurationProvider);

        add(new GoogleAnalyticsContainer("googleAnalyticsScripts"));
        
        SessionUser sessionUser = getSessionUser();

        add(new WebMarkupContainer(LEFT_PANEL_ID).setVisible(false));
        add(new WebMarkupContainer(SUB_MENU_ID).setVisible(false));
        add(new WebMarkupContainer(LEFT_PANEL_CONTROLLER_ID).setVisible(false));
        addCssContainers();

        add(new BookmarkablePageLink<Void>("reportingLink", ReportPage.class));
        add(new BookmarkablePageLink<Void>("assetSearchLink", SearchPage.class));

        add(new TopFeedbackPanel("topFeedbackPanel"));
        add(new Label("versionLabel", FieldIdVersion.getVersion()));


        add(new ExternalLink("support", getSupportUrl(), getString("label.support") ));

        addSpeedIdentifyLinks(sessionUser);

        storePageParameters(params);

        add(titleLabel = createTitleLabel("titleLabel"));
        titleLabel.setRenderBodyOnly(true);

        add(topTitleLabel = createTitleLabel("topTitleLabel"));
        topTitleLabel.setRenderBodyOnly(true);

        add(createHeaderLink("headerLink", "headerLinkLabel"));
        add(createBackToLink("backToLink", "backToLinkLabel"));
        addNavBar("navBar");
        add(new Label("loggedInUsernameLabel", sessionUser.getName()));
        add(new WebMarkupContainer("startEventLinkContainer").setVisible(sessionUser.hasAccess("createevent") || sessionUser.hasAccess("editevent")));
        add(createSetupLinkContainer(sessionUser));
        add(new WebMarkupContainer("jobsLinkContainer").setVisible(getSecurityGuard().isProjectsEnabled()));
        add(new WebMarkupContainer("safetyNetworkLinkContainer").setVisible(getUserSecurityGuard().isAllowedManageSafetyNetwork()));

        add(new SavedItemsDropdown("savedItemsDropdown"));

        add(new StaticImage("tenantLogo", new Model<String>( "/fieldid/file/downloadTenantLogo.action?uniqueID=" + getSessionUser().getTenant().getId() ) ) );

        add(createRelogLink());
    }

	private String getSupportUrl() {
    	TenantSettings settings = getTenant().getSettings();
		return settings.getSupportUrl()==null ? DEFAULT_SUPPORT_URL :settings.getSupportUrl();
	}

	private void addCssContainers() {
        add(new WebMarkupContainer("legacyCss") {
            { setRenderBodyOnly(true); }
            @Override
            public boolean isVisible() {
                return useLegacyCss();
            }
        });
        add(new WebMarkupContainer("newCss") {
            { setRenderBodyOnly(true); }
            @Override
            public boolean isVisible() {
                return !useLegacyCss();
            }
        });
        add(new WebMarkupContainer("siteWideCss") {
            { setRenderBodyOnly(true); }
            @Override
            public boolean isVisible() {
                return useSiteWideCss();
            }
        });
    }

    private Component createRelogLink() {
        PageParameters pageParameters = new PageParameters();
        pageParameters.add("user", FieldIDSession.get().getSessionUser().getUserName());
        pageParameters.add("tenant", FieldIDSession.get().getSessionUser().getTenant().getName());
        return new NonWicketLink("relogLink", "aHtml/login.action");
    }

    protected void storePageParameters(PageParameters params) {
    }

    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, "Field ID");
    }

    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new WebMarkupContainer(linkId).setVisible(false);
    }
    
    protected Component createHeaderLink(String id, String label) {
		return new WebMarkupContainer(id).setVisible(false);
	}

    private void addSpeedIdentifyLinks(SessionUser sessionUser) {
        WebMarkupContainer identifyMenuContainer = new WebMarkupContainer("identifyMenuContainer");
        identifyMenuContainer.setVisible(sessionUser.hasAccess("tag"));

        String url;
        if(getSecurityGuard().isIntegrationEnabled()) {
        	url = "/fieldid/identify.action";
        }else {
        	url = "/fieldid/assetAdd.action";
        }
        identifyMenuContainer.add(new ExternalLink("identifyLink", url));
        identifyMenuContainer.add(new ExternalLink("subMenuIdentifyLink", url));
        add(identifyMenuContainer);
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
        
        container.add(new BookmarkablePageLink<ColumnsLayoutPage>("assetLayoutLink", ColumnsLayoutPage.class, param("type", ReportType.ASSET)));
        container.add(new BookmarkablePageLink<ColumnsLayoutPage>("eventLayoutLink", ColumnsLayoutPage.class, param("type", ReportType.EVENT)));
        container.add(new BookmarkablePageLink<ColumnsLayoutPage>("scheduleLayoutLink", ColumnsLayoutPage.class, param("type", ReportType.SCHEDULE)));

        return container;
	}

	private Component createAssetEventsSubMenu() {
        WebMarkupContainer container = new WebMarkupContainer("assetsEventsSubMenuContainer");

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
        container.add(new WebMarkupContainer("manageUsersContainer").setVisible(canManageSystemUsers));
        container.add(new WebMarkupContainer("manageUserRegistrationsContainer").setVisible(canManageSystemUsers && userLimitService.isReadOnlyUsersEnabled()));
        container.add(new WebMarkupContainer("managePredefinedLocationsContainer").setVisible(getSessionUser().hasAccess("managesystemconfig") && advancedLocationEnabled));

    	return container;
    }

    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId));
    }
    
	protected ConfigurationProvider getConfigurationProvider() {
		if (configurationProvider==null) { 
			configurationProvider = ConfigContext.getCurrentContext(); 
		}		
		return configurationProvider; 
	}
	
	@Deprecated // for testing only to get around static implementation of configContext.
    public void setConfigurationProvider(ConfigurationProvider configurationProvider) {
		this.configurationProvider = configurationProvider;
	}

    @Override
    public void renderHead(IHeaderResponse response) {
        StringBuffer javascriptBuffer = new StringBuffer();
        Integer timeoutTime = getConfigurationProvider().getInteger(ConfigEntry.ACTIVE_SESSION_TIME_OUT);
        String loginLightboxTitle = getApplication().getResourceSettings().getLocalizer().getString("title.sessionexpired", null);
        javascriptBuffer.append("loggedInUserName = '").append(getSessionUser().getUserName()).append("';\n");
        javascriptBuffer.append("tenantName = '").append(getSessionUser().getTenant().getName()).append("';\n");
        javascriptBuffer.append("sessionTimeOut = ").append(timeoutTime).append(";\n");
        javascriptBuffer.append("sessionTestUrl = '/fieldid/ajax/testSession.action';").append(";\n");
        javascriptBuffer.append("loginWindowTitle = '").append(loginLightboxTitle).append("';\n");

        response.renderJavaScript(javascriptBuffer.toString(), null);

        response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());

        response.renderJavaScriptReference("javascript/common-jquery.js");
        response.renderJavaScriptReference("javascript/jquery.at_intervals.js");
        response.renderJavaScriptReference("javascript/sessionTimeout-jquery.js");
        response.renderJavaScriptReference("javascript/json2.js");
        response.renderJavaScriptReference("javascript/jquery.at_intervals.js");
        response.renderJavaScriptReference("javascript/jquery.colorbox.js");
        response.renderJavaScriptReference("javascript/jquery.dropdown.js");

        response.renderCSSReference("style/colorbox.css");
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
        	if (!hasBeenRendered()) {
        		setVisible(configService.getBoolean(ConfigEntry.GOOGLE_ANALYTICS_ENABLED));    	    		
        	}
        	super.onBeforeRender();  // note : call super at END of override.  see wicket docs.
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
    
    // Ideally these will both be unneeded by all pages, After we convert to layout.css from site_wide.css and fieldid.css
    // (both site_wide and fieldid have accumulated a ton of irrelevant stuff that may be used only on one page and breaks new pages).
    protected boolean useLegacyCss() {
        return true;
    }

    protected boolean useSiteWideCss() {
        return true;
    }
    
    protected void setLeftPanelContent(Component c) {
        Preconditions.checkArgument(LEFT_PANEL_ID.equals(c.getId()), " you must use '" + LEFT_PANEL_ID + "' as your left panel id");
    	replace(c.setVisible(true));
        if (c instanceof HasLeftPanelController) {
            HasLeftPanelController lpc = (HasLeftPanelController)c;
            Component controller = lpc.getLeftPanelController(LEFT_PANEL_CONTROLLER_ID);
            replace(controller.setVisible(true));
        }
    }

    protected void setSubMenuContent(Component c) {
        Preconditions.checkArgument(SUB_MENU_ID.equals(c.getId()), "you must use " + SUB_MENU_ID + "'as your sub menu id.");
        replace(c.setVisible(true));
    }

}
