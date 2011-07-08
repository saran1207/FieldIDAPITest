package com.n4systems.fieldid.wicket.pages;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.n4systems.fieldid.wicket.pages.reporting.ReportingPage;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RedirectToUrlException;
import org.apache.wicket.markup.ComponentTag;
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

import com.n4systems.fieldid.utils.UrlArchive;
import com.n4systems.fieldid.wicket.components.feedback.TopFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.model.Tenant;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class FieldIDLoggedInPage extends FieldIDWicketPage {

    private static String versionString;
    private Label titleLabel;
    
    public FieldIDLoggedInPage(PageParameters params) {
        super(params);

        SessionUser sessionUser = getSessionUser();

        if (sessionUser == null) {
            new UrlArchive("preLoginContext", getServletRequest(), getServletRequest().getSession()).storeUrl();
            throw new RedirectToUrlException("/login.action");
        }

        addClickTaleScripts();

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
        addNavBar("navBar");
        add(new Label("loggedInUsernameLabel", sessionUser.getUserName()));
        add(new WebMarkupContainer("startEventLinkContainer").setVisible(sessionUser.hasAccess("createevent") || sessionUser.hasAccess("editevent")));
        add(createSetupLinkContainer(sessionUser));
        add(new WebMarkupContainer("jobsLinkContainer").setVisible(getSecurityGuard().isProjectsEnabled()));
        add(new WebMarkupContainer("safetyNetworkLinkContainer").setVisible(getUserSecurityGuard().isAllowedManageSafetyNetwork()));

        add(JavascriptPackageResource.getHeaderContribution("javascript/sessionTimeout.js"));
        
        add(new StaticImage("tenantLogo", new Model<String>( "/fieldid/file/downloadTenantLogo.action?uniqueID=" + getSessionUser().getTenant().getId() ) ) );
    }

    protected void storePageParameters(PageParameters params) {
    }

    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, "Field ID");
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

    public FieldIDLoggedInPage() {
        this(null);
    }

    protected HttpServletRequest getServletRequest() {
        return getWebRequestCycle().getWebRequest().getHttpServletRequest();
    }

    private String loadVersionNumber(boolean devMode) {
        try {
            InputStream propsStream = FieldIDLoggedInPage.class.getResourceAsStream("/com/package.properties");
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
            String systemDomain = getConfigContext().getString(ConfigEntry.SYSTEM_DOMAIN);
            boolean devMode = !(serverName.toLowerCase().endsWith(systemDomain));
            versionString = loadVersionNumber(devMode);
        }
        return versionString;
    }

	protected ConfigContext getConfigContext() {
		return ConfigContext.getCurrentContext();
	}

    @Override
    public void renderHead(HtmlHeaderContainer container) {
        super.renderHead(container);

        StringBuffer javascriptBuffer = new StringBuffer();
        Integer timeoutTime = ConfigContext.getCurrentContext().getInteger(ConfigEntry.ACTIVE_SESSION_TIME_OUT);
        String loginLightboxTitle = getApplication().getResourceSettings().getLocalizer().getString("title.sessionexpired", null);
        javascriptBuffer.append("loggedInUserName = '").append(getSessionUser().getUserName()).append("';\n");
        javascriptBuffer.append("tenantName = '").append(getSessionUser().getTenant().getName()).append("';\n");
        javascriptBuffer.append("sessionTimeOut = ").append(timeoutTime).append(";\n");
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
        add(new Label("clickTaleStart", ConfigContext.getCurrentContext().getString(ConfigEntry.CLICKTALE_START)).setEscapeModelStrings(false));
        add(new Label("clickTaleEnd", ConfigContext.getCurrentContext().getString(ConfigEntry.CLICKTALE_END)).setEscapeModelStrings(false));
    }

    static class StaticImage extends WebComponent {
        public StaticImage(String id, IModel<String> urlModel) {
            super( id, urlModel );
        }

        protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag( tag );
            checkComponentTag( tag, "img" );
            tag.put( "src", getDefaultModelObjectAsString() );
        }
    }

}
