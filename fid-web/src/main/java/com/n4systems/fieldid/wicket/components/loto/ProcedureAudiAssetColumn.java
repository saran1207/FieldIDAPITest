package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by rrana on 2014-06-11.
 */
public class ProcedureAudiAssetColumn extends PropertyColumn<Procedure> {


    public ProcedureAudiAssetColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<Procedure>> item, String id, IModel<Procedure> procedureModel) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date date7DaysFromNow = cal.getTime();

        item.add(new ProcedureAuditAssetCell(id, procedureModel));

        if(procedureModel.getObject().getDueDate().before(new Date())) {
            item.add(new AttributeAppender("class", new Model<String>("overdue"), " "));
        } else if (procedureModel.getObject().getDueDate().before(date7DaysFromNow)) {
            item.add(new AttributeAppender("class", new Model<String>("sevenDays"), " "));
        }
    }
}
