package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.behavior.ConfirmBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.Asset;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class LotoActionGroup extends Panel {

    @SpringBean
    ProcedureDefinitionService procedureDefinitionService;

    public LotoActionGroup(String id, final IModel<Asset> assetModel) {
        super(id);

        add(new Link("authorLink") {
            {
                if(procedureDefinitionService.hasPublishedProcedureDefinition(assetModel.getObject())) {
                    add(new ConfirmBehavior(new FIDLabelModel("message.author_procedure_warning")));
                }
            }
            @Override
            public void onClick() {
                setResponsePage(new ProcedureDefinitionPage(assetModel.getObject()));
            }
        });

    }
}
