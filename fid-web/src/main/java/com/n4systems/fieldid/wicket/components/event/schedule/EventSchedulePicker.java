package com.n4systems.fieldid.wicket.components.event.schedule;

import com.n4systems.fieldid.service.schedule.ScheduleService;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventSchedulePicker extends FormComponentPanel<Event> {

    @SpringBean
    private ScheduleService scheduleService;

    private IModel<Asset> asset;

    public EventSchedulePicker(String id, IModel<Event> eventSchedule, IModel<Asset> asset) {
        super(id, eventSchedule);
        this.asset = asset;

        DropDownChoice<Event> schedulePicker = new DropDownChoice<Event>("eventSchedule", eventSchedule, createSchedulesListModel(asset), createScheduleChoiceRenderer());
        schedulePicker.setNullValid(true);
        schedulePicker.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });
        add(schedulePicker);
    }
    
    private IModel<List<Event>> createSchedulesListModel(final IModel<Asset> assetModel) {
        return new LoadableDetachableModel<List<Event>>() {
            @Override
            protected List<Event> load() {
                List<Event> pickableSchedulesWithNewAndNone = new ArrayList<Event>();
                List<Event> incompleteSchedules = scheduleService.findIncompleteSchedulesForAsset(assetModel.getObject());
                pickableSchedulesWithNewAndNone.add(new Event());
                pickableSchedulesWithNewAndNone.addAll(incompleteSchedules);
                return pickableSchedulesWithNewAndNone;
            }
        };
    }
    
    private IChoiceRenderer<Event> createScheduleChoiceRenderer() {
        return new IChoiceRenderer<Event>() {
            @Override
            public Object getDisplayValue(Event object) {
                if (object.getId() == null) {
                    return new FIDLabelModel("label.createanewscheduled").getObject();
                }
                return new DayDisplayModel(new Model<Date>(object.getNextDate())).getObject();
            }

            @Override
            public String getIdValue(Event object, int index) {
                if (object.getId() == null) {
                    return "0";
                }
                return object.getId().toString();
            }
        };
    }

    protected void onScheduleChanged(AjaxRequestTarget target) { }
}
