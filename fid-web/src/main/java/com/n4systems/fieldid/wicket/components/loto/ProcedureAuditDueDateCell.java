package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.TimeAgoLabel;
import com.n4systems.model.ProcedureAuditEvent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;

/**
 * Created by rrana on 2014-06-11.
 */
public class ProcedureAuditDueDateCell extends Panel {

    public ProcedureAuditDueDateCell(String id, IModel<? extends ProcedureAuditEvent> procedureModel) {
        super(id);
        ProcedureAuditEvent procedure = procedureModel.getObject();
        //add(new TimeAgoLabel("dueDate", Model.of(procedure.getDueDate()), FieldIDSession.get().getSessionUser().getTimeZone()));
        add(new TimeAgoLabel("dueDate", new PropertyModel<Date>(procedureModel, "dueDate"), FieldIDSession.get().getSessionUser().getTimeZone()));
    }
}