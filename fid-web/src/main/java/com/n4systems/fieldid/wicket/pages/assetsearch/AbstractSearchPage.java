package com.n4systems.fieldid.wicket.pages.assetsearch;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.components.table.JumpableNavigationBar;
import com.n4systems.fieldid.wicket.components.table.SimpleDataTable;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.components.AbstractCriteriaPanel;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
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
    public static final String SCROLL_JS = "var currentScrollY = typeof(window.pageYOffset)=='number' ? window.pageYOffset : document.documentElement.scrollTop; var currentPaginationBarY = findPos($('#%s'))[1]; if (currentPaginationBarY < currentScrollY) { window.scroll(0, currentPaginationBarY)}";

    protected SavedItem<T> savedItem;
    protected T searchCriteria;
    protected Component searchMenu;
    protected boolean showLeftPanel;

    @SpringBean
    private PersistenceService persistenceService;

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
        Component resultsPanel = createResultsPanel(RESULTS_PANEL_ID, criteriaModel, isShowBlankSlate());
        add(resultsPanel);

        final Component searchConfigPanel = createCriteriaPanel(FieldIDFrontEndPage.LEFT_PANEL_ID, criteriaModel);

        setLeftPanelContent(searchConfigPanel);
        if (resultsPanel instanceof SRSResultsPanel) {
            setBottomPanelContent(createNavigationPanel(FieldIDFrontEndPage.BOTTOM_PANEL_ID, (SRSResultsPanel)resultsPanel));
        }
        setSubMenuContent(searchMenu = createSubMenu(FieldIDFrontEndPage.SUB_MENU_ID, criteriaModel));
    }

    private Component createNavigationPanel(String id, final SRSResultsPanel resultsPanel) {
        return new JumpableNavigationBar(id, resultsPanel.getDataTable()) {
                @Override protected void onPageChanged(AjaxRequestTarget target) {
                    target.appendJavaScript(String.format(SCROLL_JS,getMarkupId()));
                    AbstractSearchPage.this.onPageChanged(target, resultsPanel.getDataTable());
                }
            };
    }

    protected void onPageChanged(AjaxRequestTarget target, SimpleDataTable dataTable) {
        ServletWebRequest request = (ServletWebRequest) getRequest();
        new LegacyReportCriteriaStorage().storePageNumber(request.getContainerRequest().getSession(), dataTable.getTable().getCurrentPage());
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
            searchCriteria = createCriteria();
        } else {
            saveLastSearch(searchCriteria);
            persistenceService.clearSession();
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
        response.renderCSSReference("style/legacy/pageStyles/wide.css");
        response.renderCSSReference("style/legacy/newCss/component/forms.css");
        response.renderCSSReference("style/legacy/newCss/component/buttons.css");
        response.renderCSSReference("style/legacy/newCss/assetsearch/search-filter.css");

        response.renderOnDomReadyJavaScript("fieldIdWidePage.init(" + showLeftPanel + ")");
    }

    protected Link createSaveLink(String linkId, final boolean overwrite) {
        Link link = new Link(linkId) {
            @Override
            public void onClick() {
                setResponsePage(createSaveResponsePage(overwrite));
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

    protected abstract AbstractCriteriaPanel<T> createCriteriaPanel(String id, Model<T> criteriaModel);

    protected boolean isEmptyResults() {
        return false;
    }

    protected abstract Component createResultsPanel(String id, Model<T> criteriaModel);

    protected abstract Component createBlankSlate(String id);

    protected abstract void saveLastSearch(T searchCriteria);

    protected abstract Page createSaveResponsePage(boolean overwrite);

    protected abstract T createCriteria();
}

