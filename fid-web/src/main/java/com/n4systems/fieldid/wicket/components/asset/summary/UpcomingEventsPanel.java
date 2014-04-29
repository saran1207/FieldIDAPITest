package com.n4systems.fieldid.wicket.components.asset.summary;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.wicket.components.TimeAgoLabel;
import com.n4systems.fieldid.wicket.components.asset.events.table.EditLotoScheduleLink;
import com.n4systems.fieldid.wicket.components.asset.events.table.EventStateIcon;
import com.n4systems.fieldid.wicket.components.asset.events.table.OpenActionsCell;
import com.n4systems.fieldid.wicket.components.asset.events.table.ProcedureStateIcon;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.*;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.services.date.DateService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class UpcomingEventsPanel extends Panel {

    private @SpringBean
    DateService dateService;

    private @SpringBean
    ProcedureService procedureService;

    private Asset asset;

    public UpcomingEventsPanel(String id, IModel<List<ThingEvent>> model, final Asset asset) {
        super(id, model);
        this.asset = asset;

        add(new ListView<ThingEvent>("upcomingEventsList", model) {
            @Override
            protected void populateItem(ListItem<ThingEvent> item) {
                ThingEvent schedule = item.getModelObject();

                item.add(new EventStateIcon("scheduleIcon", item.getModel()).setRenderBodyOnly(true));

                item.add(new Label("upcomingEventType", schedule.getType().getName()));
                
                DayDisplayModel upcomingEventDate = new DayDisplayModel(Model.of(schedule.getDueDate())).includeTime();
                
                if (isPastDue(schedule)) {
                    TimeAgoLabel timeAgoField = new TimeAgoLabel("upcomingEventDate",Model.of(schedule.getDueDate()),dateService.getUserTimeZone());
                    item.add(timeAgoField);
                } else if(dateService.getDaysFromToday(schedule.getDueDate()).equals(0L)) {
                    item.add(new Label("upcomingEventDate", new FIDLabelModel("label.today")));
                } else {
                    item.add(new Label("upcomingEventDate", new FIDLabelModel("label.in_x_days", dateService.getDaysFromToday(schedule.getDueDate()), upcomingEventDate.getObject())));
                }
                
                item.add(new Label("onDate", new FIDLabelModel("label.on_date", upcomingEventDate.getObject())));
                
                item.add(new OpenActionsCell("openActions", Model.of(schedule), UpcomingEventsPanel.this));
            }
        });

        add(new ListView<Procedure>("upcomingLoto", new ProcedureModel()) {
            @Override
            protected void populateItem(ListItem<Procedure> item) {
                if(item.getModelObject() != null) {
                    item.add(new ProcedureStateIcon("scheduleIcon", item.getModel()));

                    item.add(new Label("upcomingEventType", item.getModelObject().getType().getProcedureCode()));

                    DayDisplayModel upcomingProcedureDate = new DayDisplayModel(new PropertyModel<Date>(item.getModel(), "dueDate")).includeTime();

                    if (isPastDue(item.getModelObject())) {
                        TimeAgoLabel timeAgoField = new TimeAgoLabel("upcomingLotoDate", new PropertyModel<Date>(item.getModel(), "dueDate"),dateService.getUserTimeZone());
                        item.add(timeAgoField);
                    } else if(dateService.getDaysFromToday(item.getModelObject().getDueDate()).equals(0L)) {
                        item.add(new Label("upcomingLotoDate", new FIDLabelModel("label.today")));
                    } else {
                        item.add(new Label("upcomingLotoDate", new FIDLabelModel("label.in_x_days", dateService.getDaysFromToday(item.getModelObject().getDueDate()), upcomingProcedureDate.getObject())));
                    }

                    item.add(new Label("onDate", new FIDLabelModel("label.on_date", upcomingProcedureDate.getObject())));
                } else {
                    item.add(new WebMarkupContainer("scheduleIcon"));
                    item.add(new Label("upcomingLotoDate"));
                    item.add(new Label("onDate"));
                }
                item.setVisible(item.getModelObject() != null);
                item.add(new EditLotoScheduleLink("editLoto", item.getModel()) {
                    @Override
                    public void onProcedureScheduleUpdated(AjaxRequestTarget target) {
                        UpcomingEventsPanel.this.detach();
                        target.add(UpcomingEventsPanel.this);
                    }
                });
            }

        });
    }

    private boolean isPastDue(Event schedule) {
        return schedule.getWorkflowState() == WorkflowState.OPEN && dateService.isPastDue(schedule.getDueDate());
    }

    private boolean isPastDue(Procedure schedule) {
        return schedule.getWorkflowState() == ProcedureWorkflowState.OPEN && dateService.isPastDue(schedule.getDueDate());
    }

    class ProcedureModel extends LoadableDetachableModel<List<Procedure>> {

        @Override
        protected List<Procedure> load() {
            return Lists.newArrayList(procedureService.getOpenProcedure(asset));
        }

    }

}
