package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.utils.UrlArchive;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RedirectToUrlException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import rfid.web.helper.SessionUser;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

public class FieldIDLoggedInPage extends FieldIDWicketPage {

    private static String versionString;

    public FieldIDLoggedInPage(PageParameters params) {
        super(params);

        SessionUser sessionUser = getSessionUser();

        if (sessionUser == null) {
            new UrlArchive("preLoginContext", getServletRequest(), getServletRequest().getSession()).storeUrl();
            throw new RedirectToUrlException("/login.action");
        }

        add(new Label("versionLabel", getVersionString(getServletRequest())));

        boolean displayInvite = sessionUser.isEmployeeUser() && getUserSecurityGuard().isAllowedManageSafetyNetwork();
        boolean displayUpgrade = !displayInvite && sessionUser.isAnEndUser();

        add(new WebMarkupContainer("topInviteLinkContainer").setVisible(displayInvite));
        add(new WebMarkupContainer("topUpgradeLinkContainer").setVisible(displayUpgrade));
        add(new WebMarkupContainer("topFieldIDStoreLink").setVisible(getUserSecurityGuard().isAllowedAccessWebStore()));

        addSpeedIdentifyLinks();

        addNavBar("navBar");
        add(new Label("loggedInUsernameLabel", sessionUser.getUserName()));
        add(new WebMarkupContainer("startEventLinkContainer").setVisible(sessionUser.hasAccess("createevent") || sessionUser.hasAccess("editevent")));
        add(createSetupLinkContainer(sessionUser));
        add(new WebMarkupContainer("jobsLinkContainer").setVisible(getSecurityGuard().isProjectsEnabled()));
        add(new WebMarkupContainer("safetyNetworkLinkContainer").setVisible(getUserSecurityGuard().isAllowedManageSafetyNetwork()));
    }

    private void addSpeedIdentifyLinks() {
        boolean integration = getSecurityGuard().isIntegrationEnabled();
        WebMarkupContainer identifySectionContainer = new WebMarkupContainer("identifyLinksContainer");
        identifySectionContainer.setVisible(getSessionUser().hasAccess("tag"));
        identifySectionContainer.add(new WebMarkupContainer("identifyLinkContainer").setVisible(integration));
        identifySectionContainer.add(new WebMarkupContainer("addAssetLinkContainer").setVisible(!integration));
        add(identifySectionContainer);
    }

    private Component createSetupLinkContainer(SessionUser sessionUser) {
        boolean manageSystemConfig = sessionUser.hasAccess("managesystemconfig");
        WebMarkupContainer container = new WebMarkupContainer("setupLinkContainer");
        if (manageSystemConfig) {
            container.add(new BookmarkablePageLink<WebPage>("setupLink", SettingsPage.class));
        } else {
            container.add(new BookmarkablePageLink<WebPage>("setupLink", OwnersUsersLocationsPage.class));
        }
        return container;
    }

    protected void addNavBar(String navBarId) {
        add(new WebMarkupContainer(navBarId));
    }

    public FieldIDLoggedInPage() {
        this(null);
    }

    private HttpServletRequest getServletRequest() {
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

}
