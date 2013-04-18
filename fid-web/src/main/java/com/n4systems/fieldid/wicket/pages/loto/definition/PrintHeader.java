package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import static ch.lambdaj.Lambda.on;

@ComponentWithExternalHtml
public class PrintHeader extends Panel {
    public PrintHeader(String id, IModel<ProcedureDefinition> model) {
        super(id,model);
        add(new Label("code", ProxyModel.of(model,on(ProcedureDefinition.class).getProcedureCode())));
        add(new Label("warnings", ProxyModel.of(model,on(ProcedureDefinition.class).getWarnings())));
        add(new Label("equipmentNumber", ProxyModel.of(model,on(ProcedureDefinition.class).getEquipmentNumber())));
        add(new Label("equipmentDescription", ProxyModel.of(model,on(ProcedureDefinition.class).getEquipmentDescription())));
        add(new Label("electronicIdentifier", ProxyModel.of(model,on(ProcedureDefinition.class).getElectronicIdentifier())));
        add(new Label("developedBy", ProxyModel.of(model,on(ProcedureDefinition.class).getDevelopedBy().getDisplayName())));
        add(new Label("equipmentLocation", ProxyModel.of(model,on(ProcedureDefinition.class).getEquipmentLocation())));

    }
}
