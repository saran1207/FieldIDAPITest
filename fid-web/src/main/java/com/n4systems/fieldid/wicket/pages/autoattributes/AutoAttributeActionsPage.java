package com.n4systems.fieldid.wicket.pages.autoattributes;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndWithFeedbackPage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.AssetType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.security.Permissions;
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


@UserPermissionFilter(userRequiresOneOf={Permissions.MANAGE_SYSTEM_CONFIG})
public class AutoAttributeActionsPage extends FieldIDFrontEndWithFeedbackPage {

    public static final String INITIAL_TAB_SELECTION_KEY = "InitialTabSelection";
    public static final String SHOW_AUTO_ATTRIBUTES_VIEW_ALL_PAGE = "ShowViewAllPage";
    public static final String SHOW_AUTO_ATTRIBUTES_EDIT_PAGE = "ShowEditPage";
    public static final String SHOW_AUTO_ATTRIBUTES_DEFINITIONS_PAGE = "ShowDefinitionsPage";
    public static final String SHOW_IMPORTEXPORT_PAGE = "ShowImportExportPage";

    private static final Logger logger = Logger.getLogger(AutoAttributeActionsPage.class);

    private List<String> titleLabelsByTabIndex;
    private int currentlySelectedTab;
    private IModel<String> currentTitleModel;
    private IModel<Long> currentAutoAttributeCriteriaIdEditModel;
    private IModel<Long> assetTypeSelectedForEditIdModel;
    private IModel<User> currentUserModel;
    private IModel<SessionUser> sessionUserModel;
    private IModel<SecurityFilter> securityFilterModel;
    private IModel<WebSessionMap> webSessionMapModel;

    private AutoAttributeDefinitionsPanel definitionsPanel;
    private AutoAttributeEditPanel editPanel;

    /**
     * The currently selected AssetType. This is set in one of the following three cases since they change the focus
     * to an individual customer:
     * 1) In the 'View All' tab the user clicks the listed asset type
     * The selected AssetTYpe is reset to null if the user clicks on the 'View All', or 'Import/Export
     * tabs since the focus is no longer on an individual customer.
     */

    private String initialTabSelection;
    private Long initialCustomerId;
    private FeedbackPanel feedbackPanel;

    private LoaderFactory loaderFactory;

    /**
     * Tabbed page with the various actions available for customers.
     * @param params - optional and may be used to specify the initial tab selection and/or customer selection.
     */
    public AutoAttributeActionsPage(PageParameters params) {
        super(params);
        currentlySelectedTab = 0;
        titleLabelsByTabIndex = new ArrayList<String>();
        StringValue tabSelection = params.get(INITIAL_TAB_SELECTION_KEY);
        if (tabSelection != null && !tabSelection.isEmpty())
            initialTabSelection = tabSelection.toString();
    }

