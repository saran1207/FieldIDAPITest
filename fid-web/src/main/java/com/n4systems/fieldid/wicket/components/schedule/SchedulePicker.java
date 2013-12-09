package com.n4systems.fieldid.wicket.components.schedule;

import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.model.*;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import java.util.List;

public class SchedulePicker extends DialogModalWindow {

    private SchedulePickerPanel schedulePickerPanel;

    public SchedulePicker(String id, IModel<ThingEvent> scheduleModel, IModel<List<? extends EventType>> eventTypeOptions, IModel<List<Project>> jobsOptions) {
        super(id);

        setContent(schedulePickerPanel = new SchedulePickerPanel(getContentId(), scheduleModel, eventTypeOptions, jobsOptions) {
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                close(target);
                SchedulePicker.this.onPickComplete(target);
            }
        });
        setInitialWidth(370);
        setInitialHeight(450);
    }

    protected void onPickComplete(AjaxRequestTarget target) { }

    public void setSaveButtonLabel(IModel<String> saveButtonLabel) {
        schedulePickerPanel.setSaveButtonLabel(saveButtonLabel);
    }

}
