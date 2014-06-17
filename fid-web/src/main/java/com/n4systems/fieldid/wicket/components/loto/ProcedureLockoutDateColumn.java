package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2014-05-19.
 */
public class ProcedureLockoutDateColumn extends PropertyColumn<Procedure> {


    public ProcedureLockoutDateColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<Procedure>> item, String id, IModel<Procedure> procedureModel) {
        item.add(new ProcedureLockoutDateCell(id, procedureModel));
    }
}
