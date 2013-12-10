package com.n4systems.fieldid.service.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.*;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class PlaceService extends FieldIdPersistenceService {

    /**
     * NOTE THAT ALL METHODS IN THIS SERVICE ARE JUST PLACEHOLDERS FOR 2013.8!!!!
     *
     * these need to be properly implemented and possibly moved into other services before Places feature is complete.
     * just putting stuff in here now to make short term merging easier.
     * (most likely you'll want to merge this and OrgService???)
     */

    @Deprecated //only for testing
    public List<PlaceEventType> getEventTypesFor(BaseOrg org) {
        QueryBuilder<PlaceEventType> query = createUserSecurityBuilder(PlaceEventType.class);
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

    public void removeRecurringEvent(OrgRecurringEvent event) {
        throw new UnsupportedOperationException("removing recurring events not implemented yet");
    }

    public void addRecurringEvent() {
        throw new UnsupportedOperationException("adding recurring events not implemented yet");
    }

    public List<? extends User> getUsersFor(BaseOrg org) {
        QueryBuilder<User> query = createUserSecurityBuilder(User.class);
        query.addSimpleWhere("owner",org);
        return persistenceService.findAll(query);
    }

    public List<PlaceEvent> getEventsFor(BaseOrg org) {
        return getEventsFor(org, null, false, new ArrayList<WorkflowState>());
    }

    public List<PlaceEvent> getEventsFor(BaseOrg org, String order, boolean ascending, List<WorkflowState> workflowStates) {
        QueryBuilder<PlaceEvent> query = createUserSecurityBuilder(PlaceEvent.class);
        //query.addSimpleWhere("place",org);
        if (order!=null) {
            query.setOrder(order,ascending);
        }
        // TODO : add workflow state stuff here...


        return persistenceService.findAll(query);
//
//
//        // TODO : TEST DATA FOR NOW.
//        ThingEventType type = EventTypeBuilder.anEventType().named("visual").build();
//        User user = UserBuilder.anAdminUser().withFirstName("joe").withLastName("smith").withUserId("joesmith").build();
//        return Lists.newArrayList(
//                EventBuilder.anOpenEvent().ofType(type).performedOn(new Date()).scheduledFor(new Date()).withOwner(org).withPerformedBy(user).withGpsLocation(new GpsLocation(43.653489, -79.374796)).build(),
//                EventBuilder.aFailedEvent().ofType(type).performedOn(new Date()).scheduledFor(new Date()).withOwner(org).withPerformedBy(user).build(),
//                EventBuilder.anEvent().ofType(type).performedOn(new Date()).scheduledFor(new Date()).withOwner(org).withPerformedBy(user).build(),
//                EventBuilder.anOpenEvent().ofType(type).performedOn(new Date()).scheduledFor(new Date()).withOwner(org).withPerformedBy(user).build(),
//                EventBuilder.anOpenEvent().ofType(type).performedOn(new Date()).scheduledFor(new Date()).withOwner(org).withPerformedBy(user).build(),
//                EventBuilder.anEvent().ofType(type).performedOn(new Date()).scheduledFor(new Date()).withOwner(org).withPerformedBy(user).build(),
//                EventBuilder.aClosedEvent().ofType(type).performedOn(new Date()).scheduledFor(new Date()).withOwner(org).withPerformedBy(user).build(),
//                EventBuilder.aClosedEvent().ofType(type).performedOn(new Date()).scheduledFor(new Date()).withOwner(org).withPerformedBy(user).build(),
//                EventBuilder.anOpenEvent().ofType(type).performedOn(new Date()).scheduledFor(new Date()).withOwner(org).withPerformedBy(user).build(),
//                EventBuilder.aFailedEvent().ofType(type).performedOn(new Date()).scheduledFor(new Date()).withOwner(org).withPerformedBy(user).withGpsLocation(new GpsLocation(43.637325,-79.424005)).build(),
//                EventBuilder.anOpenEvent().ofType(type).performedOn(new Date()).scheduledFor(new Date()).withOwner(org).withPerformedBy(user).build(),
//                EventBuilder.anOpenEvent().ofType(type).performedOn(new Date()).scheduledFor(new Date()).withOwner(org).withPerformedBy(user).build(),
//                EventBuilder.aClosedEvent().ofType(type).performedOn(new Date()).scheduledFor(new Date()).withOwner(org).withPerformedBy(user).build()
//        );
    }

    public List<PlaceEvent> getOpenEventsFor(BaseOrg org) {
        QueryBuilder<PlaceEvent> query = createUserSecurityBuilder(PlaceEvent.class);
        query.addSimpleWhere("place",org);
        query.setOrder("dueDate",true);
        query.addSimpleWhere("workflowState",WorkflowState.OPEN);
        return persistenceService.findAll(query);
//        // TODO : TEST DATA FOR NOW.
//        ThingEventType type = EventTypeBuilder.anEventType().named("visual").build();
//        User user = UserBuilder.anAdminUser().withFirstName("joe").withLastName("smith").withUserId("joesmith").build();
//        List<ThingEvent> result = new ArrayList<ThingEvent>();
//        for (int i=1; i<40; i++) {
//            Date performed = new LocalDate().withDayOfYear(i*8).toDate();
//            Date due = new LocalDate().withYear(2012).withDayOfYear((int) (Math.random()*355)+1).toDate();
//            result.add(
//                EventBuilder.anOpenEvent()
//                        .ofType(type)
//                        .performedOn(performed)
//                        .scheduledFor(due)
//                        .withOwner(org)
//                        .withPerformedBy(user).build()
//                    );
//        }
//        return result;
    }

    public List<PlaceEvent> getOpenEventsFor(BaseOrg org, int days) {
        return getOpenEventsFor(org);
    }

    public int countEventsFor(BaseOrg org) {
        return getEventsFor(org, null, false, null).size();
    }

    public List<? extends Attachment> getAttachmentsFor(BaseOrg org) {
        Attachment attachment = new AssetAttachment();
        attachment.setComments("hello world");
        attachment.setFileName("/images/foo.png");
        return Lists.newArrayList(attachment);
    }

}
