package com.n4systems.fieldid.wicket.pages;

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

import com.n4systems.fieldid.UIConstants;
import com.n4systems.fieldid.version.FieldIdVersion;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DynamicPanel;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.feedback.TopFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.saveditems.SavedItemsDropdown;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchPage;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingPage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.fieldid.wicket.pages.setup.ImportPage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.fieldid.wicket.pages.setup.SecurityPage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.fieldid.wicket.pages.setup.TemplatesPage;
import com.n4systems.fieldid.wicket.pages.setup.WidgetsPage;
import com.n4systems.model.Tenant;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.services.ConfigService;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;

@SuppressWarnings("serial")
public class FieldIDFrontEndPage extends FieldIDAuthenticatedPage implements UIConstants {
	
	@SpringBean private ConfigService configService;
	
    private Label titleLabel;
	private Label topTitleLabel;
    private ConfigurationProvider configurationProvider;

	private DynamicPanel leftMenu;
	private DynamicPanel subMenu;

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

        add(leftMenu = new DynamicPanel("leftMenu"));
        add(subMenu = new DynamicPanel("subMenu"));
        addClickTaleScripts();
        addCssContainers();

        add(new BookmarkablePageLink<Void>("reportingLink", ReportingPage.class));
        add(new BookmarkablePageLink<Void>("assetSearchLink", AssetSearchPage.class));

        add(new TopFeedbackPanel("topFeedbackPanel"));
        add(new Label("versionLabel", FieldIdVersion.getVersion()));


        add(new ExternalLink("support", getSupportUrl(), getString("label.support") ));
        add(new WebMarkupContainer("topFieldIDStoreLink").setVisible(getUserSecurityGuard().isAllowedAccessWebStore()));

        addSpeedIdentifyLinks();

        storePageParameters(params);

        add(titleLabel = createTitleLabel("titleLabel"));
        titleLabel.setRenderBodyOnly(true);

        add(topTitleLabel = createTitleLabel("topTitleLabel"));
        topTitleLabel.setRenderBodyOnly(true);

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

    private void addSpeedIdentifyLinks() {
        String url;
        if(getSecurityGuard().isIntegrationEnabled()) {
        	url = "/fieldid/identify.action";
        }else {
        	url = "/fieldid/assetAdd.action";
        }
        add(new ExternalLink("identifyLink", url));
        
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
        
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("ownersUsersLocLink", OwnersUsersLocationsPage.class));
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("assetsEventsLink", AssetsAndEventsPage.class));
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("importLink", ImportPage.class));
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("templatesLink", TemplatesPage.class));
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("widgetsLink", WidgetsPage.class));
        subMenuContainer.add(new BookmarkablePageLink<WebPage>("securityLink", SecurityPage.class));
        
        container.add(subMenuContainer);
        
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
        response.renderJavaScriptReference("javascript/hoverIntent.js");
        response.renderJavaScriptReference("javascript/jquery.dropdown.js");

        response.renderCSSReference("style/colorbox.css");
    }

    protected Long getTenantId() {
        return getTenant().getId();
    }

    protected Tenant getTenant() {
        return getSessionUser().getTenant();
    }

    private void addClickTaleScripts() {
        add(new Label("clickTaleStart", getConfigurationProvider().getString(ConfigEntry.CLICKTALE_START)).setEscapeModelStrings(false));
        add(new Label("clickTaleEnd", getConfigurationProvider().getString(ConfigEntry.CLICKTALE_END)).setEscapeModelStrings(false));
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
    
    protected void setLeftMenuContent(Component c) { 
    	leftMenu.setContent(c);    	
    }
    
    protected void setSubMenuContent(Component c) { 
    	subMenu.setContent(c);
    }

}
