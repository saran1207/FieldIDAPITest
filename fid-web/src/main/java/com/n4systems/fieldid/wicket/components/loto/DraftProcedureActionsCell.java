package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by rrana on 2014-11-06.
 */
public class DraftProcedureActionsCell extends Panel {

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    @SpringBean
    private ProcedureService procedureService;

    public DraftProcedureActionsCell(String id, final IModel<ProcedureDefinition> proDef, final ProcedureListPanel procedureListPanel) {
        super(id);

        final ProcedureDefinition procedureDefinition = proDef.getObject();

        BookmarkablePageLink<Void> editLink = new BookmarkablePageLink<Void>("editLink", ProcedureDefinitionPage.class, PageParametersBuilder.id(procedureDefinition.getId())) {
        };
        editLink.setVisible(procedureDefinition.getPublishedState().equals(PublishedState.DRAFT));

        add(editLink);

        WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer");

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

        optionsContainer.setVisible(deleteLink.isVisible());

        //Add the print buttons
        WebMarkupContainer optionsContainer2 = new WebMarkupContainer("optionsContainer2");

        Link shortLink = new Link("shortLink") {
            @Override
            public void onClick() {

            }
        };

        shortLink.add(new Label("label", new FIDLabelModel("label.short_form")));
        optionsContainer2.add(shortLink);

        Link longLink = new Link("longLink") {
            @Override
            public void onClick() {

            }
        };
        longLink.add(new Label("label", new FIDLabelModel("label.long_form")));
        optionsContainer2.add(longLink);
        add(optionsContainer2);

    }

    private boolean isAuthor(ProcedureDefinition procedureDefinition) {

        if (procedureDefinitionService.isCurrentUserAuthor(procedureDefinition)) {
            return true;
        }

        return false;

    }
}