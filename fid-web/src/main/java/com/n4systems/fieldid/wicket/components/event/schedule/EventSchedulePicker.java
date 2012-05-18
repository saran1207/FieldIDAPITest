package com.n4systems.fieldid.wicket.components.event.schedule;

import com.n4systems.fieldid.service.schedule.ScheduleService;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
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

public class EventSchedulePicker extends FormComponentPanel<EventSchedule> {

    @SpringBean
    private ScheduleService scheduleService;

    private IModel<Asset> asset;

    public EventSchedulePicker(String id, IModel<EventSchedule> eventSchedule, IModel<Asset> asset) {
        super(id, eventSchedule);
        this.asset = asset;

        add(new DropDownChoice<EventSchedule>("eventSchedule", eventSchedule, createSchedulesListModel(asset), createScheduleChoiceRenderer()).setNullValid(true));
    }
    
    private IModel<List<EventSchedule>> createSchedulesListModel(final IModel<Asset> assetModel) {
        return new LoadableDetachableModel<List<EventSchedule>>() {
            @Override
            protected List<EventSchedule> load() {
                List<EventSchedule> pickableSchedulesWithNewAndNone = new ArrayList<EventSchedule>();
                List<EventSchedule> incompleteSchedules = scheduleService.findIncompleteSchedulesForAsset(assetModel.getObject());
                pickableSchedulesWithNewAndNone.add(new EventSchedule());
                pickableSchedulesWithNewAndNone.addAll(incompleteSchedules);
                return pickableSchedulesWithNewAndNone;
            }
        };
    }
    
    private IChoiceRenderer<EventSchedule> createScheduleChoiceRenderer() {
        return new IChoiceRenderer<EventSchedule>() {
            @Override
            public Object getDisplayValue(EventSchedule object) {
                if (object.getId() == null) {
                    return new FIDLabelModel("label.createanewscheduled").getObject();
                }
                return new DayDisplayModel(new Model<Date>(object.getNextDate())).getObject();
            }

            @Override
            public String getIdValue(EventSchedule object, int index) {
                if (object.getId() == null) {
                    return "0";
                }
                return object.getId().toString();
            }
        };
    }
}
