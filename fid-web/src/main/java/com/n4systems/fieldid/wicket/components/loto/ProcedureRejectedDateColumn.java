package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public class ProcedureRejectedDateColumn extends PropertyColumn<ProcedureDefinition> {


    public ProcedureRejectedDateColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<ProcedureDefinition>> item, String id, IModel<ProcedureDefinition> procedureModel) {
        item.add(new ProcedureRejectedDateCell(id, procedureModel));
    }
}
