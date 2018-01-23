package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.security.Permissions;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.PanelCachingTab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import java.util.ArrayList;
import java.util.List;


@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_END_USERS})
public class CustomerImportPage extends FieldIDFrontEndPage {

    public static final String INITIAL_TAB_SELECTION_KEY = "InitialTabSelection";
    public static final String SHOW_CUSTOMERLIST_PAGE = "ShowCustomerListPage";
    public static final String SHOW_IMPORTEXPORT_PAGE = "ShowImportExportPage";

    private static final Logger logger = Logger.getLogger(CustomerImportPage.class);

    private List<String> titleLabelsByTabIndex;
    private int currentlySelectedTab;
    private IModel<String> currentTitleModel;
    private IModel<User> currentUserModel;
    private IModel<SecurityFilter> securityFilterModel;
    private IModel<WebSessionMap> webSessionMapModel;
    private String initialTabSelection;
    private FeedbackPanel feedbackPanel;

    private LoaderFactory loaderFactory;

    public CustomerImportPage(PageParameters params) {
        super(params);
        currentlySelectedTab = 0;
        titleLabelsByTabIndex = new ArrayList<String>();
        StringValue tabSelection = params.get(INITIAL_TAB_SELECTION_KEY);
        if (tabSelection != null)
            initialTabSelection = tabSelection.toString();
        createModels();
        addFeedbackPanel();
    }


    private void addFeedbackPanel() {
         /* Existing top feedback panel is in the correct place for our messages but doesn't
            get recognized as a feedback panel for our messages. */
        remove(getTopFeedbackPanel());
        feedbackPanel = new FeedbackPanel("topFeedbackPanel");
        feedbackPanel.add(new AttributeAppender("style", new Model("text-align: center; color:red; padding: 0px 10px"), " "));
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
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
        response.renderCSS("li .feedbackPanelERROR {text-align: center: display:block; color: red;}", null);

         /* We want the first two tabs to appear on the left and the last two on the right */
        response.renderCSS(".tab-row {width: 100%; padding-top:10px;}", null);
        response.renderCSS("div.wicket-tabpanel div.tab-row li.tab2 {float: right}", null);
        response.renderCSS("div.wicket-tabpanel div.tab-row li.tab3.last {float: right}", null);

    }

    @Override
    protected Label createTitleLabel(String labelId) {
         /* Title will change depending on which tab is selected so provide title through a model */
        return new Label(labelId, currentTitleModel);
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, OwnersUsersLocationsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected void addNavBar(String navBarId) {

        int preSelectedTab = -1;
        final List<ITab> tabs = new ArrayList<ITab>();

        tabs.add(new PanelCachingTab(new AbstractTab(new FIDLabelModel("nav.view_all")) {
            public Panel getPanel(String panelId) {
                return new CustomerListPanel(panelId, webSessionMapModel, getLoaderFactory());
            }
        }));
        titleLabelsByTabIndex.add(new FIDLabelModel("title.customer_list_customer").getObject());
        if (SHOW_CUSTOMERLIST_PAGE.equals(initialTabSelection))
            preSelectedTab = 0;

        tabs.add(new PanelCachingTab(new AbstractTab(new FIDLabelModel("nav.view_all_archived")) {
            public Panel getPanel(String panelId) {
                return new CustomerArchivedListPanel(panelId, webSessionMapModel, getLoaderFactory());
            }
        }));
        titleLabelsByTabIndex.add(new FIDLabelModel("title.customer_list_archived_customer").getObject());

        tabs.add(new AbstractTab(new FIDLabelModel("nav.add")) {
            public Panel getPanel(String panelId)
            {
                return new CustomerEditPanel(panelId);
            }
        });
        titleLabelsByTabIndex.add("");

        tabs.add(new PanelCachingTab(new AbstractTab(new FIDLabelModel("nav.import_export")) {
            public Panel getPanel(String panelId)
            {
                return new CustomerImportPanel(panelId, currentUserModel, securityFilterModel);
            }
        }));
        titleLabelsByTabIndex.add(new FIDLabelModel("title.customer_import_export").getObject());
        if (SHOW_IMPORTEXPORT_PAGE.equals(initialTabSelection))
            preSelectedTab = 3;

        AjaxTabbedPanel tabbedPanel = new AjaxTabbedPanel("navBar", tabs) {
            @Override
            protected void onAjaxUpdate(AjaxRequestTarget target) {
                Session.get().cleanupFeedbackMessages();
                target.add(feedbackPanel);
                currentlySelectedTab = getSelectedTab();
                Object panel = tabs.get(getSelectedTab()).getPanel(TabbedPanel.TAB_PANEL_ID);
                if (panel instanceof WicketPanelAjaxUpdate) {
                    ((WicketPanelAjaxUpdate)panel).onAjaxUpdate(target);
                }
            }
        };

        tabbedPanel.add(new AttributeAppender("class", new Model("wicket-tabpanel"), " "));
        if (preSelectedTab > -1) {
            tabbedPanel.setSelectedTab(preSelectedTab);
        }
        add(tabbedPanel);
    }
    /* Create models for use by component panels as callbacks to get values obtainable only from this page.
     * This avoids adding these objects to the serialized state of the panels  */
    private void createModels() {
        currentTitleModel = new IModel<String>() {
            public String getObject() {
                if (currentlySelectedTab >= 0 && currentlySelectedTab < titleLabelsByTabIndex.size())
                    return titleLabelsByTabIndex.get(currentlySelectedTab);
                else
                    return "";
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }
        };
        currentUserModel = new IModel<User>() {
            public User getObject() {
                return getCurrentUser();
            }
            public void setObject(final User object) { }
            public void detach() {}
        };
        securityFilterModel = new IModel<SecurityFilter>() {
            public SecurityFilter getObject() {
                return getSecurityFilter();
            }
            public void setObject(final SecurityFilter object) { }
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

    @Override
    public WebSessionMap getWebSessionMap() {
        return new WebSessionMap(getServletRequest().getSession(false));
    }

    public LoaderFactory getLoaderFactory() {
        if (loaderFactory == null) {
            loaderFactory = new LoaderFactory(getSecurityFilter());
        }
        return loaderFactory;
    }
}

