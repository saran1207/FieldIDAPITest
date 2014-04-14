package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2014-04-11.
 */
public class PublishedProcedureActionsColumn extends AbstractColumn<ProcedureDefinition> {



    private ProcedureListPanel procedureListPanel;

    public PublishedProcedureActionsColumn(ProcedureListPanel procedureListPanel) {
        super(new FIDLabelModel(""));
        this.procedureListPanel = procedureListPanel;
    }

    @Override
    public void populateItem(Item<ICellPopulator<ProcedureDefinition>> cellItem, String componentId, final IModel<ProcedureDefinition> rowModel) {

        cellItem.add(new PublishedProcedureActionsCell(componentId, rowModel, procedureListPanel));

    }

}
