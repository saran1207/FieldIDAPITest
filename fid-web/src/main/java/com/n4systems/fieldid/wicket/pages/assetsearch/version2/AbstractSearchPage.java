package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.wicket.components.DynamicPanel;
import com.n4systems.fieldid.wicket.components.reporting.SlidingCollapsibleContainer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.services.reporting.DashboardReportingService;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class AbstractSearchPage<T extends SearchCriteria> extends FieldIDFrontEndPage {

    @SpringBean
    private DashboardReportingService dashboardReportingService;

    @SpringBean
    private SavedAssetSearchService savedAssetSearchService;

    protected SavedItem<T> savedItem;
    protected T searchCriteria;
    protected Component searchMenu;
    private boolean showLeftMenu;

    public AbstractSearchPage(PageParameters params) {
        super(params);
    }

    public AbstractSearchPage(T searchCriteria, SavedSearchItem savedItem, boolean showLeftMenu) {
    	super(new PageParameters());
        SavedItem<T> newSavedSearchItem = createSavedItemFromCriteria(searchCriteria);
        newSavedSearchItem.setId(savedItem==null ? null : savedItem.getId());
    	init(searchCriteria, newSavedSearchItem, showLeftMenu);
    }

    protected void init(final T searchCriteria, final SavedItem<T> savedItem, boolean showLeftMenu) {
    	this.savedItem = savedItem;
    	this.showLeftMenu = showLeftMenu;
        this.searchCriteria = searchCriteria;
        saveLastSearch(searchCriteria);

        Model<T> criteriaModel = new Model<T>(searchCriteria);
        add(createResultsPanel("resultsPanel", criteriaModel, !showLeftMenu));

        SlidingCollapsibleContainer criteriaExpandContainer = new SlidingCollapsibleContainer("criteriaExpandContainer", new FIDLabelModel("label.search_settings"));
        criteriaExpandContainer.addContainedPanel(createCriteriaPanel("criteriaPanel", criteriaModel, savedItem));

        add(criteriaExpandContainer);

        final Panel searchConfigPanel = createConfigPanel(DynamicPanel.CONTENT_ID, criteriaModel);

		setLeftMenuContent(searchConfigPanel);
        setSubMenuContent(searchMenu = createSubMenu(DynamicPanel.CONTENT_ID, criteriaModel));
    }

    protected boolean isShowLeftMenu() {
        return showLeftMenu;
    }

    protected abstract SavedItem<T> createSavedItemFromCriteria(T searchCriteriaModel);

    protected abstract Component createSubMenu(String contentId, Model<T> criteriaModel);

    protected abstract Panel createConfigPanel(String id, Model<T> criteriaModel);

    private final Component createResultsPanel(String id, Model<T> criteriaModel, boolean showBlankSlate) {
        return (showBlankSlate) ? createBlankSlate(id) : createResultsPanel(id, criteriaModel);
    }

    protected abstract Component createResultsPanel(String id, Model<T> criteriaModel);

    protected abstract Component createBlankSlate(String id);

    protected abstract void saveLastSearch(T searchCriteria);

    protected abstract Page createSaveReponsePage(boolean overwrite);

    protected abstract Component createCriteriaPanel(String id, Model<T> criteriaModel, SavedItem<T> savedItem);

    @Override
    public void renderHead(IHeaderResponse response) {
    	super.renderHead(response);
    	response.renderJavaScriptReference("javascript/fieldIdWide.js");
        response.renderCSSReference("style/pageStyles/wide.css");
        response.renderOnDomReadyJavaScript("fieldIdWidePage.init("+showLeftMenu+");");
    }

    protected Link createSaveSearchLink(String linkId, final boolean overwrite) {
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
}

