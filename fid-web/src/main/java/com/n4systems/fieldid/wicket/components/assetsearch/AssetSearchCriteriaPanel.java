package com.n4systems.fieldid.wicket.components.assetsearch;

import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.model.AssetType;
import com.n4systems.model.search.ColumnMappingGroupView;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateRangePicker;
import com.n4systems.fieldid.wicket.components.search.IdentifiersCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OrderDetailsCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OwnershipCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.SRSCriteriaPanel;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.model.search.ReportConfiguration;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class AssetSearchCriteriaPanel extends SRSCriteriaPanel<AssetSearchCriteriaModel> {

    private @SpringBean DynamicColumnsService dynamicColumnsService;

    public AssetSearchCriteriaPanel(String id, IModel<AssetSearchCriteriaModel> criteriaModel) {
        super(id, criteriaModel);
    }

    public AssetSearchCriteriaPanel(String id) {
        super(id, new Model<AssetSearchCriteriaModel>(new AssetSearchCriteriaModel()));
    }

    @Override
    protected void populateForm(SearchCriteriaForm form) {
        form.add(new IdentifiersCriteriaPanel("identifiersCriteriaPanel", form.getModel()));
        form.addAssetDetailsPanel("assetDetailsCriteriaPanel");
        form.add(new OwnershipCriteriaPanel("ownershipCriteriaPanel", form.getModel()));
        form.add(new OrderDetailsCriteriaPanel("orderDetailsCriteriaPanel"));        
        form.add(new DateRangePicker<AssetSearchCriteriaModel>("dateRangePicker", form.getModel()));
    }

    @Override
    protected WebPage createResultsPage(AssetSearchCriteriaModel criteria) {
        return new AssetSearchResultsPage(criteriaModel.getObject());
    }

    @Override
    protected ReportConfiguration loadReportConfiguration() {
        return new AssetColumnsService().getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
    }

	@Override
	protected AssetSearchCriteriaModel createNewCriteriaModel() {
		return new AssetSearchCriteriaModel();
	}

    @Override
    protected List<ColumnMappingGroupView> getDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
        return dynamicColumnsService.getDynamicAssetColumnsForSearch(assetType, availableAssetTypes);
    }

}
