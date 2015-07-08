package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Created by rrana on 2014-04-10.
 */
public class ProcedureDateColumn extends PropertyColumn<ProcedureDefinition> {

    private boolean includeTime;

    public ProcedureDateColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        this(displayModel, sortProperty, propertyExpression, false);
    }

    public ProcedureDateColumn(IModel<String> displayModel, String sortProperty, String propertyExpression, boolean includeTime) {
        super(displayModel, sortProperty, propertyExpression);
        this.includeTime = includeTime;
    }

    @Override
    public void populateItem(Item<ICellPopulator<ProcedureDefinition>> item, String id, IModel<ProcedureDefinition> procedureModel) {
        item.add(new ProcedureDateCell(id, procedureModel, getPropertyExpression(), includeTime))
                .add(new AttributeAppender("class", new Model<String>("date"), ""));
    }
}
