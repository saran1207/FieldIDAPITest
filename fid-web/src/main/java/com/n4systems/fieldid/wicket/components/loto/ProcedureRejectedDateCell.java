package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


public class ProcedureRejectedDateCell extends Panel {
    public ProcedureRejectedDateCell(String id, IModel<ProcedureDefinition> procedureModel) {
        super(id);
        ProcedureDefinition procedure = procedureModel.getObject();
        add(new Label("rejectedDate", new DayDisplayModel(Model.of(procedure.getRejectedDate())).withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone())));

    }
}