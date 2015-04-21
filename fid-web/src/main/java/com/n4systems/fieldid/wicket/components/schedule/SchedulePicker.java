package com.n4systems.fieldid.wicket.components.schedule;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.Project;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

import java.util.List;

public class SchedulePicker<T extends Event> extends DialogModalWindow {

    private SchedulePickerPanel schedulePickerPanel;

    public SchedulePicker(String id, IModel<T> scheduleModel, IModel<List<? extends EventType>> eventTypeOptions) {
        this(id, scheduleModel, eventTypeOptions, new ListModel<Project>(Lists.<Project>newArrayList()));
    }

    public SchedulePicker(String id, IModel<T> scheduleModel, IModel<List<? extends EventType>> eventTypeOptions, IModel<List<Project>> jobsOptions) {
        super(id);

        setContent(schedulePickerPanel = new SchedulePickerPanel<T>(getContentId(), scheduleModel, eventTypeOptions, jobsOptions) {
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                close(target);
                SchedulePicker.this.onPickComplete(target);
            }
        });
        setInitialWidth(400);
        setInitialHeight(450);
    }

    protected void onPickComplete(AjaxRequestTarget target) { }

    public void setSaveButtonLabel(IModel<String> saveButtonLabel) {
        schedulePickerPanel.setSaveButtonLabel(saveButtonLabel);
    }

}
