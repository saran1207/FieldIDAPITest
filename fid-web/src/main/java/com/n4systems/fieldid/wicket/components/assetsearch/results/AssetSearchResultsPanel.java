package com.n4systems.fieldid.wicket.components.assetsearch.results;

import com.n4systems.fieldid.service.search.columns.AssetTextOrFilterSearchService;
import com.n4systems.fieldid.wicket.components.GpsModel;
import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.data.AssetSearchDataProvider;
import com.n4systems.fieldid.wicket.data.FieldIdAPIDataProvider;
import com.n4systems.model.api.HasGpsLocation;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.services.reporting.AssetSearchRecord;
import com.n4systems.services.search.MappedResults;
import com.n4systems.util.views.RowView;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class AssetSearchResultsPanel extends SRSResultsPanel<AssetSearchCriteria> {

    private @SpringBean
    AssetTextOrFilterSearchService assetTextOrFilterSearchService;

    public AssetSearchResultsPanel(String id, final IModel<AssetSearchCriteria> criteriaModel) {
        super(id, criteriaModel);
        resultButtons.setVisible(true);
    }

    @Override
    protected GpsModel<? extends HasGpsLocation> createMapModel(final IModel<AssetSearchCriteria> criteriaModel) {
        return new GpsModel<AssetSearchRecord>() {
            @Override protected MappedResults<AssetSearchRecord> load() {
                return assetTextOrFilterSearchService.performSearch(criteriaModel.getObject());
            }
        };
    }

    @Override
    protected IColumn<RowView> createActionsColumn() {
        return new AssetActionsColumn();
    }

    @Override
    protected FieldIdAPIDataProvider createDataProvider(IModel<AssetSearchCriteria> criteriaModel) {
        return new AssetSearchDataProvider(criteriaModel.getObject()) {
            @Override protected void storeIdList() {
                super.storeIdList();
                selectedRows.clearIndexes();
                selectedRows.validateIndexes( dataTable.getTable().getCurrentPage(), dataTable.getTable().getItemsPerPage(), getCurrentPageIdList());
            }
        };
    }




}

