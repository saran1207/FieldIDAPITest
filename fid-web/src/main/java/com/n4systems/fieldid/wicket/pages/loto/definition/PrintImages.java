package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.components.image.ImageList;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class PrintImages extends Panel {

    // add image list here...
    public PrintImages(String id, IModel<ProcedureDefinition> model) {
        super(id,model);
        add(new ImageList("imageList", new PropertyModel(model,"images")));
    }
}
