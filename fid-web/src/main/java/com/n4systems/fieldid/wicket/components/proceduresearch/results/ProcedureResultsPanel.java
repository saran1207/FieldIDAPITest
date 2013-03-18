package com.n4systems.fieldid.wicket.components.proceduresearch.results;

import com.n4systems.fieldid.wicket.components.search.results.SRSResultsPanel;
import com.n4systems.fieldid.wicket.data.FieldIdAPIDataProvider;
import com.n4systems.fieldid.wicket.data.ProcedureDataProvider;
import com.n4systems.model.search.ProcedureCriteria;
import com.n4systems.util.views.RowView;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;

public class ProcedureResultsPanel extends SRSResultsPanel<ProcedureCriteria> {

    public ProcedureResultsPanel(String id, IModel<ProcedureCriteria> criteriaModel) {
        super(id, criteriaModel);
    }

    @Override
    protected IColumn<RowView> createActionsColumn() {
        return new ProcedureActionsColumn();
    }

    @Override
    protected FieldIdAPIDataProvider createDataProvider(IModel<ProcedureCriteria> criteriaModel) {
        return new ProcedureDataProvider(criteriaModel.getObject());
    }

}
