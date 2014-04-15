package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPrintPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/*
Actions
If the user logged in is the creator of the procedure they will have:
    View - https://n4.fieldid.com/fieldid/w/procedureDefinitionPrint?4&id=281
    Edit - https://n4.fieldid.com/fieldid/w/procedureDef?3&id=281
    Delete
If the user logged in is part of the approval group (if one is set) they will have:
    Start Approval Process - https://n4.fieldid.com/fieldid/w/procedureDef?3&id=281
    View
    Delete
If the user logged in is not the author or the approver they will only have:
    View
 */



public class ProcedureApprovalsActionsCell extends Panel {

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    public ProcedureApprovalsActionsCell(String id, IModel<ProcedureDefinition> procedureDefinitionModel, final ProcedureListPanel procedureListPanel) {
        super(id);

        final AjaxLink<Void> deleteLink;
        final BookmarkablePageLink<Void> startApprovalLink;
        final BookmarkablePageLink<Void> editLink;
        final BookmarkablePageLink<Void> viewLink;

        final ProcedureDefinition procedureDefinition = (ProcedureDefinition) procedureDefinitionModel.getObject();

        viewLink = new BookmarkablePageLink<Void>("viewLink", ProcedureDefinitionPrintPage.class, PageParametersBuilder.id(procedureDefinitionModel.getObject().getId()));
//        viewLink.setVisible(procedure.getWorkflowState() == ProcedureWorkflowState.UNLOCKED || procedure.getWorkflowState() == ProcedureWorkflowState.LOCKED);
        add(viewLink);

        editLink = new BookmarkablePageLink<Void>("editLink", ProcedureDefinitionPage.class, PageParametersBuilder.id(procedureDefinitionModel.getObject().getId())) {
            public boolean isVisible() {
                return ( isAuthor(procedureDefinition) || ( isApprover(procedureDefinition) && isRejected(procedureDefinition) ) );
            }
        };

        startApprovalLink = new BookmarkablePageLink<Void>("startApprovalLink", ProcedureDefinitionPage.class, PageParametersBuilder.id(procedureDefinitionModel.getObject().getId())) {
            public boolean isVisible() {
                return (isApprover(procedureDefinition) && !isRejected(procedureDefinition));
            }
        };

        deleteLink =  new AjaxLink<Void>("deleteLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {

                try {
                    procedureDefinitionService.deleteProcedureDefinition(procedureDefinition);
                } catch (Exception e) {
                    error(new FIDLabelModel("error.eventdeleting").getObject());
                    target.add(((FieldIDTemplatePage) getPage()).getTopFeedbackPanel());
                }

                info(new FIDLabelModel("message.procedure_definitions.delete").getObject());
                target.add(procedureListPanel);
                target.add(((FieldIDTemplatePage) getPage()).getTopFeedbackPanel());

            }

            public boolean isVisible() {
                return ((isAuthor(procedureDefinition) || isApprover(procedureDefinition)) );
            }

        };


        WebMarkupContainer actionsList = new WebMarkupContainer("actionsList") {
            public boolean isVisible() {
                return ( editLink.isVisible() || startApprovalLink.isVisible() ||  deleteLink.isVisible());
            }

        };
        actionsList.setOutputMarkupId(true);

        actionsList.add(editLink);
        actionsList.add(startApprovalLink);
        actionsList.add(deleteLink);
//        actionsList.add(new BookmarkablePageLink<Void>("viewAssetLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(procedure.getAsset().getId())));
        add(actionsList);

    }


    private boolean isAuthor(ProcedureDefinition procedureDefinition) {

        if (procedureDefinitionService.isCurrentUserAuthor(procedureDefinition)) {
            return true;
        }

        return false;

    }

    private boolean isApprover(ProcedureDefinition procedureDefinition) {

        if (!isAuthor(procedureDefinition) && procedureDefinitionService.canCurrentUserApprove()) {
            return true;
        }
        return false;
    }

    private boolean isRejected(ProcedureDefinition procedureDefinition) {

        if (procedureDefinition.getPublishedState().equals(PublishedState.REJECTED ) && null != procedureDefinition.getRejectedDate()) {
            return true;
        }
        return false;
    }

}

