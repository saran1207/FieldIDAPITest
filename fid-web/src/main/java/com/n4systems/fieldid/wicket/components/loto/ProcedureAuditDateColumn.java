package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.model.ProcedureAuditEvent;
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
public class ProcedureAuditDateColumn extends PropertyColumn<ProcedureAuditEvent> {


    public ProcedureAuditDateColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<ProcedureAuditEvent>> item, String id, IModel<ProcedureAuditEvent> procedureModel) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date date7DaysFromNow = cal.getTime();

        item.add(new ProcedureAuditDateCell(id, procedureModel))
                .add(new AttributeAppender("class", new Model<String>("created"), " "));

        if(procedureModel.getObject().getDueDate().before(new Date())) {
            item.add(new AttributeAppender("class", new Model<String>("overdue"), " "));
        } else if (procedureModel.getObject().getDueDate().before(date7DaysFromNow)) {
            item.add(new AttributeAppender("class", new Model<String>("sevenDays"), " "));
        }
    }
}