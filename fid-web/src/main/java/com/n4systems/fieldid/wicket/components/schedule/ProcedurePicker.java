package com.n4systems.fieldid.wicket.components.schedule;

import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

public class ProcedurePicker extends DialogModalWindow {

    private ProcedurePickerPanel procedurePickerPanel;

    public ProcedurePicker(String id, IModel<Procedure> scheduleModel) {
        super(id);

        setContent(procedurePickerPanel = new ProcedurePickerPanel(getContentId(), scheduleModel) {
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                close(target);
                ProcedurePicker.this.onPickComplete(target);
                updateVisibility();
            }
        });
        //setInitialWidth(370);
        setInitialWidth(400);
        setInitialHeight(400);

    }

    protected void onPickComplete(AjaxRequestTarget target) { }

    public void setSaveButtonLabel(IModel<String> saveButtonLabel) {
        procedurePickerPanel.setSaveButtonLabel(saveButtonLabel);
    }
}
