package com.n4systems.fieldid.service.org;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventType;
import com.n4systems.model.OrgRecurringEvent;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

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

    public List<EventType> getEventTypesFor(BaseOrg org) {
        QueryBuilder<EventType> query = createUserSecurityBuilder(EventType.class);
        query.addOrder("name");
        query.addSimpleWhere("group.action", false);
        return persistenceService.findAll(query);
    }

    public void removeRecurringEvent(OrgRecurringEvent event) {
        throw new UnsupportedOperationException("removing recurring events not implemented yet");
    }

    public void addRecurringEvent() {
        throw new UnsupportedOperationException("adding recurring events not implemented yet");
    }
}
