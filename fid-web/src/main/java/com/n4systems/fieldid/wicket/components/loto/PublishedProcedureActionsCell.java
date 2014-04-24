package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.loto.DraftListAllPage;
import com.n4systems.fieldid.wicket.pages.loto.PreviouslyPublishedListAllPage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPrintPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
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
    private ProcedureService procedureService;

    public PublishedProcedureActionsCell(String id, final IModel<ProcedureDefinition> proDef, final ProcedureListPanel procedureListPanel) {
        super(id);

        final ProcedureDefinition procedureDefinition = proDef.getObject();

        Link printLink;
        printLink = new Link("viewLink") {
            @Override public void onClick() {
                setResponsePage(new ProcedureDefinitionPrintPage(PageParametersBuilder.id(procedureDefinition.getId())));
            }
        };
        printLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.view_print"), TipsyBehavior.Gravity.W));
        printLink.add(new AttributeAppender("class", "tipsy-tooltip").setSeparator(" "));
        PopupSettings popupSettings = new PopupSettings("popupWindow", PopupSettings.SCROLLBARS).setWidth(1000).setTop(1);
        printLink.setPopupSettings(popupSettings);
        add(printLink);

        WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer");

        Link reviseLink = new Link("reviseLink") {
            @Override
            public void onClick() {
                ProcedureDefinition copiedDefinition = procedureDefinitionService.cloneProcedureDefinition(procedureDefinition);
                copiedDefinition.setPublishedState(PublishedState.DRAFT);
                setResponsePage(new ProcedureDefinitionPage(Model.of(copiedDefinition)));
            }
        };
        reviseLink.setVisible(procedureDefinitionService.hasPublishedProcedureDefinition(procedureDefinition.getAsset())
              && (procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED) || procedureDefinition.getPublishedState().equals(PublishedState.PREVIOUSLY_PUBLISHED)));

        reviseLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.revise"), TipsyBehavior.Gravity.N));

        if(procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED))
            reviseLink.add(new Label("label", new FIDLabelModel("label.revise")));
        else
            reviseLink.add(new Label("label", new FIDLabelModel("label.restore")));

        optionsContainer.add(reviseLink);

        Link copyLink = new Link("copyLink") {
            @Override
            public void onClick() {
                ProcedureDefinition copiedDefinition = procedureDefinitionService.cloneProcedureDefinitionForCopy(procedureDefinition);
                copiedDefinition.setPublishedState(PublishedState.DRAFT);
                setResponsePage(new ProcedureDefinitionPage(Model.of(copiedDefinition)));
            }
        };
        copyLink.setVisible(procedureDefinitionService.hasPublishedProcedureDefinition(procedureDefinition.getAsset())
                && procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED));
        copyLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.copy"), TipsyBehavior.Gravity.N));
        copyLink.add(new Label("label", new FIDLabelModel("label.copy")));
        optionsContainer.add(copyLink);

        boolean showUnpublished = procedureDefinitionService.isApprovalRequired() ? procedureDefinitionService.canCurrentUserApprove() : true;

        AjaxLink unpublishLink = new AjaxLink<Void>("unpublishLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                if(procedureService.hasOpenProcedure(procedureDefinition)) {
                    error(new FIDLabelModel("error.unpublish").getObject());
                    target.add(procedureListPanel.getErrorFeedbackPanel());
                } else {
                    procedureDefinitionService.unpublishProcedureDefinition(procedureDefinition);
                    info(new FIDLabelModel("message.unpublish", procedureDefinition.getProcedureCode()).getObject());
                    target.add(procedureListPanel);
                    target.add(((FieldIDTemplatePage) getPage()).getTopFeedbackPanel());
                }
            }
        };
        unpublishLink.setVisible(showUnpublished  && procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED));
        optionsContainer.add(unpublishLink);

        Link draftLink = new Link("draftLink") {
            @Override
            public void onClick() {
                ProcedureDefinition publishedDef = procedureDefinitionService.getPublishedProcedureDefinition(procedureDefinition.getAsset(), procedureDefinition.getFamilyId());
                setResponsePage(new DraftListAllPage(publishedDef.getProcedureCode(), publishedDef.getAsset()));
            }
        };
        draftLink.setVisible(procedureDefinitionService.hasPublishedProcedureDefinition(procedureDefinition.getAsset())
                && procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED));


        optionsContainer.add(draftLink);


        Link previouslyPublishedLink = new Link("previouslyPublishedLink") {
            @Override
            public void onClick() {
                ProcedureDefinition publishedDef = procedureDefinitionService.getPublishedProcedureDefinition(procedureDefinition.getAsset(), procedureDefinition.getFamilyId());
                setResponsePage(new PreviouslyPublishedListAllPage(publishedDef.getProcedureCode(), publishedDef.getAsset()));
            }
        };
        previouslyPublishedLink.setVisible(procedureDefinitionService.hasPublishedProcedureDefinition(procedureDefinition.getAsset())
                && procedureDefinition.getPublishedState().equals(PublishedState.PUBLISHED));


        optionsContainer.add(previouslyPublishedLink);


        BookmarkablePageLink<Void> editLink = new BookmarkablePageLink<Void>("editLink", ProcedureDefinitionPage.class, PageParametersBuilder.id(procedureDefinition.getId())) {
        };
        editLink.setVisible(isAuthor(procedureDefinition) && procedureDefinition.getPublishedState().equals(PublishedState.DRAFT));

        optionsContainer.add(editLink);



        AjaxLink<Void> deleteLink = new AjaxLink<Void>("deleteLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {

                try {
                    procedureDefinitionService.deleteProcedureDefinition(procedureDefinition);
                    info(new FIDLabelModel("message.procedure_definitions.delete").getObject());
                    target.add(procedureListPanel);
                    target.add(((FieldIDTemplatePage) getPage()).getTopFeedbackPanel());
                } catch (Exception e) {
                    error(new FIDLabelModel("error.delete_procedure_definition").getObject());
                    target.add(procedureListPanel.getErrorFeedbackPanel());
                }
            }
        };

        deleteLink.setVisible(isAuthor(procedureDefinition) && procedureDefinition.getPublishedState().equals(PublishedState.DRAFT));

        optionsContainer.add(deleteLink);

        add(optionsContainer);

    }

    private boolean isAuthor(ProcedureDefinition procedureDefinition) {

        if (procedureDefinitionService.isCurrentUserAuthor(procedureDefinition)) {
            return true;
        }

        return false;

    }
}