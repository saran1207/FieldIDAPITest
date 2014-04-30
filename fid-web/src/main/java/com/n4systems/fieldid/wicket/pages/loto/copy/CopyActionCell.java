package com.n4systems.fieldid.wicket.pages.loto.copy;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.Asset;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class CopyActionCell extends Panel {

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    public CopyActionCell(String id, final IModel<ProcedureDefinition> procedureDefinition, final  IModel<Asset> assetModel) {
        super(id);

        add(new Link("copyLink") {
            @Override
            public void onClick() {
                ProcedureDefinition copiedDefinition = procedureDefinitionService.cloneProcedureDefinitionForCopy(procedureDefinition.getObject(), assetModel.getObject());
                copiedDefinition.setPublishedState(PublishedState.DRAFT);
                setResponsePage(new ProcedureDefinitionPage(Model.of(copiedDefinition)));
            }
        });
    }
}
