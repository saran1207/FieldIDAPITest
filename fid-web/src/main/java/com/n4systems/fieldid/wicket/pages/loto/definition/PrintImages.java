package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.components.image.ImageList;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

@ComponentWithExternalHtml
public class PrintImages extends ImageList<ProcedureDefinitionImage> {

    public PrintImages(String id, IModel<ProcedureDefinition> model) {
        super(id,new PropertyModel(model,"images"));
    }
}
