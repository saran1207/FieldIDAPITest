package com.n4systems.model.event;

import com.n4systems.model.Event;
import com.n4systems.model.EventGroup;
import com.n4systems.persistence.savers.Saver;

import javax.persistence.EntityManager;

public class SimpleEventSaver extends Saver<Event> {

    @Override
    public void save(EntityManager em, Event entity) {
        if (entity.getGroup() == null) {
            entity.setGroup(new EventGroup());
            em.persist(entity.getGroup());
        }
        super.save(em, entity);
    }
}
