package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.util.time.DateUtil;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Created by rrana on 2014-06-11.
 */
public class ProcedureAuditDateCell extends Panel {

    public ProcedureAuditDateCell(String id, IModel<? extends ProcedureAuditEvent> procedureModel) {
        super(id);
        ProcedureAuditEvent procedure = procedureModel.getObject();

        DayDisplayModel dayDisplayModel = new DayDisplayModel(Model.of(procedure.getDueDate()));

        //Since it would be ideal to show the time if relevant, we need to check to see if this has a time of midnight,
        //meaning that it's an "all day" date and technically has no time.  This also prevents the date being shifted
        //around by timezone changes.
        if(!DateUtil.isMidnight(procedure.getDueDate())) {
            dayDisplayModel.withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone()).includeTime();
        }
        add(new Label("scheduleDate", dayDisplayModel));
    }
}