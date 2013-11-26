package com.n4systems.model;

import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="thing_events")
@PrimaryKeyJoinColumn(name="id")
public class ThingEvent extends Event<ThingEventType> {

    @ManyToOne
    @JoinColumn(name="thing_event_type_id")
    private ThingEventType type;

    private ProofTestInfo proofTestInfo;

    @Override
    public ThingEventType getType() {
        return type;
    }

    @Override
    public void setType(ThingEventType type) {
        this.type = type;
    }

    public ThingEvent copyDataFrom(ThingEvent event) {
        SecurityEnhanced x = (Event)this;
        setAsset(event.getAsset());
        setType(event.getType());
        setTenant(event.getTenant());
        setCreated(event.getCreated());
        setCreatedBy(event.getCreatedBy());
        setModifiedBy(event.getModifiedBy());
        setModified(event.getModified());

        return this;
    }

    @AllowSafetyNetworkAccess
    public ProofTestInfo getProofTestInfo() {
        return proofTestInfo;
    }

    public void setProofTestInfo(ProofTestInfo proofTestInfo) {
        this.proofTestInfo = proofTestInfo;
    }

    @Override
    public ThingEvent enhance(SecurityLevel level) {
        ThingEvent enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
        enhanced.setBook(enhance(getBook(), level));
        enhanced.setPerformedBy(enhance(getPerformedBy(), level));
        enhanced.setType(enhance(getType(), level));
        enhanced.setAsset(enhance(getAsset(), level));
        enhanced.setOwner(enhance(getOwner(), level));

        List<SubEvent> enhancedSubEvents = new ArrayList<SubEvent>();
        for (SubEvent subEvent : getSubEvents()) {
            enhancedSubEvents.add(subEvent.enhance(level));
        }
        enhanced.setSubEvents(enhancedSubEvents);

        return enhanced;
    }

}
