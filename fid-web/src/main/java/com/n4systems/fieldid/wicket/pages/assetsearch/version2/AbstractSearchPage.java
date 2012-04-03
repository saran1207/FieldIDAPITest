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

    private @SpringBean DashboardReportingService dashboardReportingService;
    private @SpringBean SavedAssetSearchService savedAssetSearchService;

    protected SavedItem<T> savedItem;
    protected T searchCriteria;
    protected Component searchMenu;
    private boolean showLeftPanel;

    public AbstractSearchPage(PageParameters params) {
        this(params, null, null);  // will create default criteria & savedItems.
    }

    public AbstractSearchPage(PageParameters params, T searchCriteria, SavedItem<T> savedItem) {
        super(params);
    	this.savedItem = savedItem;
        this.searchCriteria = searchCriteria;
        init();
    }

    protected void init() {
        showLeftPanel = calculateInitialViewState();
        saveLastSearch(getSearchCriteria());

        Model<T> criteriaModel = new Model<T>(getSearchCriteria());
        add(createResultsPanel("resultsPanel", criteriaModel, isShowBlankSlate()));

        SlidingCollapsibleContainer criteriaExpandContainer = new SlidingCollapsibleContainer("criteriaExpandContainer", new FIDLabelModel("label.search_settings"));
        criteriaExpandContainer.addContainedPanel(createCriteriaPanel("criteriaPanel", criteriaModel, getSavedItem()));

        add(criteriaExpandContainer);

        final Component searchConfigPanel = createCriteriaPanel(FieldIDFrontEndPage.LEFT_PANEL_ID, criteriaModel);

		setLeftPanelContent(searchConfigPanel);
        setSubMenuContent(searchMenu = createSubMenu(FieldIDFrontEndPage.SUB_MENU_ID, criteriaModel));
    }

    private boolean calculateInitialViewState() {
        return searchCriteria == null;
    }

    private boolean isShowBlankSlate() {
        return showLeftPanel;
    }

    protected boolean isShowLeftPanel() {
        return showLeftPanel;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
    	super.renderHead(response);
    	response.renderJavaScriptReference("javascript/fieldIdWide.js");
        response.renderCSSReference("style/pageStyles/wide.css");
        response.renderOnDomReadyJavaScript("fieldIdWidePage.init("+ showLeftPanel +");");
    }

    protected Link createSaveLink(String linkId, final boolean overwrite) {
        Link link = new Link(linkId) {
            @Override public void onClick() {
                setResponsePage(createSaveReponsePage(overwrite));
            }
        };
        if (!overwrite) {
            // If this is not overwrite (ie the Save As link), it should be invisible if this isn't an existing saved report
            link.setVisible(savedItem.getId() != null);
        }
        return link;
    }

    public SavedItem<T> getSavedItem() {
        if (savedItem==null) {
            savedItem = createSavedItemFromCriteria(getSearchCriteria());
        }
        return savedItem;
    }

    public T getSearchCriteria() {
        if (searchCriteria==null) {
            searchCriteria = createSearchCriteria();
        }
        return searchCriteria;
    }

    private final Component createResultsPanel(String id, Model<T> criteriaModel, boolean showBlankSlate) {
        return (showBlankSlate || isEmptyResults()) ? createBlankSlate(id) : createResultsPanel(id, criteriaModel);
    }

    protected abstract SavedItem<T> createSavedItemFromCriteria(T searchCriteriaModel);

    protected abstract Component createSubMenu(String contentId, Model<T> criteriaModel);

    protected abstract Component createCriteriaPanel(String id, Model<T> criteriaModel);

    protected boolean isEmptyResults() { return false; }

    protected abstract Component createResultsPanel(String id, Model<T> criteriaModel);

    protected abstract Component createBlankSlate(String id);

    protected abstract void saveLastSearch(T searchCriteria);

    protected abstract Page createSaveReponsePage(boolean overwrite);

    protected abstract Component createCriteriaPanel(String id, Model<T> criteriaModel, SavedItem<T> savedItem);

    protected abstract T createSearchCriteria();
}

