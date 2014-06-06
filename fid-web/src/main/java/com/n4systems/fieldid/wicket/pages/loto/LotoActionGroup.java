package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.behavior.ConfirmBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.loto.copy.CopyProceduresList;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.Asset;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class LotoActionGroup extends Panel {

    @SpringBean
    ProcedureDefinitionService procedureDefinitionService;

    public LotoActionGroup(String id, final IModel<Asset> assetModel) {
        super(id);

        WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer");

        optionsContainer.add(new Link("authorNewLink") {
            {
                if (procedureDefinitionService.hasPublishedProcedureDefinition(assetModel.getObject())) {
                    add(new ConfirmBehavior(new FIDLabelModel("message.author_procedure_warning")));
                }
            }
            @Override
            public void onClick() {
                setResponsePage(new ProcedureDefinitionPage(assetModel.getObject()));
            }
        });

        optionsContainer.add(new Link("recurringSchedulesLink") {
            @Override
            public void onClick() {
                setResponsePage(RecurringLotoSchedulesPage.class, PageParametersBuilder.uniqueId(assetModel.getObject().getId()));
            }
        });

        optionsContainer.add(new Link("copyExistingLink") {
            {
                if (procedureDefinitionService.hasPublishedProcedureDefinition(assetModel.getObject())) {
                    add(new ConfirmBehavior(new FIDLabelModel("message.author_procedure_warning")));
                }
            }
            @Override
            public void onClick() {
                setResponsePage(new CopyProceduresList(assetModel));
            }
        });

        optionsContainer.add(new Link("viewPublishedLink") {
            @Override
            public void onClick() {
                setResponsePage(new PublishedListAllPage(assetModel.getObject().getDisplayName(), assetModel.getObject(), false, true));
            }
        });

        optionsContainer.add(new Link("draftsLink") {
            @Override
            public void onClick() {
                setResponsePage(new DraftListAllPage(assetModel.getObject().getDisplayName(), assetModel.getObject(), false, true));
            }
        });

        optionsContainer.add(new Link("waitingForApprovalLink") {
            @Override
            public void onClick() {
                setResponsePage(new ProcedureWaitingApprovalsPage(assetModel.getObject().getDisplayName(), assetModel.getObject(), false, true));
            }
        });

        optionsContainer.add(new Link("rejectedLink") {
            @Override
            public void onClick() {
                setResponsePage(new ProcedureRejectedPage(assetModel.getObject().getDisplayName(), assetModel.getObject(), false, true));
            }
        });

        optionsContainer.add(new Link("previouslyPublishedLink") {
            @Override
            public void onClick() {
                setResponsePage(new PreviouslyPublishedListAllPage(assetModel.getObject().getDisplayName(), assetModel.getObject(), false, true));
            }
        });

        add(optionsContainer);

    }
}
