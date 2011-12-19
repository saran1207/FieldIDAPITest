package com.n4systems.fieldid.wicket.pages.assetsearch;

import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchCriteriaPanel;
import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchMassActionPanel;
import com.n4systems.fieldid.wicket.components.assetsearch.results.AssetSearchResultsPanel;
import com.n4systems.fieldid.wicket.components.reporting.SlidingReportSectionCollapseContainer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class AssetSearchResultsPage extends FieldIDFrontEndPage {

    private AssetSearchResultsPanel reportResultsPanel;

    public AssetSearchResultsPage(AssetSearchCriteriaModel searchCriteriaModel) {
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
