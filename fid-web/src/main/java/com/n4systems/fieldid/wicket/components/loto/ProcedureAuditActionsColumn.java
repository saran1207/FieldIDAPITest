package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.ProcedureAuditEvent;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by rrana on 2014-06-11.
 */
public class ProcedureAuditActionsColumn extends AbstractColumn<ProcedureAuditEvent> {



    private ProcedureAuditListPanel procedureListPanel;

    public ProcedureAuditActionsColumn(ProcedureAuditListPanel procedureListPanel) {
        super(new FIDLabelModel(""));
        this.procedureListPanel = procedureListPanel;
    }

    @Override
    public void populateItem(Item<ICellPopulator<ProcedureAuditEvent>> cellItem, String componentId, final IModel<ProcedureAuditEvent> rowModel) {

        cellItem.add(new ProcedureAuditActionsCell(componentId, rowModel, procedureListPanel));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        final Date date7DaysFromNow = cal.getTime();

        if(rowModel.getObject().getDueDate().before(new Date())) {
            cellItem.add(new AttributeAppender("class", new Model<String>("overdue"), " "));
        } else if (rowModel.getObject().getDueDate().before(date7DaysFromNow)) {
            cellItem.add(new AttributeAppender("class", new Model<String>("sevenDays"), " "));
        }


    }

}
