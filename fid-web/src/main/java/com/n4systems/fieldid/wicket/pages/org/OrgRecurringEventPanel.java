package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.model.EnumLabelModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.time.RecurrenceTimeModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.OrgRecurringEvent;
import com.n4systems.model.Recurrence;
import com.n4systems.model.RecurrenceType;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static ch.lambdaj.Lambda.on;

public class OrgRecurringEventPanel extends Panel {

    private @SpringBean PlaceService placeService;

    private WebMarkupContainer container;
    private ListView recurringEventList;

    private ModalWindow recurrenceModalWindow;
    private final IModel<? extends BaseOrg> model;

    public OrgRecurringEventPanel(String id, IModel<? extends BaseOrg> model) {
        super(id,model);
        this.model = model;

        add(container = new WebMarkupContainer("container"));
        container.setOutputMarkupId(true);

        container.add(recurringEventList = new ListView<OrgRecurringEvent>("recurringEvents", getRecurringEvents()) {
            @Override protected void populateItem(final ListItem<OrgRecurringEvent> item) {
                final OrgRecurringEvent event = (OrgRecurringEvent) item.getDefaultModelObject();
                item.add(new Label("eventType", ProxyModel.of(event, on(OrgRecurringEvent.class).getEventType().getName())));
                item.add(new Label("recurrence", new EnumLabelModel(event.getRecurrence().getType())));
                item.add(new Label("time", new RecurrenceTimeModel(ProxyModel.of(event, on(OrgRecurringEvent.class).getRecurrence().getTimes())," @ %s")));
                item.add(new AjaxLink("remove") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        // TODO : Remove it from place.
                        placeService.removeRecurringEvent(event);
                        target.add(container);
                    }
                });
            }
        });

        add(recurrenceModalWindow = new DialogModalWindow("addDialog").setInitialWidth(480));
        recurrenceModalWindow.setContent(getRecurrenceForm(recurrenceModalWindow.getContentId()));

        add(new AjaxLink<Void>("add") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                recurrenceModalWindow.show(target);
            }
        });

        WebMarkupContainer blankSlate = new WebMarkupContainer("blankSlate");
        add(blankSlate);
        blankSlate.add(new Label("message", new FIDLabelModel("message.org_recurring.blank_slate", model.getObject().getDisplayName())));

        container.setVisible(true);//!org.getEventTypes().isEmpty();
        blankSlate.setVisible(false);  // org.getEventTypes().isEmpty()
    }


    private Component getRecurrenceForm(String id) {
        return new OrgRecurringEventsFormPanel(id, model);
    }

    private LoadableDetachableModel<List<OrgRecurringEvent>> getRecurringEvents() {
        // TEST DATA
        final OrgRecurringEvent recur1;
        final OrgRecurringEvent recur2;
        // TEST DATA

        recur1 = new OrgRecurringEvent();
        recur2 = new OrgRecurringEvent();
        recur1.setEventType(EventTypeBuilder.anEventType().named("visual").build());
        recur2.setEventType(EventTypeBuilder.anEventType().named("maintenance").build());
        recur1.setRecurrence(new Recurrence(RecurrenceType.ANNUALLY));
        recur2.setRecurrence(new Recurrence(RecurrenceType.WEEKLY_FRIDAY));

        return new LoadableDetachableModel<List<OrgRecurringEvent>>() {
            @Override protected List<OrgRecurringEvent> load() {
                return Lists.newArrayList(recur1,recur2);
            }
        };
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
    }

}
