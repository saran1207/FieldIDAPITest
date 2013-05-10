package com.n4systems.fieldid.wicket.pages.assetsearch.components;


import com.n4systems.model.search.ProcedureCriteria;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

public class ProcedureCriteriaPanel extends AbstractCriteriaPanel<ProcedureCriteria> {

    public ProcedureCriteriaPanel(String id, Model<ProcedureCriteria> model) {
        super(id,model);
    }

    @Override
    protected AbstractColumnsPanel createColumnsPanel(String id, Model<ProcedureCriteria> model) {
        return new ProcedureColumnsPanel(id, model);
    }

    @Override
    protected Panel createFiltersPanel(String filters, Model<ProcedureCriteria> model) {
        return new ProcedureFilterPanel("filters",model);
    }

    public ProcedureColumnsPanel getReportingColumnsPanel() {
        return (ProcedureColumnsPanel) columns;
    }


}
