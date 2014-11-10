package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;


public class ProcedureApprovalsActionsColumn extends AbstractColumn<ProcedureDefinition> {

    private ProcedureListPanel procedureListPanel;

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    public ProcedureApprovalsActionsColumn(ProcedureListPanel procedureListPanel) {
        super(new Model<String>(""));
        this.procedureListPanel = procedureListPanel;
    }

    @Override
    public void populateItem(Item<ICellPopulator<ProcedureDefinition>> cellItem, String componentId, IModel<ProcedureDefinition> procedureDefinitionModel) {
        ProcedureDefinition procedureDefinition = procedureDefinitionModel.getObject();
        cellItem.add(new ProcedureApprovalsActionsCell(componentId, procedureDefinitionModel, procedureListPanel));

    }
}

