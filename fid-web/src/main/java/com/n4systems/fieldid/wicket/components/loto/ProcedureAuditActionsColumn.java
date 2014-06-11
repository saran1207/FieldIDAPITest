package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2014-06-11.
 */
public class ProcedureAuditActionsColumn extends AbstractColumn<Procedure> {



    private ProcedureAuditListPanel procedureListPanel;

    public ProcedureAuditActionsColumn(ProcedureAuditListPanel procedureListPanel) {
        super(new FIDLabelModel(""));
        this.procedureListPanel = procedureListPanel;
    }

    @Override
    public void populateItem(Item<ICellPopulator<Procedure>> cellItem, String componentId, final IModel<Procedure> rowModel) {

        cellItem.add(new ProcedureAuditActionsCell(componentId, rowModel, procedureListPanel));

    }

}
