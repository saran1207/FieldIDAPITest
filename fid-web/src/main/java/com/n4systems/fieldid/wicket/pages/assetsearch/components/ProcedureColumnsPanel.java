package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.service.search.columns.ProcedureColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ProcedureCriteria;
import com.n4systems.model.search.ReportConfiguration;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;


public class ProcedureColumnsPanel extends AbstractColumnsPanel<ProcedureCriteria> {

    @SpringBean
    private ProcedureColumnsService procedureColumnsService;

    public ProcedureColumnsPanel(String id, IModel<ProcedureCriteria> model) {
		super(id, model);
		setOutputMarkupId(true);
		setMarkupId(id);
	}

    @Override
    protected void updateColumns(IModel<List<ColumnMappingGroupView>> dynamicAssetColumnsModel, IModel<List<ColumnMappingGroupView>> dynamicEventColumnsModel) {
    }

    protected ReportConfiguration loadReportConfiguration() {
        return procedureColumnsService.getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
    }

}
