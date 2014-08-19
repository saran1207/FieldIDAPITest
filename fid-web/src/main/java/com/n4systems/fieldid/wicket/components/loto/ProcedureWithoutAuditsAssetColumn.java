package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;


public class ProcedureWithoutAuditsAssetColumn extends PropertyColumn<ProcedureDefinition> {

    public ProcedureWithoutAuditsAssetColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<ProcedureDefinition>> item, String id, IModel<ProcedureDefinition> procedureModel) {
        item.add(new ProcedureWithoutAuditsAssetCell(id, procedureModel));
    }
}