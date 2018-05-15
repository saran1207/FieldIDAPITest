package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.service.procedure.SvgGenerationService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.loto.DraftListAllPage;
import com.n4systems.fieldid.wicket.pages.loto.PreviouslyPublishedListAllPage;
import com.n4systems.fieldid.wicket.pages.loto.RecurringLotoSchedulesPage;
import com.n4systems.fieldid.wicket.pages.loto.UnpublishProcedureDefinitionPage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by rrana on 2014-04-11.
 */

public class PublishedProcedureActionsCell extends Panel {

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private ProcedureService procedureService;

    @SpringBean
    private LotoReportService lotoReportService;

    @SpringBean
    private SvgGenerationService svgGenerationService;

    private final ProcedureDefinition procedureDefinition;

    private static final Logger logger = Logger.getLogger(PublishedProcedureActionsCell.class);

    public PublishedProcedureActionsCell(String id, final IModel<ProcedureDefinition> proDef, final ProcedureListPanel procedureListPanel) {
        super(id);

        procedureDefinition = proDef.getObject();

        Boolean hasAuthorEditProcedures = FieldIDSession.get().getUserSecurityGuard().isAllowedAuthorEditProcedure();
        Boolean hasMaintainLotoSchedule = FieldIDSession.get().getUserSecurityGuard().isAllowedMaintainLotoSchedule();
        Boolean hasProcedureAudit = FieldIDSession.get().getUserSecurityGuard().isAllowedProcedureAudit();
        Boolean hasPublishedProcedureDefinition = procedureDefinitionService.hasPublishedProcedureDefinition(procedureDefinition.getAsset());

        FIDModalWindow modal;
        add(modal = new FIDModalWindow("modal", getDefaultModel(), 600, 200));
        modal.setTitle(new FIDLabelModel("message.downloadbeinggenerated"));

        WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer");

        Link reviseLink = new Link("reviseLink") {
            @Override
            public void onClick() {
                ProcedureDefinition copiedDefinition = procedureDefinitionService.cloneProcedureDefinition(procedureDefinition);
                copiedDefinition.setPublishedState(PublishedState.DRAFT);
                setResponsePage(new ProcedureDefinitionPage(Model.of(copiedDefinition), true));
            }
        };

        reviseLink.setVisible(hasPublishedProcedureDefinition);

        reviseLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.revise"), TipsyBehavior.Gravity.E));

        reviseLink.add(new Label("label", new FIDLabelModel("label.revise")));
        reviseLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.revise"), TipsyBehavior.Gravity.E));

        optionsContainer.add(reviseLink);

        Link copyLink = new Link("copyLink") {
            @Override
            public void onClick() {
                ProcedureDefinition copiedDefinition = procedureDefinitionService.cloneProcedureDefinitionForCopy(procedureDefinition);
                copiedDefinition.setPublishedState(PublishedState.DRAFT);
                setResponsePage(new ProcedureDefinitionPage(Model.of(copiedDefinition), true));
            }
        };
        copyLink.setVisible(hasPublishedProcedureDefinition);
        copyLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.copy"), TipsyBehavior.Gravity.E));
        copyLink.add(new Label("label", new FIDLabelModel("label.copy")));
        optionsContainer.add(copyLink);

        boolean showUnpublished = !procedureDefinitionService.isApprovalRequired() || procedureDefinitionService.canCurrentUserApprove();

        AjaxLink unpublishLink = new AjaxLink<Void>("unpublishLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                if(procedureService.hasOpenProcedure(procedureDefinition)) {
                    //Instead of causing an error, we want to forward the user to a new page where they can see the
                    //open procedures for that definition.  We also want to ensure all recurring schedules are removed,
                    //which will only be done after the user confirms this is what they want to do.
                    error(new FIDLabelModel("error.unpublish").getObject());
                    target.add(procedureListPanel.getErrorFeedbackPanel());
                } else {
                    if(procedureDefinitionService.hasRecurringSchedule(procedureDefinition)) {
                        //There's a recurring schedule... the user needs to be notified of this before we proceed.  It's
                        //possible they made a mistake.
                        setResponsePage(new UnpublishProcedureDefinitionPage(
                                        PageParametersBuilder.param("procedureDefinitionId",
                                                procedureDefinition.getId()))
                        );

                    } else {
                        //Otherwise, there's no recurring schedule to warn the user about.  We can just clean it up
                        //right here...
                        procedureDefinitionService.unpublishProcedureDefinition(procedureDefinition);
                        info(new FIDLabelModel("message.unpublish", procedureDefinition.getProcedureCode()).getObject());
                        target.add(procedureListPanel);
                        target.add(((FieldIDTemplatePage) getPage()).getTopFeedbackPanel());
                    }
                }
            }
        };
        unpublishLink.setVisible(showUnpublished);
        optionsContainer.add(unpublishLink);

        Link draftLink = new Link("draftLink") {
            @Override
            public void onClick() {
                ProcedureDefinition publishedDef = procedureDefinitionService.getPublishedProcedureDefinition(procedureDefinition.getAsset(), procedureDefinition.getFamilyId());
                setResponsePage(new DraftListAllPage(publishedDef.getProcedureCode(), publishedDef.getAsset(), true, false));
            }
        };
        draftLink.setVisible(hasPublishedProcedureDefinition);


        optionsContainer.add(draftLink);


        Link previouslyPublishedLink = new Link("previouslyPublishedLink") {
            @Override
            public void onClick() {
                ProcedureDefinition publishedDef = procedureDefinitionService.getPublishedProcedureDefinition(procedureDefinition.getAsset(), procedureDefinition.getFamilyId());
                setResponsePage(new PreviouslyPublishedListAllPage(publishedDef.getProcedureCode(), publishedDef.getAsset(), true, false));
            }
        };
        previouslyPublishedLink.setVisible(hasPublishedProcedureDefinition
                && procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED));


        optionsContainer.add(previouslyPublishedLink);

        Link recurringSchedulesLink = getRecurringScheduleLink("recurringSchedulesLink", hasPublishedProcedureDefinition);
        recurringSchedulesLink.setVisible(hasMaintainLotoSchedule || hasProcedureAudit);

        optionsContainer.add(recurringSchedulesLink);


        add(optionsContainer);

        optionsContainer.setVisible(hasAuthorEditProcedures);

        Link recurringSchedulesLink2 = getRecurringScheduleLink("recurringSchedulesLink2", hasPublishedProcedureDefinition);

        recurringSchedulesLink2.setVisible(!hasAuthorEditProcedures && (hasMaintainLotoSchedule || hasProcedureAudit));

        add(recurringSchedulesLink2);
        //Add the print buttons
        add(new LotoPrintoutOptionsContainer("printOptionsContainer", procedureDefinition, modal));
    }

    private Link getRecurringScheduleLink(String id, Boolean hasPublishedProcedureDefinition) {
        Link recurringSchedulesLink = new Link(id) {
            @Override
            public void onClick() {
                setResponsePage(RecurringLotoSchedulesPage.class, PageParametersBuilder.uniqueId(procedureDefinition.getAsset().getId()));
            }
        };
        recurringSchedulesLink.setVisible(hasPublishedProcedureDefinition
                && procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED));
        return recurringSchedulesLink;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        /* Add the hover text for the print button. We can't do this using the TipsyBehavior since the button is in
           a Wicket enclosure. */
        response.renderJavaScript(
                "$(document).ready(function(event){$('.lotoHoverTextButton').tipsy({gravity: 'e', fade:true, delayIn:250, live:true})});",
                "LOTO_PRINT_TIPSY_JS");
        super.renderHead(response);
    }
}