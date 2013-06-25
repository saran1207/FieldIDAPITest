package com.n4systems.fieldid.wicket.pages.identify.components;

import com.n4systems.fieldid.wicket.components.DateLabel;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Event;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

import static ch.lambdaj.Lambda.on;

public class EventSchedulesPanel extends Panel {

    public EventSchedulesPanel(String id, final SchedulePicker schedulePicker, final IModel<List<Event>> schedulesModel) {
        super(id);
        setOutputMarkupPlaceholderTag(true);

        final WebMarkupContainer currentSchedulesContainer = new WebMarkupContainer("currentSchedulesContainer");
        currentSchedulesContainer.setOutputMarkupPlaceholderTag(true);

        final WebMarkupContainer noSchedulesIndicator = new WebMarkupContainer("noSchedulesIndicator") {
            { setOutputMarkupId(true); }
            @Override
            public boolean isVisible() {
                return schedulesModel.getObject() != null && schedulesModel.getObject().isEmpty();
            }
        };
        add(noSchedulesIndicator);

        add(currentSchedulesContainer);
        currentSchedulesContainer.add(new ListView<Event>("currentSchedules", schedulesModel) {
            @Override
            protected void populateItem(final ListItem<Event> item) {
                item.add(new DateLabel("dueDate", ProxyModel.of(item.getModel(), on(Event.class).getDueDate())));
                item.add(new Label("eventTypeName", ProxyModel.of(item.getModel(), on(Event.class).getType().getName())));
                item.add(new AjaxLink("removeLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        schedulesModel.getObject().remove(item.getIndex());
                        target.add(currentSchedulesContainer, noSchedulesIndicator);
                    }
                });
            }
        });

        Form addScheduleForm = new Form("addScheduleForm");
        add(addScheduleForm);
        addScheduleForm.add(new AjaxButton("addScheduleButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                schedulePicker.show(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });

    }

}
