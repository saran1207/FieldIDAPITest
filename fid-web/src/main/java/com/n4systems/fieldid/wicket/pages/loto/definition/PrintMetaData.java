package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class PrintMetaData extends Panel {
    public PrintMetaData(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

//        add(new ExternalS3Image("image", path));
    }
}
