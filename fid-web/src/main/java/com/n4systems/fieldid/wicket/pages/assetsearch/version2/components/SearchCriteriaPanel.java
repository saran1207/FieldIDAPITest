package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateRangePicker;
import com.n4systems.fieldid.wicket.components.search.IdentifiersCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OrderDetailsCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OwnershipCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.SRSCriteriaPanel;
import com.n4systems.fieldid.wicket.pages.assetsearch.AssetSearchResultsPage;
import com.n4systems.model.AssetType;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.utils.DateRange;

public class SearchCriteriaPanel extends SRSCriteriaPanel<SavedSearchItem, AssetSearchCriteriaModel> {

    private @SpringBean DynamicColumnsService dynamicColumnsService;

    public SearchCriteriaPanel(String id, IModel<AssetSearchCriteriaModel> criteriaModel, SavedSearchItem savedItem) {
        super(id, criteriaModel, savedItem);
    }

    public SearchCriteriaPanel(String id) {
        super(id, new Model<AssetSearchCriteriaModel>(new AssetSearchCriteriaModel()), null);
    }

    @Override
    protected void populateForm(SearchCriteriaForm form) {
        form.add(new IdentifiersCriteriaPanel("identifiersCriteriaPanel", form.getModel()));
        form.addAssetDetailsPanel("assetDetailsCriteriaPanel");
        form.add(new OwnershipCriteriaPanel("ownershipCriteriaPanel", form.getModel()));
        form.add(new OrderDetailsCriteriaPanel("orderDetailsCriteriaPanel"));        
        form.add(new DateRangePicker("dateRangePicker", new PropertyModel<DateRange>(form.getModel(), "dateRange")));
    }

    @Override
    protected WebPage createResultsPage(AssetSearchCriteriaModel criteria, SavedSearchItem savedItem) {
        return new AssetSearchResultsPage(criteriaModel.getObject(), savedItem);
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
