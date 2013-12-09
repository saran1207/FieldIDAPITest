package com.n4systems.model;


import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="action_event_types")
@PrimaryKeyJoinColumn(name="id")
public class ActionEventType extends EventType {

    @Override
    public boolean isActionEventType() {
        return true;
    }
}
