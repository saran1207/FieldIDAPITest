package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.service.procedure.SvgGenerationService;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by rrana on 2014-11-06.
 */
public class DraftProcedureActionsCell extends Panel {

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    @SpringBean
    private ProcedureService procedureService;


    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private LotoReportService lotoReportService;

    @SpringBean
    private SvgGenerationService svgGenerationService;


    private static final Logger logger = Logger.getLogger(DraftProcedureActionsCell.class);

    public DraftProcedureActionsCell(String id, final IModel<ProcedureDefinition> proDef, final ProcedureListPanel procedureListPanel) {
        super(id);

        final ProcedureDefinition procedureDefinition = proDef.getObject();

        FIDModalWindow modal;
        add(modal = new FIDModalWindow("modal", getDefaultModel(), 700, 150));
        modal.setTitle(new FIDLabelModel("message.downloadbeinggenerated"));

        PageParameters params = new PageParameters();
        params.add("id", procedureDefinition.getId());
        params.add("isCopyOrRevise", "true");

        BookmarkablePageLink<Void> editLink = new BookmarkablePageLink<Void>("editLink", ProcedureDefinitionPage.class, params) {
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

        deleteLink.setVisible(procedureDefinitionService.isCurrentUserAuthor(procedureDefinition) && procedureDefinition.getPublishedState().equals(PublishedState.DRAFT));

        optionsContainer.add(deleteLink);

        add(optionsContainer);

        optionsContainer.setVisible(deleteLink.isVisible());

        //Add the print buttons
        add(new LotoPrintoutOptionsContainer("optionsContainer2", procedureDefinition, modal));
    }
}