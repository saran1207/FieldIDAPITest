package com.n4systems.fieldid.actions.event;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.EventType;
import com.n4systems.model.event.EventTypesByEventGroupIdLoader;

import java.util.List;

public class EventTypesInEventTypeGroupAction extends AbstractAction {

    private Long eventTypeGroupId;
    private List<EventType> eventTypes;

    public EventTypesInEventTypeGroupAction(PersistenceManager persistenceManager) {
        super(persistenceManager);
    }

    public String doList() {
        EventTypesByEventGroupIdLoader loader = getLoaderFactory().createEventTypesByGroupListLoader();

        loader.setEventTypeGroupId(eventTypeGroupId);

        eventTypes = loader.load();

        return SUCCESS;
    }

    public Long getEventTypeGroupId() {
        return eventTypeGroupId;
    }

    public void setEventTypeGroupId(Long eventTypeGroupId) {
        this.eventTypeGroupId = eventTypeGroupId;
    }

    public List<EventType> getEventTypes() {
        return eventTypes;
    }
}
