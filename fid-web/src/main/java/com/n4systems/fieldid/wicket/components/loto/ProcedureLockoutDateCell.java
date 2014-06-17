package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Created by rrana on 2014-05-19.
 */
public class ProcedureLockoutDateCell extends Panel {

    public ProcedureLockoutDateCell(String id, IModel<? extends Procedure> procedureModel) {
        super(id);
        Procedure procedure = procedureModel.getObject();
        add(new Label("lockedDate", new DayDisplayModel(Model.of(procedure.getLockDate())).withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone())));
    }
}