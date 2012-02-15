package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.wicket.components.DynamicPanel;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchCriteriaPanel;
import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchMassActionPanel;
import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchResultsPanel;
import com.n4systems.fieldid.wicket.components.reporting.SlidingCollapsibleContainer;
import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.components.search.results.SearchConfigPanel;
import com.n4systems.fieldid.wicket.components.search.results.SearchSubMenu;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.services.reporting.DashboardReportingService;

@SuppressWarnings("serial")
public class AssetSearchResultsPage extends FieldIDFrontEndPage {

    private AssetSearchResultsPanel resultsPanel;

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
		AssetSearchCriteriaModel model = null;
		try { 
			if(params!=null) {
				// 	load config and set values...
				Long widgetDefinitionId = params.get(SRSResultsPanel.WIDGET_DEFINITION_PARAMETER).toLong();
				Long x = params.get(SRSResultsPanel.X_PARAMETER).toLong();
				String series = params.get(SRSResultsPanel.SERIES_PARAMETER).toString();
				String y = params.get(SRSResultsPanel.Y_PARAMETER).toString();				 
				return dashboardReportingService.convertWidgetDefinitionToAssetCriteria(widgetDefinitionId,x,y,series);
			}
		} catch (Exception e) {						
		}
		return new AssetSearchCriteriaModel();
	}

    private void init(final AssetSearchCriteriaModel searchCriteriaModel, SavedSearchItem savedSearchItem) {
    	this.savedSearchItem = savedSearchItem;
        this.searchCriteriaModel = searchCriteriaModel;
        savedAssetSearchService.saveLastSearch(searchCriteriaModel);
        Model<AssetSearchCriteriaModel> criteriaModel = new Model<AssetSearchCriteriaModel>(searchCriteriaModel);
        add(resultsPanel = new AssetSearchResultsPanel("resultsPanel", criteriaModel));
        

        SlidingCollapsibleContainer criteriaExpandContainer = new SlidingCollapsibleContainer("criteriaExpandContainer", new FIDLabelModel("label.search_settings"));
        criteriaExpandContainer.addContainedPanel(new AssetSearchCriteriaPanel("criteriaPanel", criteriaModel, savedSearchItem) {
        	@Override
        	protected void onNoDisplayColumnsSelected() {
        		resultsPanel.setVisible(false);
        	}
        });
        
//        add(new BookmarkablePageLink<Void>("startNewReportLink", AssetSearchPage.class));
//        add(createSaveSearchLink("saveReportLink", true));
//        add(createSaveSearchLink("saveReportLinkAs", false));

        add(new BookmarkablePageLink<Void>("startNewReportLink2", AssetSearchPage.class));
        add(createSaveSearchLink("saveReportLink2", true));
        add(createSaveSearchLink("saveReportLinkAs2", false));

        add(criteriaExpandContainer);
        add(new AssetSearchMassActionPanel("massActionPanel", criteriaModel));    
        
        final SearchConfigPanel searchConfigPanel = new SearchConfigPanel(DynamicPanel.CONTENT_ID, new Model(searchCriteriaModel)) {
        	@Override protected void onSearchSubmit() {
        		setResponsePage(new AssetSearchResultsPage(searchCriteriaModel));
        	}
        	@Override protected void onNoDisplayColumnsSelected() {
        		
        	}
        };
		setLeftMenuContent(searchConfigPanel);
        setSubMenuContent(new SearchSubMenu(DynamicPanel.CONTENT_ID) {
        	@Override protected void onClick(AjaxRequestTarget target, String id) {
        		if ("filters".equals(id)) { 
        			searchConfigPanel.showFilters();
        		} else if ("columns".equals(id)) { 
        			searchConfigPanel.showColumns();
        		}
        		target.add(searchConfigPanel);        		
        	}
        });
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
    public void renderHead(IHeaderResponse response) {
    	super.renderHead(response);
        response.renderCSSReference("style/pageStyles/wide.css");    	
    }
    
}
