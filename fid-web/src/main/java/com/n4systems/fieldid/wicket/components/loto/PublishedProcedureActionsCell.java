package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPrintPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
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

    private @SpringBean
    ProcedureDefinitionService procedureDefinitionService;

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

        WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer"){
        };

        optionsContainer.setVisible(!procedureDefinition.getPublishedState().equals(PublishedState.PREVIOUSLY_PUBLISHED));

        Link copyLink;
        copyLink = new Link("copyProcedureDefLink") {
            @Override
            public void onClick() {
                ProcedureDefinition publishedDef = procedureDefinitionService.getPublishedProcedureDefinition(proDef.getObject().getAsset(), proDef.getObject().getFamilyId());
                ProcedureDefinition copiedDefinition = procedureDefinitionService.cloneProcedureDefinition(publishedDef);
                copiedDefinition.setPublishedState(PublishedState.DRAFT);
                setResponsePage(new ProcedureDefinitionPage(Model.of(copiedDefinition)));
            }
        };
        copyLink.setVisible(procedureDefinitionService.hasPublishedProcedureDefinition(proDef.getObject().getAsset())
              && proDef.getObject().getPublishedState().equals(PublishedState.PUBLISHED));

        copyLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.revise"), TipsyBehavior.Gravity.N));

        optionsContainer.add(copyLink);

        BookmarkablePageLink<Void> editLink = new BookmarkablePageLink<Void>("editLink", ProcedureDefinitionPage.class, PageParametersBuilder.id(procedureDefinition.getId())) {
        };
        editLink.setVisible(isAuthor(procedureDefinition) && procedureDefinition.getPublishedState().equals(PublishedState.DRAFT));

        optionsContainer.add(editLink);




        AjaxLink<Void> deleteLink = new AjaxLink<Void>("deleteLink") {

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

        };

        deleteLink.setVisible(procedureDefinition.getPublishedState().equals(PublishedState.DRAFT));

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