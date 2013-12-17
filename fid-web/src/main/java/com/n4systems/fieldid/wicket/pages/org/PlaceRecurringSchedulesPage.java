package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.fieldid.wicket.components.DisplayRecurrenceTimeModel;
import com.n4systems.fieldid.wicket.components.assettype.RecurrenceFormPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.org.RecurringPlaceEventsFormPanel;
import com.n4systems.fieldid.wicket.model.EnumLabelModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.util.NullCoverterModel;
import com.n4systems.model.RecurrenceTime;
import com.n4systems.model.RecurringPlaceEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Set;

public class PlaceRecurringSchedulesPage extends PlacePage{

    @SpringBean
    private RecurringScheduleService recurringScheduleService;

    private WebMarkupContainer scheduleList;
    private ListView<RecurringPlaceEvent> listView;
    private ModalWindow recurrenceModalWindow;

    public PlaceRecurringSchedulesPage(PageParameters params) {
        super(params);

        add(scheduleList = new WebMarkupContainer("scheduleList"));
        scheduleList.setOutputMarkupId(true);

        add(recurrenceModalWindow = new DialogModalWindow("addRecurrence").setInitialWidth(480));
        recurrenceModalWindow.setContent(getRecurrenceForm());

        add(new BookmarkablePageLink<PlaceSummaryPage>("backToPlaceLink", PlaceSummaryPage.class, PageParametersBuilder.id(getOrg().getId()))
                .add(new Label("backToLabel", new PropertyModel<String>(orgModel, "name"))));

        add(new AjaxLink("addRecurrenceLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                recurrenceModalWindow.show(target);
            }
        });

        scheduleList.add(listView = new ListView<RecurringPlaceEvent>("recurringEvent", getRecurringEvents()) {

            @Override
            protected void populateItem(final ListItem<RecurringPlaceEvent> item) {
                final RecurringPlaceEvent event = item.getModelObject();

                item.add(new Label("eventType", new PropertyModel<String>(item.getDefaultModelObject(), "eventType.name")));
                item.add(new Label("recurrence", new EnumLabelModel(event.getRecurrence().getType())));
                item.add(new Label("org", new NullCoverterModel(new PropertyModel<String>(item.getDefaultModelObject(), "owner.name"), "---")));
                item.add(new Label("time", new DisplayRecurrenceTimeModel(new PropertyModel<Set<RecurrenceTime>>(item.getDefaultModelObject(), "recurrence.times"))));

                if (event.isAutoAssign()) {
                    item.add(new Label("autoAssign", new FIDLabelModel("label.auto_assign")));
                } else {
                    item.add(new Label("autoAssign", "---"));
                }

                item.add(new AjaxLink("remove") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        recurringScheduleService.purgeRecurringEvent(item.getModelObject());
                        refreshContent(target);
                    }
                });
            }
        });
    }

    @Override
    protected void refreshContent(AjaxRequestTarget target) {
        listView.detach();
        target.add(scheduleList);
    }

    private LoadableDetachableModel<List<RecurringPlaceEvent>> getRecurringEvents() {
        return new LoadableDetachableModel<List<RecurringPlaceEvent>>() {

            @Override
            protected List<RecurringPlaceEvent> load() {
                return recurringScheduleService.getRecurringPlaceEvents(getOrg());
            }
        };
    }

    public RecurrenceFormPanel getRecurrenceForm() {
        return new RecurringPlaceEventsFormPanel(recurrenceModalWindow.getContentId(), orgModel) {
            @Override
            protected void onCreateRecurrence(AjaxRequestTarget target, RecurringEventsForm form) {
                super.onCreateRecurrence(target, form);
                recurrenceModalWindow.close(target);
                refreshContent(target);
            }
        };
    }
}
