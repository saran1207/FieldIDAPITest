package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public abstract class ProcedureApprovalsPage extends FieldIDTemplatePage {

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.procedure_approvals"));
    }

    @Override
    protected void addNavBar(String navBarId) {

        // get count
        Long waitingApprovalsCount = procedureDefinitionService.getWaitingApprovalsCount("");
        Long rejectedApprovalsCount = procedureDefinitionService.getRejectedApprovalsCount("");

        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("label.procedure_waiting_approvals", waitingApprovalsCount)).page(ProcedureWaitingApprovalsPage.class).build(),
                aNavItem().label(new FIDLabelModel("label.procedure_rejected", rejectedApprovalsCount)).page(ProcedureRejectedPage.class).build()
        ));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/procedureDefinitionPage.js");
    }

}

