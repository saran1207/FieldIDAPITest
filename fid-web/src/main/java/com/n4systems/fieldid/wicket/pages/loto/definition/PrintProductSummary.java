package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.services.SecurityContext;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static ch.lambdaj.Lambda.on;

@ComponentWithExternalHtml
public class PrintProductSummary extends Panel {

    private @SpringBean SecurityContext securityContext;

    public PrintProductSummary(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

        //warnings
        add(new Label("warnings", ProxyModel.of(model, on(ProcedureDefinition.class).getWarnings())));

    }
}
