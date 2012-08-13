package com.n4systems.model.notification;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.Event;

import javax.persistence.*;

@Entity
@Table(name = "assignee_notifications")
public class AssigneeNotification extends BaseEntity {

    @OneToOne
    @JoinColumn(name="event_id")
    private Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}
