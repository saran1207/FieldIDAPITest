package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.wicket.components.schedule.ProcedurePicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.ProcedureWorkflowState;
import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class EditLotoScheduleLink extends Panel {

    @SpringBean
    private ProcedureService procedureService;

    private ProcedurePicker procedurePicker;

    public EditLotoScheduleLink(String id, final IModel<Procedure> procedureModel) {
        super(id, procedureModel);

        if(procedureModel.getObject() != null) {
            procedurePicker = new ProcedurePicker("procedurePicker", procedureModel) {
                @Override
                protected void onPickComplete(AjaxRequestTarget target) {
                    procedureService.updateSchedule(procedureModel.getObject());
                    onProcedureScheduleUpdated(target);
                }
            };
            procedurePicker.setSaveButtonLabel(new FIDLabelModel("label.save"));
            add(procedurePicker);

            add(new AjaxLink<Void>("editLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    procedurePicker.show(target);
                }
            });

            WebMarkupContainer deleteContainer;

            add(deleteContainer = new WebMarkupContainer("deleteContainer"));
            deleteContainer.setVisible(procedureModel.getObject().getWorkflowState().equals(ProcedureWorkflowState.OPEN));
            deleteContainer.add(new AjaxLink<Void>("deleteLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    procedureService.deleteSchedule(procedureModel.getObject());
                    info(new FIDLabelModel("message.loto_deleted").getObject());
                    onProcedureScheduleUpdated(target);
                }
            }.setVisible(procedureModel.getObject().getWorkflowState().equals(ProcedureWorkflowState.OPEN)));

        } else {
            add(new WebMarkupContainer("procedurePicker"));
            add(new WebMarkupContainer("editLink"));
            add(new WebMarkupContainer("deleteLink"));
        }

    }

    public void onProcedureScheduleUpdated(AjaxRequestTarget target) {}
}
