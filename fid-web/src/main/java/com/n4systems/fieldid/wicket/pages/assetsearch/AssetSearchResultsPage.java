package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchCriteriaPanel;
import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchMassActionPanel;
import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchResultsPanel;
import com.n4systems.fieldid.wicket.components.reporting.SlidingCollapsibleContainer;
import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchResultsPage;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.services.reporting.DashboardReportingService;

@SuppressWarnings("serial")
public class AssetSearchResultsPage extends FieldIDFrontEndPage {

    private AssetSearchResultsPanel reportResultsPanel;

    @SpringBean
    private DashboardReportingService dashboardReportingService;

    @SpringBean
    private SavedAssetSearchService savedAssetSearchService;

    private SavedSearchItem savedSearchItem;
    private AssetSearchCriteriaModel searchCriteriaModel;

    public AssetSearchResultsPage(PageParameters params) { 
    	super(params);
		init(createSearchCriteriaModel(params), new SavedSearchItem(searchCriteriaModel));
    }

	public AssetSearchResultsPage(SavedSearchItem savedSearchItem) {
        super(new PageParameters());
        init(savedSearchItem.getSearchCriteria(), savedSearchItem);
	}
    
    public AssetSearchResultsPage(AssetSearchCriteriaModel searchCriteriaModel, SavedSearchItem savedSearchItem) {
    	super(new PageParameters());
        SavedSearchItem newSavedSearchItem = new SavedSearchItem(searchCriteriaModel);
       	newSavedSearchItem.setId(savedSearchItem==null ? null : savedSearchItem.getId());
    	init(searchCriteriaModel, newSavedSearchItem);
    }

	public AssetSearchResultsPage(AssetSearchCriteriaModel searchCriteriaModel) {
		this(searchCriteriaModel,null);
	}

	private AssetSearchCriteriaModel createSearchCriteriaModel(PageParameters params) {
    	if(params!=null) {
    		// load config and set values...
    		Long widgetDefinitionId = params.get(SRSResultsPanel.WIDGET_DEFINITION_PARAMETER).toLong();
    		Long x = params.get(SRSResultsPanel.X_PARAMETER).toLong();
    		String series = params.get(SRSResultsPanel.SERIES_PARAMETER).toString();
    		String y = params.get(SRSResultsPanel.Y_PARAMETER).toString();
    		AssetSearchCriteriaModel model = dashboardReportingService.convertWidgetDefinitionToAssetCriteria(widgetDefinitionId,x,y,series); 
    		return model;
    	}
    	throw new IllegalStateException("must specify configId in parameters in order to create report criteria model.");
	}

    private void init(AssetSearchCriteriaModel searchCriteriaModel, SavedSearchItem savedSearchItem) {
    	this.savedSearchItem = savedSearchItem;
        this.searchCriteriaModel = searchCriteriaModel;
        savedAssetSearchService.saveLastSearch(searchCriteriaModel);
        Model<AssetSearchCriteriaModel> criteriaModel = new Model<AssetSearchCriteriaModel>(searchCriteriaModel);
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
    	BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(id, SearchResultsPage.class);
    	pageLink.add(new FlatLabel(label, new FIDLabelModel("label.search2")));
    	return pageLink;
    }
    

}
