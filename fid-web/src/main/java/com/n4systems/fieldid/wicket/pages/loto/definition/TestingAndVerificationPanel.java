package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import static ch.lambdaj.Lambda.on;

/**
 * Created by rrana on 2014-09-17.
 */
@ComponentWithExternalHtml
public class TestingAndVerificationPanel extends Panel {

    public TestingAndVerificationPanel(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

        boolean showPanel = true;
        if(model.getObject().getTestingAndVerification() == null) {
            showPanel = false;
        } else if (model.getObject().getTestingAndVerification().isEmpty()) {
            showPanel = false;
        } else {
            showPanel = true;
        }
        Label process = new Label("testingAndVerification", ProxyModel.of(model, on(ProcedureDefinition.class).getTestingAndVerification()));
        process.setVisible(showPanel);

        add(process);
    }
}
