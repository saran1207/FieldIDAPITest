package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class PrintImages extends Panel {

    // add image list here...
    public PrintImages(String id, IModel<ProcedureDefinition> model) {
        super(id,model);
    }
}
