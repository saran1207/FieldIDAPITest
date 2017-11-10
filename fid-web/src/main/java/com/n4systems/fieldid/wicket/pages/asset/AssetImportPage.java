package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import org.apache.log4j.Logger;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import rfid.web.helper.SessionUser;

import java.util.ArrayList;
import java.util.List;


@UserPermissionFilter(userRequiresOneOf={Permissions.TAG})
public class AssetImportPage extends FieldIDFrontEndPage {

    private static final Logger logger = Logger.getLogger(AssetImportPage.class);

    public static String ASSET_TYPE_ID_KEY = "assetTypeId";
    public static String INITIAL_TAB = "initialTab";
    public static String ADD_WITH_ORDER_TAB = "addWithOrder";

    private StringValue preSelectedAssetTypeId;
    private int currentlySelectedTab;
    private IModel<String> currentTitleModel;
    private IModel<User> currentUserModel;
    private IModel<SessionUser> sessionUserModel;
    private IModel<SecurityFilter> securityFilterModel;
    private IModel<SystemSecurityGuard> securityGuardModel;
    private IModel<WebSessionMap> webSessionMapModel;
    private List<String> titleLabelsByTabIndex;
    private FeedbackPanel feedbackPanel;
    private boolean setAddWithOrderAsInitial;

    public AssetImportPage(PageParameters params) {
        super(params);
        currentlySelectedTab = 0;
        titleLabelsByTabIndex = new ArrayList<String>();
        preSelectedAssetTypeId = params.get(ASSET_TYPE_ID_KEY);
        StringValue initialTabSelection = params.get(INITIAL_TAB);
            if (!initialTabSelection.isNull() && ADD_WITH_ORDER_TAB.equals(initialTabSelection.toString()))
            setAddWithOrderAsInitial = true;
        else
            setAddWithOrderAsInitial = false;
        createModels();
        addFeedbackPanel();
    }

    private void addFeedbackPanel() {
         /* Existing top feedback panel is in the correct place for our messages but doesn't
            get recognized as a feedback panel for our messages. */
        remove(getTopFeedbackPanel());
        feedbackPanel = new FeedbackPanel("topFeedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        /* Title will change depending on which tab is selected so provide title through a model */
        return new Label(labelId, currentTitleModel);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/import.css");
        response.renderCSSReference("style/legacy/component/wicketTabbedPanel.css");
        response.renderCSS("div#pageContent {display: none}", null);
        response.renderCSS(".wicket-tabbed-panel-content {margin-top: 14px;}", null);
        response.renderCSS("li .feedbackPanelINFO {padding: 10px 0px 10px 0px;\n" +
                "text-align: center;\n" +
                "border: 1px solid #5fb336;\n" +
                "background-color: #e3f4db;\n" +
                "font-size: 13px;\n" +
                "display: block;\n" +
                "color: #333333;}", null);
    }

    @Override
    protected void addNavBar(String navBarId) {

        int preSelectedTab = -1;
        List<AbstractTab> tabs = new ArrayList<AbstractTab>();
        tabs.add(new AbstractTab(new FIDLabelModel("label.import")) {
            public Panel getPanel(String panelId)
            {
                return new AssetImportPanel(panelId, preSelectedAssetTypeId, currentUserModel, sessionUserModel,
                        securityFilterModel, webSessionMapModel);
            }
        });
        titleLabelsByTabIndex.add(new FIDLabelModel("title.asset_import").getObject());

        /* 'Add with Order' requires the integration feature to be enabled */
        boolean isIntegrationEnabled = getSecurityGuard().isExtendedFeatureEnabled(ExtendedFeature.Integration);
        if (isIntegrationEnabled) {
            tabs.add(new AbstractTab(new FIDLabelModel("nav.add_with_order")) {
                public Panel getPanel(String panelId) {
                    return new AddAssetWithOrderPanel(panelId, securityFilterModel, securityGuardModel, sessionUserModel);
                }
            });
            titleLabelsByTabIndex.add(new FIDLabelModel("nav.add_with_order").getObject());
            if (setAddWithOrderAsInitial)
                preSelectedTab = 1;
        }
        tabs.add(new AbstractTab(new FIDLabelModel("nav.add")) {
            public Panel getPanel(String panelId)
            {
                return new RedirectToNewAssetPage(panelId);
            }
        });
        titleLabelsByTabIndex.add(""); // selecting this tab leaves this page

        TabbedPanel tabbedPanel = new TabbedPanel("navBar", tabs) {
            protected void onModelChanged() {
                currentlySelectedTab = new Integer(getDefaultModelObjectAsString()).intValue();
            }
        };
        tabbedPanel.add(new AttributeAppender("class", new Model("wicket-tabpanel-right wicket-tabpanel"), " "));
        if (preSelectedTab > -1) {
                tabbedPanel.setSelectedTab(preSelectedTab);
        }
        add(tabbedPanel);
    }

    /* Create models for use by component panels to get values obtainable from this page.
     * This avoids adding these objects to the serialized state of the panels  */
    private void createModels() {
        currentTitleModel = new IModel<String>() {
            public String getObject() {
                if (currentlySelectedTab >=0 && currentlySelectedTab < titleLabelsByTabIndex.size())
                    return titleLabelsByTabIndex.get(currentlySelectedTab);
                else
                    return "";
            }
            public void setObject(final String object) { }
            public void detach() {}
        };
        currentUserModel = new IModel<User>() {
            public User getObject() {
                return getCurrentUser();
            }
            public void setObject(final User object) { }
            public void detach() {}
        };
        sessionUserModel = new IModel<SessionUser>() {
            public SessionUser getObject() {
                return getSessionUser();
            }
            public void setObject(final SessionUser object) { }
            public void detach() {}
        };
        securityFilterModel = new IModel<SecurityFilter>() {
            public SecurityFilter getObject() {
                return getSecurityFilter();
            }
            public void setObject(final SecurityFilter object) { }
            public void detach() {}
        };
        securityGuardModel = new IModel<SystemSecurityGuard>() {
            public SystemSecurityGuard getObject() {
                return getSecurityGuard();
            }
            public void setObject(final SystemSecurityGuard object) { }
            public void detach() {}
        };
        webSessionMapModel = new IModel<WebSessionMap>() {
            public WebSessionMap getObject() {
                return new WebSessionMap(getServletRequest().getSession(false));
            }
            public void setObject(final WebSessionMap object) { }
            public void detach() {}
        };
    }

}
