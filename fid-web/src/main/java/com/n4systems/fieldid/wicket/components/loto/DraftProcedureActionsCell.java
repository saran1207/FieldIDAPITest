package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.service.procedure.SvgGenerationService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
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

        Boolean hasAuthorEditProcedures = FieldIDSession.get().getUserSecurityGuard().isAllowedAuthorEditProcedure();
        Boolean hasDeleteProcedures = FieldIDSession.get().getUserSecurityGuard().isAllowedDeleteProcedure();

        FIDModalWindow modal;
        add(modal = new FIDModalWindow("modal", getDefaultModel(), 700, 150));
        modal.setTitle(new FIDLabelModel("message.downloadbeinggenerated"));

        PageParameters params = new PageParameters();
        params.add("id", procedureDefinition.getId());
        params.add("isCopyOrRevise", "true");


        WebMarkupContainer primaryActionLink;

        if (hasAuthorEditProcedures) {
            //edit
            primaryActionLink = new BookmarkablePageLink<>("primaryActionLink", ProcedureDefinitionPage.class, params);
            primaryActionLink.add(new FlatLabel("label", new FIDLabelModel("label.edit")));
        } else if (!hasAuthorEditProcedures && hasDeleteProcedures) {
            //delete only
            primaryActionLink = getDeleteLink("primaryActionLink", procedureListPanel, procedureDefinition);
            primaryActionLink.add(new FlatLabel("label", new FIDLabelModel("label.delete")));
            primaryActionLink.setVisible(procedureDefinitionService.isCurrentUserAuthor(procedureDefinition));
        } else {
            //no actions
            primaryActionLink = new WebMarkupContainer("primaryActionLink");
            primaryActionLink.setVisible(false);
        }
        add(primaryActionLink);

        AjaxLink<Void> deleteLink = getDeleteLink("deleteLink", procedureListPanel, procedureDefinition);

        deleteLink.setVisible(procedureDefinitionService.isCurrentUserAuthor(procedureDefinition) &&  hasAuthorEditProcedures && hasDeleteProcedures);

        add(deleteLink);

        //Add the print buttons
        add(new LotoPrintoutOptionsContainer("printOptionsContainer", procedureDefinition, modal) {
            @Override
            public boolean isVisible() {
                if (FieldIDSession.get().getSessionUser().isReadOnlyUser())
                   return false;
                else
                    return super.isVisible();
            }
        });
    }

    private AjaxLink<Void> getDeleteLink(String id, final ProcedureListPanel procedureListPanel, final ProcedureDefinition procedureDefinition) {
        return new AjaxLink<Void>(id) {

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
    }
}