package com.n4systems.model;


import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="action_event_types")
@PrimaryKeyJoinColumn(name="id")
public class ActionEventType extends EventType<ActionEventType> {

    @Override
    public boolean isActionEventType() {
        return true;
    }

    @Override
    public ActionEventType enhance(SecurityLevel level) {
        return EntitySecurityEnhancer.enhanceEntity(this, level);
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        setActionType(true);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        setActionType(true);
    }

}
