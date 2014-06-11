package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.TimeAgoLabel;
import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Created by rrana on 2014-06-11.
 */
public class ProcedureAuditDueDateCell extends Panel {

    public ProcedureAuditDueDateCell(String id, IModel<? extends Procedure> procedureModel) {
        super(id);
        Procedure procedure = procedureModel.getObject();
        add(new TimeAgoLabel("dueDate", Model.of(procedure.getDueDate()), FieldIDSession.get().getSessionUser().getTimeZone()));
    }
}