    @Override
    protected void onInitialize() {
        createModels();
        super.onInitialize();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/component/wicketTabbedPanel.css");

        response.renderCSS("div#pageContent {display: none}", null);
        response.renderCSS(".wicket-tabbed-panel-content {margin-top: 14px;}", null);

         /* We want the first three tabs to appear on the left (though all might not be visible) and the last one on the right */
        response.renderCSS(".tab-row {width: 100%; padding-top:10px;}", null);
        response.renderCSS("div.wicket-tabpanel div.tab-row li.tab3 {float: right}", null);
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
         * This page has 4 tabs - 'View All', 'Edit', 'Definitions', 'Import/Export'
         *
         * The 'Edit', 'Definitions' and Import/Export' tabs become visible under these situations:
         *
         */

        final int NO_TAB_SELECTION = -1;
        final int VIEW_ALL_TAB_INDEX = 0;
        final int EDIT_TAB_INDEX= 1;
        final int DEFINITIONS_TAB_INDEX = 2;
        final int IMPORT_EXPORT_TAB_INDEX = 3;

        int preSelectedTab = NO_TAB_SELECTION;


        final List<ITab> tabs = new ArrayList<ITab>();

        final TabbedPanel tabbedPanel = new TabbedPanel("navBar", tabs) {

            @Override
            protected void onBeforeRender() {
                int newTab = getSelectedTab();
                if (newTab != currentlySelectedTab) {
                    /* Tab selection has changed, some pages might have to cleanup */
                    if (newTab == VIEW_ALL_TAB_INDEX || newTab == IMPORT_EXPORT_TAB_INDEX)
                        assetTypeSelectedForEditIdModel.setObject(null); // cleanup any old value
                    else
                    if (newTab == DEFINITIONS_TAB_INDEX)
                        definitionsPanel.handleSelectionChange();
                    else
                    if (newTab == EDIT_TAB_INDEX)
                        editPanel.handleSelectionChange();

                    Session.get().cleanupFeedbackMessages();
                    currentlySelectedTab = getSelectedTab();
                }
                super.onBeforeRender();
            }

            @Override
            public TabbedPanel setSelectedTab(int index) {
                return super.setSelectedTab(index);
            }
        };

        tabbedPanel.add(new AttributeAppender("class", new Model("wicket-tabpanel"), " "));
        if (preSelectedTab > -1) {
            tabbedPanel.setSelectedTab(preSelectedTab);
        }
        add(tabbedPanel);

        /* Create the tabs */

        /**
         * View All tab
         */
        tabs.add(new PanelCachingTab(new AbstractTab(new FIDLabelModel("nav.view_all")) {
            @Override
            public Panel getPanel(String panelId) {
                return new AutoAttributeViewAllPanel(panelId, webSessionMapModel, getLoaderFactory()) {
                    @Override
                    protected void assetTypeWithCriteriaChosen(Long assetTypeId, Long criteriaId) {
                        if (definitionsPanel != null)
                            definitionsPanel.handleSelectionChange();
                        changeSelectedPage(
                                tabbedPanel,
                                assetTypeId,
                                criteriaId,
                                DEFINITIONS_TAB_INDEX);
                    }

                    @Override
                    protected void assetTypeWithoutCriteriaChosen(Long assetTypeId) {
                        if (editPanel != null)
                            editPanel.handleSelectionChange();
                        changeSelectedPage(tabbedPanel, assetTypeId, null, EDIT_TAB_INDEX);
                    }
                };
            }
        }));
        titleLabelsByTabIndex.add(getString("title.auto_attribute_list"));
        if (SHOW_AUTO_ATTRIBUTES_VIEW_ALL_PAGE.equals(initialTabSelection)) {
            preSelectedTab = VIEW_ALL_TAB_INDEX;
        }

        /**
         * Edit tab
         */
        tabs.add(new PanelCachingTab(new AbstractTab(new FIDLabelModel("nav.edit")) {
            @Override
            public Panel getPanel(String panelId) {
                editPanel = new AutoAttributeEditPanel(
                        panelId,
                        currentUserModel,
                        assetTypeSelectedForEditIdModel,
                        currentAutoAttributeCriteriaIdEditModel) {

                    @Override
                    void saveActionCompleted(AjaxRequestTarget target) {
                        changeSelectedPage(
                                tabbedPanel,
                                DEFINITIONS_TAB_INDEX);
                    }
                    @Override
                    void cancelActionCompleted(AjaxRequestTarget target) {
                        changeSelectedPage(tabbedPanel, VIEW_ALL_TAB_INDEX);
                    }

                    @Override
                    void deleteActionCompleted(AjaxRequestTarget target) {
                        assetTypeSelectedForEditIdModel.setObject(null);
                        changeSelectedPage(
                                tabbedPanel,
                                VIEW_ALL_TAB_INDEX);
                    }

                    @Override
                    void switchToDefinitionList(AjaxRequestTarget target) {
                        changeSelectedPage(tabbedPanel, DEFINITIONS_TAB_INDEX);
                    }
                };
                return editPanel;
            }
            @Override
            public boolean isVisible() {
                return assetTypeSelectedForEditIdModel.getObject() != null;
            }
        }));
        titleLabelsByTabIndex.add(getString("title.auto_attribute_edit"));
        if (SHOW_AUTO_ATTRIBUTES_EDIT_PAGE.equals(initialTabSelection)) {
            preSelectedTab = EDIT_TAB_INDEX;
        }

        /**
         * Definitions tab
         */
        tabs.add(new PanelCachingTab(new AbstractTab(new FIDLabelModel("nav.definitions")) {
            @Override
            public Panel getPanel(String panelId) {
                definitionsPanel = new AutoAttributeDefinitionsPanel(panelId, currentAutoAttributeCriteriaIdEditModel, sessionUserModel);
                return definitionsPanel;
            }
             @Override
            public boolean isVisible() {
                return assetTypeSelectedForEditIdModel.getObject() != null && currentAutoAttributeCriteriaIdEditModel.getObject() != null;
            }
        }));
        titleLabelsByTabIndex.add(getString("title.auto_attribute_definitions"));
        if (SHOW_AUTO_ATTRIBUTES_DEFINITIONS_PAGE.equals(initialTabSelection)) {
            preSelectedTab = DEFINITIONS_TAB_INDEX;
        }

        /**
         * Import/Export tab - import/export auto attributes for an AssetType
         */
        tabs.add(new PanelCachingTab(new AbstractTab(new FIDLabelModel("nav.import_export")) {
            @Override
            public Panel getPanel(String panelId)
            {
                return new AutoAttributeImportPanel(panelId, currentUserModel, sessionUserModel,
                        securityFilterModel, webSessionMapModel);
            }
        }));
        titleLabelsByTabIndex.add(getString("title.auto_attribute_import_export"));
        if (SHOW_IMPORTEXPORT_PAGE.equals(initialTabSelection)) {
            preSelectedTab = IMPORT_EXPORT_TAB_INDEX;
        }


        /* Set initial tab selection */
        if (preSelectedTab != NO_TAB_SELECTION) {
            tabbedPanel.setSelectedTab(preSelectedTab);
        }

    }

    private void changeSelectedPage(TabbedPanel tabbedPanel, Long assetTypeId, Long criteriaId, int newIndex) {
        assetTypeSelectedForEditIdModel.setObject(assetTypeId);
        currentAutoAttributeCriteriaIdEditModel.setObject(criteriaId);
        tabbedPanel.setSelectedTab(newIndex);
        currentlySelectedTab = newIndex;
        RequestCycle.get().setResponsePage( AutoAttributeActionsPage.this.getPage() );
    }

    private void changeSelectedPage(TabbedPanel tabbedPanel, int newIndex) {
        tabbedPanel.setSelectedTab(newIndex);
        currentlySelectedTab = newIndex;
        RequestCycle.get().setResponsePage( AutoAttributeActionsPage.this.getPage() );
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
        assetTypeSelectedForEditIdModel = Model.of((Long) null);
        currentAutoAttributeCriteriaIdEditModel = Model.of((Long) null);
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

