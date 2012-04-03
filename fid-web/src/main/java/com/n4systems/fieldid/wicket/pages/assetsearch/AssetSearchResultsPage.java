package com.n4systems.fieldid.wicket.pages.assetsearch;

import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchCriteriaPanel;
import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchMassActionPanel;
import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchResultsPanel;
import com.n4systems.fieldid.wicket.components.reporting.SlidingCollapsibleContainer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.AbstractSearchPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchPage;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.services.reporting.DashboardReportingService;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**related to WEB-2629.   when switched over to the new page, this page should be removed. 
 in the interim, any applicable bug fixes or changes should be ported to new asset search page.
 @see SearchResultsPage
*/    
@Deprecated 
public class AssetSearchResultsPage extends FieldIDFrontEndPage {

    private AssetSearchResultsPanel reportResultsPanel;

    @SpringBean
    private DashboardReportingService dashboardReportingService;

    @SpringBean
    private SavedAssetSearchService savedAssetSearchService;

    private SavedSearchItem savedSearchItem;
    private AssetSearchCriteria searchCriteriaModel;

    public AssetSearchResultsPage(PageParameters params) { 
    	super(params);
		AssetSearchCriteria model = createSearchCriteriaModelFromDashboardParameters(params);
		init(model, new SavedSearchItem(model));
    }

	public AssetSearchResultsPage(SavedSearchItem savedSearchItem) {
        super(new PageParameters());
        init(savedSearchItem.getSearchCriteria(), savedSearchItem);
	}
    
    public AssetSearchResultsPage(AssetSearchCriteria searchCriteriaModel, SavedSearchItem savedSearchItem) {
    	super(new PageParameters());
    	SavedSearchItem searchItem = null;
    	if (savedSearchItem==null) { 
    		searchItem = new SavedSearchItem(searchCriteriaModel);    		
    	} else {
    		searchItem = savedSearchItem;
    		savedSearchItem.setSearchCriteria(searchCriteriaModel);
    	}
    	init(searchCriteriaModel, searchItem);
    }

	public AssetSearchResultsPage(AssetSearchCriteria searchCriteriaModel) {
		this(searchCriteriaModel,null);
	}

	private AssetSearchCriteria createSearchCriteriaModelFromDashboardParameters(PageParameters params) {
    	if(params!=null) {
    		// load config and set values...
    		Long widgetDefinitionId = params.get(AbstractSearchPage.WIDGET_DEFINITION_PARAMETER).toLong();
    		Long x = params.get(AbstractSearchPage.X_PARAMETER).toLong();
    		String series = params.get(AbstractSearchPage.SERIES_PARAMETER).toString();
    		String y = params.get(AbstractSearchPage.Y_PARAMETER).toString();
    		AssetSearchCriteria model = dashboardReportingService.convertWidgetDefinitionToAssetCriteria(widgetDefinitionId,x,y,series);
    		return model;
    	}
    	throw new IllegalStateException("you must specify expected dashboard parameters in order to create report criteria model.");
	}

    private void init(AssetSearchCriteria searchCriteriaModel, SavedSearchItem savedSearchItem) {
    	this.savedSearchItem = savedSearchItem;
        this.searchCriteriaModel = searchCriteriaModel;
        savedAssetSearchService.saveLastSearch(searchCriteriaModel);
        Model<AssetSearchCriteria> criteriaModel = new Model<AssetSearchCriteria>(searchCriteriaModel);
        add(reportResultsPanel = new AssetSearchResultsPanel("resultsPanel", criteriaModel));

        SlidingCollapsibleContainer criteriaExpandContainer = new SlidingCollapsibleContainer("criteriaExpandContainer", new FIDLabelModel("label.search_settings"));
        criteriaExpandContainer.addContainedPanel(new AssetSearchCriteriaPanel("criteriaPanel", criteriaModel, savedSearchItem) {
        	@Override
        	protected void onNoDisplayColumnsSelected() {
        		reportResultsPanel.setVisible(false);
        	}
        });

        add(new BookmarkablePageLink<Void>("startNewReportLink", AssetSearchPage.class));
        add(createSaveSearchLink("saveReportLink", true));
        add(createSaveSearchLink("saveReportLinkAs", false));

        add(new BookmarkablePageLink<Void>("startNewReportLink2", AssetSearchPage.class));
        add(createSaveSearchLink("saveReportLink2", true));
        add(createSaveSearchLink("saveReportLinkAs2", false));

        add(criteriaExpandContainer);
        add(new AssetSearchMassActionPanel("massActionPanel", criteriaModel));    	
	}

    private Link createSaveSearchLink(String linkId, final boolean overwrite) {
        Link link = new Link(linkId) {
            @Override
            public void onClick() {
                setResponsePage(new SaveAssetSearchPage(savedSearchItem, AssetSearchResultsPage.this, overwrite));
            }
        };
        if (!overwrite) {
            // If this is not overwrite (ie the Save As link), it should be invisible if this isn't an existing saved report
            link.setVisible(savedSearchItem.getId() != null);
        }
        return link;
    }

	@Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new PropertyModel<String>(this, "pageLabel"));
    }

    public String getPageLabel() {
        IModel<String> pageLabelModel = new FIDLabelModel("title.asset_search_results");
        if (searchCriteriaModel.getSavedReportName() != null) {
            pageLabelModel = new Model<String>(pageLabelModel.getObject() + " for - " + searchCriteriaModel.getSavedReportName());
        }
        return pageLabelModel.getObject();
    }
    
    @Override
    protected Component createHeaderLink(String id, String label) {
    	BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(id, SearchPage.class);
    	pageLink.add(new FlatLabel(label, new FIDLabelModel("label.search2")));
    	return pageLink;
    }
    

}
