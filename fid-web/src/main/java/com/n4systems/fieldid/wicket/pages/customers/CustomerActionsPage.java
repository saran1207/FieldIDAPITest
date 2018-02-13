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
import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
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
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import rfid.web.helper.SessionUser;

import java.util.ArrayList;
import java.util.List;


@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_END_USERS})
public class CustomerActionsPage extends FieldIDFrontEndPage {

    public static final String INITIAL_TAB_SELECTION_KEY = "InitialTabSelection";
    public static final String SHOW_CUSTOMERLIST_PAGE = "ShowCustomerListPage";
    public static final String SHOW_IMPORTEXPORT_PAGE = "ShowImportExportPage";
    public static final String SHOW_CUSTOMERVIEW_PAGE = "ShowCustomerViewPage";
    public static final String INITIAL_CUSTOMER_ID = "id";

    private static final Logger logger = Logger.getLogger(CustomerActionsPage.class);

    private List<String> titleLabelsByTabIndex;
    private int currentlySelectedTab;
    private IModel<String> currentTitleModel;
    private IModel<User> currentUserModel;
    private IModel<SessionUser> sessionUserModel;
    private IModel<SecurityFilter> securityFilterModel;
    private IModel<WebSessionMap> webSessionMapModel;

    /**
     * The currently selected customerOrg. This is set in one of the following three cases since they change the focus
     * to an individual customer:
     * 1) In the 'View All' tab the user clicks the show customer link
     * 2) In the 'View All' tab the user clicks the edit link
     * 3) In the 'Add' tab the user clicks the 'save link
     * The selected customerOrg is reset to null if the user clicks on the 'View All', 'View Archived', 'Import/Export
     * or 'Add' tabs since the focus is no longer on an individual customer.
     */
    private IModel<Long> customerSelectedForEditModel;

    private String initialTabSelection;
    private Long initialCustomerId;
    private FeedbackPanel feedbackPanel;

    private LoaderFactory loaderFactory;

