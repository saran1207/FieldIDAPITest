package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.fieldid.service.event.PlaceEventCreationService;
import com.n4systems.fieldid.wicket.components.event.prooftest.ProofTestEditPanel;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForPlaceModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.org.PlaceSummaryPage;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public abstract class PlaceEventPage extends EventPage<PlaceEvent> {

    @SpringBean protected PlaceEventCreationService eventCreationService;

    @Override
    protected PlaceEvent createNewOpenEvent() {
        PlaceEvent placeEvent = new PlaceEvent();
        placeEvent.setPlace(event.getObject().getPlace());
        return placeEvent;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        ownerSection.setVisible(false);
        jobsContainer.setVisible(false);
        eventBookContainer.setVisible(false);
    }

    @Override
    protected boolean supportsProofTests() {
        return false;
    }

    @Override
    protected Component createCancelLink(String cancelLink) {
        return new BookmarkablePageLink<Void>(cancelLink, PlaceSummaryPage.class, PageParametersBuilder.id(event.getObject().getPlace().getId()));
    }

    @Override
    protected ProofTestEditPanel createProofTestEditPanel(String id) {
        return null;
    }

    @Override
    protected boolean targetAlreadyArchived(PlaceEvent event) {
        return event.getPlace().isArchived();
    }

    @Override
    protected void retireEvent(PlaceEvent event) {
    }

    @Override
    protected void gotoSummaryPage(PlaceEvent event) {
        setResponsePage(PlaceSummaryPage.class, PageParametersBuilder.id(event.getPlace().getId()));
    }

    @Override
    protected SchedulePicker<PlaceEvent> createSchedulePicker() {
        return new SchedulePicker<PlaceEvent>("schedulePicker", new PropertyModel<PlaceEvent>(PlaceEventPage.this, "scheduleToAdd"), new EventTypesForPlaceModel(new PropertyModel<BaseOrg>(event, "place"))){
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                onSchedulePickComplete(target);
            }
        };
    }

    @Override
    protected Component createTargetDetailsPanel(IModel<PlaceEvent> model) {
        return new WebMarkupContainer("targetDetailsPanel");
    }

    @Override
    protected Component createPostEventPanel(IModel<PlaceEvent> event) {
        return new WebMarkupContainer("postEventPanel");
    }

    protected List<EventScheduleBundle<BaseOrg>> createEventScheduleBundles() {
        List<EventScheduleBundle<BaseOrg>> scheduleBundles = new ArrayList<EventScheduleBundle<BaseOrg>>();

        for (PlaceEvent sched : schedules) {
            EventScheduleBundle<BaseOrg> bundle = new EventScheduleBundle<BaseOrg>(sched.getPlace(), sched.getType(), sched.getProject(), sched.getDueDate());
            scheduleBundles.add(bundle);
        }

        return scheduleBundles;
    }
}
