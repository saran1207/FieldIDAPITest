package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.event.*;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;
import com.n4systems.persistence.utils.PostFetcher;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class ThingEventSummaryPage extends EventSummaryPage {

    @SpringBean
    private EventService eventService;
    private IModel<ThingEvent> eventModel;
    private IModel<Asset> assetModel;


    public ThingEventSummaryPage(PageParameters parameters) {
        super(parameters);

        eventModel = Model.of(loadExistingEvent());
        assetModel = new PropertyModel<Asset>(eventModel, "asset");
        eventSummaryType = EventSummaryType.THING_EVENT;
    }

    @Override
    protected Panel getDetailsPanel(String id) {
        return new EventDetailsPanel(id, eventModel);
    }

    @Override
    protected Panel getEventFormPanel(String id) {
        return new EventFormViewPanel(id, ThingEvent.class, new PropertyModel<List<AbstractEvent.SectionResults>>(eventModel, "sectionResults"));
    }

    @Override
    protected Panel getEventResultPanel(String id) {
        return new EventResultPanel(id, eventModel);
    }

    @Override
    protected Panel getPostEventInfoPanel(String id) {
        return new PostEventDetailsPanel(id, eventModel);
    }

    @Override
    protected Panel getEventAttachmentsPanel(String id) {
        return new EventAttachmentsPanel(id, eventModel);
    }

    @Override
    protected Panel getEventLocationPanel(String id) {
        return new EventLocationPanel(id, eventModel);
    }

    @Override
    protected Panel getProofTestPanel(String id) {
        return new ProofTestPanel(id, eventModel);
    }

    @Override
    protected Event getEvent() {
        return eventModel.getObject();
    }

    private ThingEvent loadExistingEvent() {
        ThingEvent existingEvent = eventService.lookupExistingEvent(ThingEvent.class, uniqueId);
        PostFetcher.postFetchFields(existingEvent, Event.ALL_FIELD_PATHS);
        PostFetcher.postFetchFields(existingEvent, Event.THING_TYPE_PATHS);
        return existingEvent;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new ThingEventHeaderPanel(labelId, assetModel, true);
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("label.summary")).page(AssetSummaryPage.class).params(PageParametersBuilder.uniqueId(assetModel.getObject().getId())).build(),
                aNavItem().label(new FIDLabelModel("label.event_summary")).page(ThingEventSummaryPage.class).params(PageParametersBuilder.id(uniqueId)).build()
        ));
    }


}