    public CustomerActionsPage(PageParameters params) {
        super(params);
        currentlySelectedTab = 0;
        titleLabelsByTabIndex = new ArrayList<String>();
        StringValue tabSelection = params.get(INITIAL_TAB_SELECTION_KEY);
        if (tabSelection != null && !tabSelection.isEmpty())
            initialTabSelection = tabSelection.toString();
        StringValue customerIdSelection = params.get(INITIAL_CUSTOMER_ID);
        if (customerIdSelection != null && !customerIdSelection.isEmpty())
            initialCustomerId = new Long(customerIdSelection.toString());
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

         /* We want the first four tabs to appear on the left (though all might not be visible) and the last two on the right */
        response.renderCSS(".tab-row {width: 100%; padding-top:10px;}", null);
        response.renderCSS("div.wicket-tabpanel div.tab-row li.tab7 {float: right}", null);
        response.renderCSS("div.wicket-tabpanel div.tab-row li.tab8.last {float: right}", null);

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

        /**
         * This page has 8 tabs - 'View All', 'View Archived', 'View', 'Edit', 'Divisions', 'Users', 'Import/Export', 'Add'
         *
         * The 'View', 'Edit', 'Divisions' and 'Users' tabs are normally hidden and become visible under these situations:
         *
         * 1) The user has just added a Customer in the 'Add' tab. All tabs become visible and the 'View' tab is selected.
         * 2) The user has clicked 'edit' for a Customer in the 'View All' tab. All tabs become visible and the 'Edit'
         *    tab is selected.
         * 3) If the user is on the 'View', 'Edit', 'Division' or 'Users' tab and they click on any of these four tabs
         *    they remain visible. If they click on any of the other tabs ('View All', 'View Archived', 'Import/Export'
         *    or 'Add' these tabs become invisible again.
         *
         * Note about the 'Edit' tab. Normally this tab has an edit panel. However, if the user clicks the 'merge'
         * link in the 'View' tab focus switches to the 'Edit' tab but its contents are a merge panel. So there are
         * two 'Edit' tabs and we show the appropriate one based on the user's actions. If while in the merge panel
         * the user clicks the tabs mentioned in (3) above the contents of the 'Edit' tab switch back to the
         * edit panel.
         */

        final int NO_TAB_SELECTION = -1;
        final int VIEW_ALL_TAB_INDEX = 0;
        final int VIEW_ARCHIVED_TAB_INDEX = 1;
        final int VIEW_TAB_INDEX = 2;
        final int EDIT_TAB_INDEX= 3;
        final int MERGE_TAB_INDEX = 4;
        final int DIVISION_TAB_INDEX = 5;
        final int USERS_TAB_INDEX = 6;
        final int ADD_TAB_INDEX = 7;
        final int IMPORT_EXPORT_TAB_INDEX = 8;

        int preSelectedTab = NO_TAB_SELECTION;

        final MutableBoolean showMergeTab = new MutableBoolean(false);

        final List<ITab> tabs = new ArrayList<ITab>();

        final TabbedPanel tabbedPanel = new TabbedPanel("navBar", tabs) {

            @Override
            protected void onBeforeRender() {
                int newTab = getSelectedTab();
                if (newTab == VIEW_ALL_TAB_INDEX || newTab == VIEW_ARCHIVED_TAB_INDEX || newTab == ADD_TAB_INDEX
                        || newTab == IMPORT_EXPORT_TAB_INDEX)
                    customerSelectedForEditModel.setObject(null); // cleanup any old value
                Session.get().cleanupFeedbackMessages();
                currentlySelectedTab = getSelectedTab();
                if (currentlySelectedTab != MERGE_TAB_INDEX)
                    showMergeTab.setValue(false);
                super.onBeforeRender();
            }
        };

        tabbedPanel.add(new AttributeAppender("class", new Model("wicket-tabpanel"), " "));
        if (preSelectedTab > -1) {
            tabbedPanel.setSelectedTab(preSelectedTab);
        }
        add(tabbedPanel);

        tabs.add(new PanelCachingTab(new AbstractTab(new FIDLabelModel("nav.view_all")) {
            @Override
            public Panel getPanel(String panelId) {
                return new CustomerListPanel(panelId, webSessionMapModel, getLoaderFactory()) {

                    @Override
                    protected void invokeCustomerEdit(Long customerId) {
                        customerSelectedForEditModel.setObject(customerId);
                        tabbedPanel.setSelectedTab(EDIT_TAB_INDEX);
                        currentlySelectedTab = EDIT_TAB_INDEX;
                        RequestCycle.get().setResponsePage( CustomerActionsPage.this.getPage() );
                    }

                    @Override
                    protected void invokeCustomerShow(Long customerId) {
                        customerSelectedForEditModel.setObject(customerId);
                        tabbedPanel.setSelectedTab(VIEW_TAB_INDEX);
                        currentlySelectedTab = EDIT_TAB_INDEX;
                        RequestCycle.get().setResponsePage( CustomerActionsPage.this.getPage() );
                    }
                };
            }
        }));
        titleLabelsByTabIndex.add(new FIDLabelModel("title.customer_list_customer").getObject());
        if (SHOW_CUSTOMERLIST_PAGE.equals(initialTabSelection))
            preSelectedTab = VIEW_ALL_TAB_INDEX;

        tabs.add(new PanelCachingTab(new AbstractTab(new FIDLabelModel("nav.view_all_archived")) {
            public Panel getPanel(String panelId) {
                return new CustomerArchivedListPanel(panelId, webSessionMapModel, getLoaderFactory()){

                    //TODO copied from struts version but all customers here should have EntityState archived.

                    @Override
                    protected void invokeCustomerEdit(Long customerId) {
                        customerSelectedForEditModel.setObject(customerId);
                        tabbedPanel.setSelectedTab(EDIT_TAB_INDEX);
                        currentlySelectedTab = EDIT_TAB_INDEX;
                        RequestCycle.get().setResponsePage( CustomerActionsPage.this.getPage() );
                    }

                    @Override
                    protected void invokeCustomerShow(Long customerId) {
                        customerSelectedForEditModel.setObject(customerId);
                        tabbedPanel.setSelectedTab(VIEW_TAB_INDEX);
                        currentlySelectedTab = EDIT_TAB_INDEX;
                        RequestCycle.get().setResponsePage( CustomerActionsPage.this.getPage() );
                    }
                };
            }
        }));
        titleLabelsByTabIndex.add(new FIDLabelModel("title.customer_list_archived_customer").getObject());

        tabs.add(new AbstractTab(new FIDLabelModel("nav.view")) {
            @Override
            public Panel getPanel(String panelId)
            {
                return new CustomerShowPanel(panelId, customerSelectedForEditModel, securityFilterModel) {
                    @Override
                    protected void listDivisionsAction() {
                        tabbedPanel.setSelectedTab(DIVISION_TAB_INDEX);
                        currentlySelectedTab = DIVISION_TAB_INDEX;
                        RequestCycle.get().setResponsePage( CustomerActionsPage.this.getPage() );
                    }

                    @Override
                    protected void mergeInvokedAction() {
                        showMergeTab.setValue(true);
                        tabbedPanel.setSelectedTab(MERGE_TAB_INDEX);
                        currentlySelectedTab = MERGE_TAB_INDEX;
                        RequestCycle.get().setResponsePage( CustomerActionsPage.this.getPage() );
                    }

                    @Override
                    protected void postArchiveAction() {
                        customerSelectedForEditModel.setObject(null);
                        tabbedPanel.setSelectedTab(VIEW_ALL_TAB_INDEX);
                        currentlySelectedTab = VIEW_ALL_TAB_INDEX;
                        RequestCycle.get().setResponsePage( CustomerActionsPage.this.getPage() );
                    }
                };
            }
            @Override
            public boolean isVisible() {
                return customerSelectedForEditModel.getObject() != null;
            }
        });
        titleLabelsByTabIndex.add(new FIDLabelModel("title.customer_show_customer").getObject());
        if (SHOW_CUSTOMERVIEW_PAGE.equals(initialTabSelection)) {
            preSelectedTab = VIEW_TAB_INDEX;
            customerSelectedForEditModel.setObject(initialCustomerId);
        }

        tabs.add(new AbstractTab(new FIDLabelModel("nav.edit")) {
            @Override
            public Panel getPanel(String panelId)
            {
                return new CustomerEditPanel(panelId, customerSelectedForEditModel,
                        sessionUserModel, webSessionMapModel, getLoaderFactory()) {
                    @Override
                    protected void postSaveAction(Long customerId, AjaxRequestTarget target) {
                        customerSelectedForEditModel.setObject(customerId);
                        tabbedPanel.setSelectedTab(VIEW_TAB_INDEX);
                        currentlySelectedTab = VIEW_TAB_INDEX;
                        //target.add(tabbedPanel);
                        RequestCycle.get().setResponsePage( CustomerActionsPage.this.getPage() );
                    }
                };
            }
            @Override
            public boolean isVisible() {
                return customerSelectedForEditModel.getObject() != null && !showMergeTab.booleanValue();
            }
        });
        titleLabelsByTabIndex.add("");

        tabs.add(new AbstractTab(new FIDLabelModel("nav.edit")) {
            @Override
            public Panel getPanel(String panelId)
            {
                return new CustomerMergePanel(panelId, customerSelectedForEditModel, sessionUserModel, getLoaderFactory()) {

                };
            }
            @Override
            public boolean isVisible() {
                return customerSelectedForEditModel.getObject() != null && showMergeTab.booleanValue();
            }
        });
        titleLabelsByTabIndex.add("");

        tabs.add(new AbstractTab(new FIDLabelModel("nav.divisions")) {
            @Override
            public Panel getPanel(String panelId)
            {
                return new CustomerDivisionsPanel(panelId, customerSelectedForEditModel, getLoaderFactory());
            }
            @Override
            public boolean isVisible() {
                return customerSelectedForEditModel.getObject() != null;
            }
        });
        titleLabelsByTabIndex.add(new FIDLabelModel("title.customer_divisions").getObject());

        tabs.add(new AbstractTab(new FIDLabelModel("nav.users")) {
            @Override
            public Panel getPanel(String panelId)
            {
                return new CustomerUsersPanel(panelId) ;
            }
            @Override
            public boolean isVisible() {
                return customerSelectedForEditModel.getObject() != null;
            }
        });
        titleLabelsByTabIndex.add(new FIDLabelModel("title.customer_users").getObject());

        tabs.add(new AbstractTab(new FIDLabelModel("nav.add")) {
            @Override
            public Panel getPanel(String panelId)
            {
                return new CustomerEditPanel(panelId, customerSelectedForEditModel,
                        sessionUserModel, webSessionMapModel, getLoaderFactory()) {
                    @Override
                    protected void postSaveAction(Long customerId, AjaxRequestTarget target) {
                        customerSelectedForEditModel.setObject(customerId);
                        tabbedPanel.setSelectedTab(VIEW_TAB_INDEX);
                        currentlySelectedTab = VIEW_TAB_INDEX;
                        //target.add(tabbedPanel);
                        RequestCycle.get().setResponsePage( CustomerActionsPage.this.getPage() );
                    }
                };
            }
        });
        titleLabelsByTabIndex.add("");

        tabs.add(new PanelCachingTab(new AbstractTab(new FIDLabelModel("nav.import_export")) {
            @Override
            public Panel getPanel(String panelId)
            {
                return new CustomerImportPanel(panelId, currentUserModel, sessionUserModel,
                        securityFilterModel, webSessionMapModel);
            }
        }));
        titleLabelsByTabIndex.add(new FIDLabelModel("title.customer_import_export").getObject());

        if (SHOW_IMPORTEXPORT_PAGE.equals(initialTabSelection))
            preSelectedTab = IMPORT_EXPORT_TAB_INDEX;

        if (preSelectedTab != NO_TAB_SELECTION) {
            tabbedPanel.setSelectedTab(preSelectedTab);
        }

    }
    /* Create models for use by component panels as callbacks to get values obtainable only from this page.
     * This avoids attempting to add these objects to the serialized state of the panels */
    private void createModels() {

        currentTitleModel = new IModel<String>() {
            /* This model represents the title that appears on the page above the tabs and describes the currently
               displayed tab panel. Not all panels have this title */
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
        webSessionMapModel = new IModel<WebSessionMap>() {
            public WebSessionMap getObject() {
                return new WebSessionMap(getServletRequest().getSession(false));
            }
            public void setObject(final WebSessionMap object) { }
            public void detach() {}
        };
        customerSelectedForEditModel = Model.of((Long) null);
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

