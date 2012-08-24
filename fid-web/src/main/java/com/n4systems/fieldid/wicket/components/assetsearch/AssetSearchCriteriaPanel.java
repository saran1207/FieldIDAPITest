package com.n4systems.fieldid.wicket.components.assetsearch;

import com.n4systems.fieldid.service.search.columns.AssetColumnsService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateRangePicker;
import com.n4systems.fieldid.wicket.components.search.IdentifiersCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OrderDetailsCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OwnershipCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.SRSCriteriaPanel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.SearchPage;
import com.n4systems.model.AssetType;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.utils.DateRange;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class AssetSearchCriteriaPanel extends SRSCriteriaPanel<SavedSearchItem, AssetSearchCriteria> {

    private @SpringBean DynamicColumnsService dynamicColumnsService;

    public AssetSearchCriteriaPanel(String id, IModel<AssetSearchCriteria> criteriaModel, SavedSearchItem savedItem) {
        super(id, criteriaModel, savedItem);
    }

    public AssetSearchCriteriaPanel(String id) {
        super(id, new Model<AssetSearchCriteria>(new AssetSearchCriteria()), null);
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
    protected WebPage createResultsPage(AssetSearchCriteria criteria, SavedSearchItem savedItem) {
        return new SearchPage(criteriaModel.getObject(), savedItem);
    }

    @Override
    protected ReportConfiguration loadReportConfiguration() {
        return new AssetColumnsService().getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
    }

	@Override
	protected AssetSearchCriteria createNewCriteriaModel() {
		return new AssetSearchCriteria();
	}

    @Override
    protected List<ColumnMappingGroupView> getDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
        return dynamicColumnsService.getDynamicAssetColumnsForSearch(assetType, availableAssetTypes);
    }

}
