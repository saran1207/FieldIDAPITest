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
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by rrana on 2014-11-06.
 */
public class PreviouslyPublishedProcedureActionsCell extends Panel {

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


    private static final Logger logger = Logger.getLogger(PreviouslyPublishedProcedureActionsCell.class);

    public PreviouslyPublishedProcedureActionsCell(String id, final IModel<ProcedureDefinition> proDef, final ProcedureListPanel procedureListPanel) {
        super(id);

        Boolean hasAuthorEditProcedures = FieldIDSession.get().getUserSecurityGuard().isAllowedAuthorEditProcedure();

        final ProcedureDefinition procedureDefinition = proDef.getObject();

        FIDModalWindow modal;
        add(modal = new FIDModalWindow("modal", getDefaultModel(), 600, 50));
        modal.setTitle(new FIDLabelModel("message.downloadbeinggenerated"));

        Link reviseLink = new Link("reviseLink") {
            @Override
            public void onClick() {
                ProcedureDefinition copiedDefinition = procedureDefinitionService.cloneProcedureDefinition(procedureDefinition);
                copiedDefinition.setPublishedState(PublishedState.DRAFT);
                setResponsePage(new ProcedureDefinitionPage(Model.of(copiedDefinition), true));
            }
        };

        reviseLink.setVisible(hasAuthorEditProcedures);
        reviseLink.add(new Label("label", new FIDLabelModel("label.restore")));
        reviseLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.restore"), TipsyBehavior.Gravity.E));

        add(reviseLink);

        //Add the print buttons
        add(new LotoPrintoutOptionsContainer("optionsContainer2", procedureDefinition, modal));
    }
}