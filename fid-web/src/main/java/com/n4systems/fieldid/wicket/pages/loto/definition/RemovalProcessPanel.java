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
public class RemovalProcessPanel extends Panel {

    public RemovalProcessPanel(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

        boolean showPanel = true;
        if(model.getObject().getApplicationProcess() == null) {
            showPanel = false;
        } else if (model.getObject().getApplicationProcess().isEmpty()) {
            showPanel = false;
        } else {
            showPanel = true;
        }
        Label process = new Label("process", ProxyModel.of(model, on(ProcedureDefinition.class).getRemovalProcess()));
        process.setVisible(showPanel);

        add(process);
    }
}
