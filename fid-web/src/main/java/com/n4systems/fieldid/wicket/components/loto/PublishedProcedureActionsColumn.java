package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
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

        if(rowModel.getObject().getPublishedState().equals(PublishedState.PUBLISHED)) {
            cellItem.add(new PublishedProcedureActionsCell(componentId, rowModel, procedureListPanel));
        } else if (rowModel.getObject().getPublishedState().equals(PublishedState.DRAFT)) {
            cellItem.add(new DraftProcedureActionsCell(componentId, rowModel, procedureListPanel));
        } else {
            cellItem.add(new PreviouslyPublishedProcedureActionsCell(componentId, rowModel, procedureListPanel));
        }
    }

}
