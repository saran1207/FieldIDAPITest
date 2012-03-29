package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchCriteriaPanel;
import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchResultsPanel;
import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.SearchBlankSlate;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.SearchCriteriaPanel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.components.SearchSubMenu;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.services.reporting.DashboardReportingService;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class SearchResultsPage extends AbstractSearchPage<AssetSearchCriteria> {

    private @SpringBean DashboardReportingService dashboardReportingService;
    private @SpringBean SavedAssetSearchService savedAssetSearchService;

    public SearchResultsPage(PageParameters params) {
    	super(params);
		AssetSearchCriteria model = createSearchCriteriaModel(params);
		init(model, createSavedItemFromCriteria(model), true);
    }

	public SearchResultsPage(SavedSearchItem savedSearchItem) {
        super(new PageParameters());
        init(savedSearchItem.getSearchCriteria(), savedSearchItem, true);
	}

    public SearchResultsPage(AssetSearchCriteria searchCriteria, boolean showLeftMenu) {
        this(searchCriteria, null, showLeftMenu);
    }

    public SearchResultsPage(AssetSearchCriteria searchCriteriaModel, SavedSearchItem savedSearchItem, boolean showLeftMenu) {
    	super(new PageParameters());
        SavedSearchItem newSavedSearchItem = new SavedSearchItem(searchCriteriaModel);
       	newSavedSearchItem.setId(savedSearchItem==null ? null : savedSearchItem.getId());
    	init(searchCriteriaModel, newSavedSearchItem, showLeftMenu);
    }

    @Override
    protected SavedItem<AssetSearchCriteria> createSavedItemFromCriteria(AssetSearchCriteria searchCriteriaModel) {
        return new SavedSearchItem(searchCriteriaModel);
    }

    @Override
    protected Component createSubMenu(String id, Model<AssetSearchCriteria> criteriaModel) {
        return new SearchSubMenu(id, criteriaModel) {
            @Override protected Link createSaveLink(String id) {
                return SearchResultsPage.this.createSaveLink(id, true);                
            };
            @Override protected IModel<String> getHeaderModel() {
                return new PropertyModel<String>(savedItem,"name");
            }
        };
    }

    @Override
    protected Component createCriteriaPanel(String id, final Model<AssetSearchCriteria> model) {
        return new SearchCriteriaPanel(id, model) {
            @Override protected void onSearchSubmit() {
                setResponsePage(new SearchResultsPage(model.getObject(),false));
            }
            @Override protected void onNoDisplayColumnsSelected() { }
        };
    }

    @Override
    protected Component createBlankSlate(String id) {
        return new SearchBlankSlate(id);
    }

    @Override
    protected Component createResultsPanel(String id, Model<AssetSearchCriteria> criteriaModel) {
        return  new AssetSearchResultsPanel(id, criteriaModel) {
            @Override protected void updateSelectionStatus(AjaxRequestTarget target) {
                super.updateSelectionStatus(target);
                target.add(searchMenu);
            };
        };
    }

    @Override
    protected void saveLastSearch(AssetSearchCriteria searchCriteria) {
        savedAssetSearchService.saveLastSearch(searchCriteria);
    }

    @Override
    protected Page createSaveReponsePage(boolean overwrite) {
        return new SaveAssetSearchPage((SavedSearchItem) savedItem, SearchResultsPage.this, overwrite);
    }

    @Override
    protected Component createCriteriaPanel(String id, Model<AssetSearchCriteria> criteriaModel, SavedItem<AssetSearchCriteria> savedItem) {
        return new AssetSearchCriteriaPanel(id, criteriaModel, (SavedSearchItem) savedItem);
    }

	private AssetSearchCriteria createSearchCriteriaModel(PageParameters params) {
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
            // just return default if this doesn't work.  (most likely bad or incorrect parameters).
		}
		return dashboardReportingService.getDefaultAssetSearchCritieriaModel();
	}

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new PropertyModel<String>(this, "pageLabel"));
    }

    public String getPageLabel() {
        IModel<String> pageLabelModel = new FIDLabelModel("title.asset_search_results");
        if (searchCriteria.getSavedReportName() != null) {
            pageLabelModel = new Model<String>(pageLabelModel.getObject() + " for - " + searchCriteria.getSavedReportName());
        }
        return pageLabelModel.getObject();
    }

    
}
