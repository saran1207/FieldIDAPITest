package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import static ch.lambdaj.Lambda.on;

@ComponentWithExternalHtml
public class PrintAsset extends Panel {


    public PrintAsset(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

        //assetDescription
        add(new Label("assetDescription", ProxyModel.of(model, on(ProcedureDefinition.class).getAsset().getDescription())));

    }
}
