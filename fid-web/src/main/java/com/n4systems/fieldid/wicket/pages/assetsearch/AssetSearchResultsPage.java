package com.n4systems.fieldid.wicket.pages.assetsearch;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchCriteriaPanel;
import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchMassActionPanel;
import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchResultsPanel;
import com.n4systems.fieldid.wicket.components.reporting.SlidingReportSectionCollapseContainer;
import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.services.reporting.DashboardReportingService;

public class AssetSearchResultsPage extends FieldIDFrontEndPage {

    private AssetSearchResultsPanel reportResultsPanel;

    @SpringBean
    private DashboardReportingService dashboardReportingService;
    
    
    public AssetSearchResultsPage(PageParameters params) { 
    	super(params);
    	init(createSearchCriteriaModel(params));
    }
    
    public AssetSearchResultsPage(AssetSearchCriteriaModel searchCriteriaModel) {
    	super(new PageParameters());
    	init(searchCriteriaModel);
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

    private void init(AssetSearchCriteriaModel searchCriteriaModel) {
        Model<AssetSearchCriteriaModel> criteriaModel = new Model<AssetSearchCriteriaModel>(searchCriteriaModel);
        add(reportResultsPanel = new AssetSearchResultsPanel("resultsPanel", criteriaModel));

        SlidingReportSectionCollapseContainer criteriaExpandContainer = new SlidingReportSectionCollapseContainer("criteriaExpandContainer", new FIDLabelModel("label.reportcriteria"));
        criteriaExpandContainer.addContainedPanel(new AssetSearchCriteriaPanel("criteriaPanel", criteriaModel) {
        	@Override
        	protected void onNoDisplayColumnsSelected() {
        		reportResultsPanel.setVisible(false);
        	}
        });
        add(criteriaExpandContainer);
        add(new AssetSearchMassActionPanel("massActionPanel", criteriaModel));    	
	}

	@Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.asset_search_results"));
    }

}
