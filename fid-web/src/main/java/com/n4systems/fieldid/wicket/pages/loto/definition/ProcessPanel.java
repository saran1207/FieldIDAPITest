package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import static ch.lambdaj.Lambda.on;

/**
 * Created by rrana on 2014-07-08.
 */

@ComponentWithExternalHtml
public class ProcessPanel extends Panel {

    public ProcessPanel(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

        //assetDescription
        //add(new Label("assetDescription", ProxyModel.of(model, on(ProcedureDefinition.class).getAsset().getDescription())));

        add(new Label("process", ProxyModel.of(model, on(ProcedureDefinition.class).getApplicationProcess())));

    }
}
