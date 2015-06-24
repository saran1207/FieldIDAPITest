package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;

/**
 * Created by rrana on 2014-04-10.
 */
public class ProcedureDateCell extends Panel {

    public ProcedureDateCell(String id, IModel<? extends ProcedureDefinition> procedureModel, String property) {
        super(id);
        add(new Label("createdDate", new DayDisplayModel(new PropertyModel<>(procedureModel, property)).withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone())));
    }
}