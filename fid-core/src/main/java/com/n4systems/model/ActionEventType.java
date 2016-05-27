package com.n4systems.model;


import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name="action_event_types")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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

    //XXX: Legacy Post fetch code craps out if there's no supportedProofTests method on an event type....
    //When events are not viewable in struts this should no longer be necessary...
    @Transient
    public boolean isSupportedProofTests() {
        return false;
    }

}
