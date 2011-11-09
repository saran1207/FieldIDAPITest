package com.n4systems.fieldid.wicket.pages;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.feedback.TopFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingPage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.model.Tenant;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;

public class FieldIDFrontEndPage extends FieldIDAuthenticatedPage {

    private static String versionString;
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
    
    // UGH DD: nned to refactor configurationProvider more.  next phase need to refactor this out into spring bean or some 
    //   non-static reference to ConfigContext that is currently throughout pages. 
    public FieldIDFrontEndPage(PageParameters params, ConfigurationProvider configurationProvider) {
        super(params);
        setConfigurationProvider(configurationProvider);

        SessionUser sessionUser = getSessionUser();

        addClickTaleScripts();
        addCssContainers();

        add(JavascriptPackageResource.getHeaderContribution("javascript/flot/jquery.min.js"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/jquery.at_intervals.js"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/sessionTimeout-jquery.js"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/json2.js"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/common-jquery.js"));
        add(JavascriptPackageResource.getHeaderContribution("javascript/jquery.colorbox.js"));
        add(CSSPackageResource.getHeaderContribution("style/colorbox.css"));

        add(new BookmarkablePageLink<Void>("reportingLink", ReportingPage.class));

        add(new TopFeedbackPanel("topFeedbackPanel"));
        add(new Label("versionLabel", getVersionString(getServletRequest())));

        boolean displayInvite = sessionUser.isEmployeeUser() && getUserSecurityGuard().isAllowedManageSafetyNetwork();
        boolean displayUpgrade = !displayInvite && sessionUser.isAnEndUser();

        add(new WebMarkupContainer("topInviteLinkContainer").setVisible(displayInvite));
        add(new WebMarkupContainer("topUpgradeLinkContainer").setVisible(displayUpgrade));
        add(new WebMarkupContainer("topFieldIDStoreLink").setVisible(getUserSecurityGuard().isAllowedAccessWebStore()));

        addSpeedIdentifyLinks();

        storePageParameters(params);

        add(titleLabel = createTitleLabel("titleLabel"));
        titleLabel.setRenderBodyOnly(true);

        add(topTitleLabel = createTitleLabel("topTitleLabel"));
        topTitleLabel.setRenderBodyOnly(true);

        add(createBackToLink("backToLink", "backToLinkLabel"));
        addNavBar("navBar");
        add(new Label("loggedInUsernameLabel", sessionUser.getUserName()));
        add(new WebMarkupContainer("startEventLinkContainer").setVisible(sessionUser.hasAccess("createevent") || sessionUser.hasAccess("editevent")));
        add(createSetupLinkContainer(sessionUser));
        add(new WebMarkupContainer("jobsLinkContainer").setVisible(getSecurityGuard().isProjectsEnabled()));
        add(new WebMarkupContainer("safetyNetworkLinkContainer").setVisible(getUserSecurityGuard().isAllowedManageSafetyNetwork()));

        add(new StaticImage("tenantLogo", new Model<String>( "/fieldid/file/downloadTenantLogo.action?uniqueID=" + getSessionUser().getTenant().getId() ) ) );

        add(createRelogLink());
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
        boolean integration = getSecurityGuard().isIntegrationEnabled();
        WebMarkupContainer identifySectionContainer = new WebMarkupContainer("identifyLinksContainer");
        identifySectionContainer.setRenderBodyOnly(true);
        identifySectionContainer.setVisible(getSessionUser().hasAccess("tag"));
        identifySectionContainer.add(new WebMarkupContainer("identifyLinkContainer").setVisible(integration));
        identifySectionContainer.add(new WebMarkupContainer("addAssetLinkContainer").setVisible(!integration));
        add(identifySectionContainer);
    }
    
    private Component createSetupLinkContainer(SessionUser sessionUser) {
        boolean hasSetupAccess = sessionUser.hasSetupAccess();
        boolean manageSystemConfig = sessionUser.hasAccess("managesystemconfig");
        WebMarkupContainer container = new WebMarkupContainer("setupLinkContainer");
        if (hasSetupAccess && manageSystemConfig) {
            container.add(new BookmarkablePageLink<WebPage>("setupLink", SettingsPage.class));
        } else if (hasSetupAccess) {
            container.add(new BookmarkablePageLink<WebPage>("setupLink", OwnersUsersLocationsPage.class));
        } else {
            container.setVisible(false);
        }
        return container;
    }

    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId));
    }

    private String loadVersionNumber(boolean devMode) {
        try {
            InputStream propsStream = FieldIDFrontEndPage.class.getResourceAsStream("/com/package.properties");
            Properties props = new Properties();
            props.load(propsStream);
            String versionNumber = props.getProperty("app.versionnumber");
            if (devMode) {
                versionNumber += "-" + props.getProperty("app.buildnumber");
            }
            return versionNumber;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UNKNOWN";
    }

    private String getVersionString(HttpServletRequest servletRequest) {
        if (versionString == null) {
            String serverName = getServletRequest().getServerName();
            String systemDomain = getConfigurationProvider().getString(ConfigEntry.SYSTEM_DOMAIN);
            boolean devMode =  StringUtils.isNotBlank(systemDomain) && !(serverName.toLowerCase().endsWith(systemDomain));
            versionString = loadVersionNumber(devMode);
        }
        return versionString;
    }

    
    // TODO DD : can we use a springleton to solve this? would make it much more testable an
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
    public void renderHead(HtmlHeaderContainer container) {
        super.renderHead(container);

        StringBuffer javascriptBuffer = new StringBuffer();
        Integer timeoutTime = getConfigurationProvider().getInteger(ConfigEntry.ACTIVE_SESSION_TIME_OUT);
        String loginLightboxTitle = getApplication().getResourceSettings().getLocalizer().getString("title.sessionexpired", null);
        javascriptBuffer.append("loggedInUserName = '").append(getSessionUser().getUserName()).append("';\n");
        javascriptBuffer.append("tenantName = '").append(getSessionUser().getTenant().getName()).append("';\n");
        javascriptBuffer.append("sessionTimeOut = ").append(timeoutTime).append(";\n");
        javascriptBuffer.append("sessionTestUrl = '/fieldid/ajax/testSession.action';").append(";\n");
        javascriptBuffer.append("loginWindowTitle = '").append(loginLightboxTitle).append("';\n");

        container.getHeaderResponse().renderJavascript(javascriptBuffer.toString(), null);
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

    protected boolean useLegacyCss() {
        return true;
    }

}
