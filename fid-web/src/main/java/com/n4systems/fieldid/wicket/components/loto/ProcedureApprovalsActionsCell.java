package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.SvgGenerationService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.ajax.ConfirmAjaxCallDecorator;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
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

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private LotoReportService lotoReportService;

    @SpringBean
    private SvgGenerationService svgGenerationService;


    private static final Logger logger = Logger.getLogger(ProcedureApprovalsActionsCell.class);


    public ProcedureApprovalsActionsCell(String id, IModel<ProcedureDefinition> procedureDefinitionModel, final ProcedureListPanel procedureListPanel) {
        super(id);

        final AjaxLink<Void> deleteLink;
        final BookmarkablePageLink<Void> startApprovalLink;
        final BookmarkablePageLink<Void> editLink;

        final ProcedureDefinition procedureDefinition = procedureDefinitionModel.getObject();

        FIDModalWindow modal;
        add(modal = new FIDModalWindow("modal", getDefaultModel(), 600, 50));
        modal.setTitle(new FIDLabelModel("message.downloadbeinggenerated"));

        editLink = new BookmarkablePageLink<Void>("editLink", ProcedureDefinitionPage.class, PageParametersBuilder.id(procedureDefinitionModel.getObject().getId())) {
            public boolean isVisible() {
                if ( isAuthor(procedureDefinition) || ( isApprover(procedureDefinition) && isRejected(procedureDefinition) ) )
                    return FieldIDSession.get().getUserSecurityGuard().isAllowedEditEvent();
                else
                    return false;
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

            @Override
            public boolean isVisible() {
                if ((isAuthor(procedureDefinition) || isApprover(procedureDefinition)) ) {
                    return FieldIDSession.get().getUserSecurityGuard().isAllowedDeleteProcedure();
                }
                else
                    return false;
            }

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                return new ConfirmAjaxCallDecorator(new FIDLabelModel("message.confirm_delete_procedure").getObject());
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

        add(actionsList);

        //Add the print buttons
        add(new LotoPrintoutOptionsContainer("optionsContainer2", procedureDefinition, modal) {
            @Override
            public boolean isVisible() {
                if (FieldIDSession.get().getSessionUser().isReadOnlyUser())
                    return false;
                else
                    return super.isVisible() && !isRejected(procedureDefinition) && actionsList.isVisible();
            }
        });
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

        if (procedureDefinition.getPublishedState().equals(PublishedState.REJECTED ) && procedureDefinition.getRejectedDate() != null) {
            return true;
        }
        return false;
    }

}

