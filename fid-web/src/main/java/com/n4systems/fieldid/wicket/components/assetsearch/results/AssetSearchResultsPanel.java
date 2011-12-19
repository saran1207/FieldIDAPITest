package com.n4systems.fieldid.wicket.components.assetsearch.results;

import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.data.AssetSearchDataProvider;
import com.n4systems.fieldid.wicket.data.FieldIdAPIDataProvider;
import com.n4systems.model.search.AssetSearchCriteriaModel;
import com.n4systems.util.views.RowView;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;

public class AssetSearchResultsPanel extends SRSResultsPanel<AssetSearchCriteriaModel> {

    public AssetSearchResultsPanel(String id, final IModel<AssetSearchCriteriaModel> criteriaModel) {
        super(id, criteriaModel);
    }

    @Override
    protected IColumn<RowView> createActionsColumn() {
        return new AssetActionsColumn();
    }

    @Override
    protected FieldIdAPIDataProvider createDataProvider(IModel<AssetSearchCriteriaModel> criteriaModel) {
        return new AssetSearchDataProvider(criteriaModel.getObject());
    }

}
