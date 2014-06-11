package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPrintPage;
import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by rrana on 2014-06-11.
 */
public class ProcedureAuditActionsCell extends Panel {

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    @SpringBean
    private ProcedureService procedureService;

    public ProcedureAuditActionsCell(String id, final IModel<Procedure> proDef, final ProcedureAuditListPanel procedureListPanel) {
        super(id);

        final Procedure procedureDefinition = proDef.getObject();

        Link printLink;
        printLink = new Link("viewLink") {
            @Override public void onClick() {
                setResponsePage(new ProcedureDefinitionPrintPage(PageParametersBuilder.id(procedureDefinition.getType().getId())));
            }
        };
        printLink.add(new TipsyBehavior(new FIDLabelModel("message.procedure_definitions.view_print"), TipsyBehavior.Gravity.E));
        printLink.add(new AttributeAppender("class", "tipsy-tooltip").setSeparator(" "));
        PopupSettings popupSettings = new PopupSettings("popupWindow", PopupSettings.SCROLLBARS).setWidth(1000).setTop(1);
        printLink.setPopupSettings(popupSettings);
        add(printLink);

    }

}