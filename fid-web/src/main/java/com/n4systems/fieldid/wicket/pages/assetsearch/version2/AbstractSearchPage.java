package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.wicket.components.reporting.SlidingCollapsibleContainer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.services.reporting.DashboardReportingService;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class AbstractSearchPage<T extends SearchCriteria> extends FieldIDFrontEndPage {

    //
    public static final String SOURCE_PARAMETER = "source";
    public static final String WIDGET_DEFINITION_PARAMETER = "wdf";
    // different sources (i.e. where the redirection is coming from...who is requesting the search page).
    public static final String SAVED_ITEM_SOURCE = "savedItem";
    public static final String WIDGET_SOURCE = "widget";
    // parameters required by the different sources.
    public static final String X_PARAMETER = "longX";
    public static final String SERIES_PARAMETER = "series";
    public static final String Y_PARAMETER = "y";
    public static final String RESULTS_PANEL_ID = "resultsPanel";

    private @SpringBean DashboardReportingService dashboardReportingService;
    private @SpringBean SavedAssetSearchService savedAssetSearchService;

    protected SavedItem<T> savedItem;
    protected T searchCriteria;
    protected Component searchMenu;
    protected boolean showLeftPanel;

    public AbstractSearchPage(PageParameters params) {
        this(params, null, null);  // will create default criteria & savedItems.
    }

    public AbstractSearchPage(PageParameters params, T searchCriteria, SavedItem<T> savedItem) {
        super(params);
        this.savedItem = savedItem;
        this.searchCriteria = searchCriteria;
        initialize();
    }

    private void initialize() {
        showLeftPanel = calculateInitialViewState();
        initializeSearchCriteria();
        initializeSavedItem();
        addComponents();
    }

    private void addComponents() {
        Model<T> criteriaModel = new Model<T>(searchCriteria);
        add(createResultsPanel(RESULTS_PANEL_ID, criteriaModel, isShowBlankSlate()));

        SlidingCollapsibleContainer criteriaExpandContainer = new SlidingCollapsibleContainer("criteriaExpandContainer", new FIDLabelModel("label.search_settings"));
        criteriaExpandContainer.addContainedPanel(createCriteriaPanel("criteriaPanel", criteriaModel, savedItem));

        add(criteriaExpandContainer);

        final Component searchConfigPanel = createCriteriaPanel(FieldIDFrontEndPage.LEFT_PANEL_ID, criteriaModel);

        setLeftPanelContent(searchConfigPanel);
        setSubMenuContent(searchMenu = createSubMenu(FieldIDFrontEndPage.SUB_MENU_ID, criteriaModel));
    }

    public void initializeSavedItem() {
        if (savedItem == null) {
            savedItem = createSavedItemFromCriteria(searchCriteria);
        } else {
            savedItem.setSearchCriteria(searchCriteria);
        }
    }

    public void initializeSearchCriteria() {
        if (searchCriteria == null) {
            searchCriteria = createSearchCriteria();
        } else {
            saveLastSearch(searchCriteria);
        }
    }

    private boolean calculateInitialViewState() {
        return searchCriteria == null;
    }

    private boolean isShowBlankSlate() {
        return showLeftPanel;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/fieldIdWide.js");
        response.renderCSSReference("style/pageStyles/wide.css");
        response.renderOnDomReadyJavaScript("fieldIdWidePage.init('" + showLeftPanel + "')");
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
    }

    protected Link createSaveLink(String linkId, final boolean overwrite) {
        Link link = new Link(linkId) {
            @Override
            public void onClick() {
                setResponsePage(createSaveReponsePage(overwrite));
            }
        };
        if (!overwrite) {
            // If this is not overwrite (ie the Save As link), it should be invisible if this isn't an existing saved report
            link.setVisible(savedItem.getId() != null);
        }
        return link;
    }

    private final Component createResultsPanel(String id, Model<T> criteriaModel, boolean showBlankSlate) {
        return (showBlankSlate || isEmptyResults()) ? createBlankSlate(id) : createResultsPanel(id, criteriaModel);
    }

    public Page withSavedItemNamed(String name) {
        savedItem.setName(name);
        return this;
    }

    protected void resetPageOnError() {
        // clear results panel. show left panel. go back to initial state.
        showLeftPanel = true;
        replace(createBlankSlate(RESULTS_PANEL_ID));
    }

    protected abstract SavedItem<T> createSavedItemFromCriteria(T searchCriteriaModel);

    protected abstract Component createSubMenu(String contentId, Model<T> criteriaModel);

    protected abstract Component createCriteriaPanel(String id, Model<T> criteriaModel);

    protected boolean isEmptyResults() {
        return false;
    }

    protected abstract Component createResultsPanel(String id, Model<T> criteriaModel);

    protected abstract Component createBlankSlate(String id);

    protected abstract void saveLastSearch(T searchCriteria);

    protected abstract Page createSaveReponsePage(boolean overwrite);

    protected abstract Component createCriteriaPanel(String id, Model<T> criteriaModel, SavedItem<T> savedItem);

    protected abstract T createSearchCriteria();
}

