package com.n4systems.fieldid.wicket.pages.event;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.event.*;
import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.org.OrgViewPage;
import com.n4systems.fieldid.wicket.pages.org.PlaceSummaryPage;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Event;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.persistence.utils.PostFetcher;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class PlaceEventSummaryPage extends EventSummaryPage {

    @SpringBean
    private EventService eventService;
    private IModel<PlaceEvent> eventModel;
    private IModel<BaseOrg> placeModel;


    public PlaceEventSummaryPage(PageParameters parameters) {
        super(parameters);

        eventModel = Model.of(loadExistingEvent());
        placeModel = new PropertyModel<BaseOrg>(eventModel, "place");
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
        return new GoogleMap<Event>(id, eventModel.getObject());
    }

    @Override
    protected Panel getProofTestPanel(String id) {
        return new EmptyPanel(id);
    }


    protected PlaceEvent loadExistingEvent() {
        PlaceEvent existingEvent = eventService.lookupExistingEvent(PlaceEvent.class, uniqueId);
        PostFetcher.postFetchFields(existingEvent, Event.ALL_FIELD_PATHS);
        PostFetcher.postFetchFields(existingEvent, Event.PLACE_FIELD_PATHS);
        return existingEvent;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new PropertyModel<String>(placeModel, "name"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("label.summary")).page(PlaceSummaryPage.class).params(PageParametersBuilder.id(placeModel.getObject().getId())).build(),
                aNavItem().label(new FIDLabelModel("label.event")).page(PlaceEventSummaryPage.class).params(PageParametersBuilder.id(uniqueId)).build()
        ));
    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        BaseOrg org = placeModel.getObject();
        List<NavigationItem> navItems = createBreadCrumbs(org);
        add(new BreadCrumbBar(breadCrumbBarId, navItems.toArray(new NavigationItem[0])));
    }

    protected List<NavigationItem> createBreadCrumbs(BaseOrg org) {
        List<NavigationItem> navItems = Lists.newArrayList();

        navItems.add(aNavItem().label("label.places").page(OrgViewPage.class).build());

        if(org.getPrimaryOrg() != null) {
            navItems.add(aNavItem().label(new PropertyModel<String>(org.getPrimaryOrg(), "name")).page(PlaceSummaryPage.class).params(PageParametersBuilder.id(org.getPrimaryOrg().getId())).build());
        }
        if(org.getSecondaryOrg() != null) {
            navItems.add(aNavItem().label(new PropertyModel<String>(org.getSecondaryOrg(), "name")).page(PlaceSummaryPage.class).params(PageParametersBuilder.id(org.getSecondaryOrg().getId())).build());
        }
        if(org.getCustomerOrg() != null) {
            navItems.add(aNavItem().label(new PropertyModel<String>(org.getCustomerOrg(), "name")).page(PlaceSummaryPage.class).params(PageParametersBuilder.id(org.getCustomerOrg().getId())).build());
        }
        if(org.getDivisionOrg() != null) {
            navItems.add(aNavItem().label(new PropertyModel<String>(org.getDivisionOrg(), "name")).page(PlaceSummaryPage.class).params(PageParametersBuilder.id(org.getDivisionOrg().getId())).build());
        }

        navItems.add(aNavItem().label("label.event").page(PlaceEventSummaryPage.class).params(PageParametersBuilder.uniqueId(uniqueId)).build());

        return navItems;
    }


}
