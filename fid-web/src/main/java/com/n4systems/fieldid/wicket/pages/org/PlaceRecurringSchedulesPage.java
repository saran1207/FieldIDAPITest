package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.fieldid.wicket.components.DisplayRecurrenceTimeModel;
import com.n4systems.fieldid.wicket.components.assettype.RecurrenceFormPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.BackToPlaceSubHeader;
import com.n4systems.fieldid.wicket.components.org.RecurringPlaceEventsFormPanel;
import com.n4systems.fieldid.wicket.model.EnumLabelModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.util.NullCoverterModel;
import com.n4systems.model.RecurrenceTime;
import com.n4systems.model.RecurringPlaceEvent;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;
import java.util.Set;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class PlaceRecurringSchedulesPage extends PlacePage{

    @SpringBean
    private RecurringScheduleService recurringScheduleService;

    private WebMarkupContainer scheduleList;
    private WebMarkupContainer blankSlate;
    private WebMarkupContainer noEventTypes;
    private ListView<RecurringPlaceEvent> listView;
    private ModalWindow recurrenceModalWindow;

    public PlaceRecurringSchedulesPage(PageParameters params) {
        super(params);

        add(scheduleList = new WebMarkupContainer("scheduleList"));
        scheduleList.setOutputMarkupPlaceholderTag(true);

        add(blankSlate = new WebMarkupContainer("blankSlate"));
        blankSlate.setOutputMarkupPlaceholderTag(true);

        add(noEventTypes = new WebMarkupContainer("noEventTypes"));
        noEventTypes.setOutputMarkupPlaceholderTag(true);
        noEventTypes.add(new BookmarkablePageLink<PlaceEventTypesPage>("eventTypesLink", PlaceEventTypesPage.class, PageParametersBuilder.id(getOrg().getId())));

        setVisibility();

        add(recurrenceModalWindow = new DialogModalWindow("addRecurrence").setInitialWidth(480));
        recurrenceModalWindow.setContent(getRecurrenceForm());

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

    protected void refreshContent(AjaxRequestTarget target) {
        listView.detach();
        setVisibility();
        target.add(scheduleList, blankSlate);
    }

    private void setVisibility() {
        boolean hasRecurrences = recurringScheduleService.countRecurringPlaceEvents(getOrg()) > 0;
        scheduleList.setVisible(hasRecurrences);
        blankSlate.setVisible(!hasRecurrences && !hasEventTypes());
        noEventTypes.setVisible(!hasRecurrences && hasEventTypes());
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.recurring_schedules_for", orgModel.getObject().getDisplayName()));
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new ActionGroup(actionGroupId);
    }

    @Override
    protected List<NavigationItem> createBreadCrumbs(BaseOrg org) {
        List<NavigationItem> navItems = super.createBreadCrumbs(org);
        navItems.add(aNavItem().label(new FIDLabelModel("label.recurring_schedules")).page(getClass()).params(PageParametersBuilder.id(org.getId())).build());
        return navItems;
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId).setVisible(false));
    }

    @Override
    protected Component createSubHeader(String subHeaderId) {
        return new BackToPlaceSubHeader(subHeaderId, orgModel);
    }

    private LoadableDetachableModel<List<RecurringPlaceEvent>> getRecurringEvents() {
        return new LoadableDetachableModel<List<RecurringPlaceEvent>>() {

            @Override
            protected List<RecurringPlaceEvent> load() {
                return recurringScheduleService.getRecurringPlaceEvents(getOrg());
            }
        };
    }

    private class ActionGroup extends Fragment {
        public ActionGroup(String id) {
            super(id, "addRecurrenceActionGroup", PlaceRecurringSchedulesPage.this);
            add(new AjaxLink("addRecurrenceLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    recurrenceModalWindow.show(target);
                }

                @Override
                protected boolean isLinkEnabled() {
                    return !hasEventTypes();
                }

                @Override
                protected void disableLink(ComponentTag tag) {
                    super.disableLink(tag);
                    tag.put("class", tag.getAttribute("class") + " disabled");
                }
            }.setBeforeDisabledLink("").setAfterDisabledLink(""));
        }
    }

    private boolean hasEventTypes() {
        return getOrg().getEventTypes().isEmpty();
    }
}
