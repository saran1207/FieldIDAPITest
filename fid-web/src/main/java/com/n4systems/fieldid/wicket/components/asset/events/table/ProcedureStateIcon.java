package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.services.date.DateService;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ProcedureStateIcon extends Panel {

    private @SpringBean
    DateService dateService;

    public ProcedureStateIcon(String id, IModel<Procedure> procedureModel) {
        super(id);

        Procedure procedure = procedureModel.getObject();
        ProcedureWorkflowState state = procedure.getWorkflowState();
        ContextImage image;
        if(state.equals(ProcedureWorkflowState.OPEN)) {
            if(isPastDue(procedure)) {
                add(image = new ContextImage("resultIcon", "images/loto-icon-overdue.png"));
                image.add(new AttributeAppender("title", new FIDLabelModel("label.loto_open_assigned_overdue", procedure.getAssignee().getDisplayName())));
            } else {
                add(image = new ContextImage("resultIcon", "images/loto-icon.png"));
                image.add(new AttributeAppender("title", new FIDLabelModel("label.assignee_is", procedure.getAssignedUserOrGroup().getDisplayName())));
                image.add(new AttributeAppender("class", "scheduleIconMargin").setSeparator(" "));
            }
        }
    }

    private boolean isPastDue(Procedure procedure) {
        return procedure.getWorkflowState() == ProcedureWorkflowState.OPEN && dateService.isPastDue(procedure.getDueDate());
    }
}
