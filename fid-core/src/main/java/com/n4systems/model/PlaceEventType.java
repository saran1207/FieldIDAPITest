package com.n4systems.model;

import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="place_event_types")
@PrimaryKeyJoinColumn(name="id")
public class PlaceEventType extends EventType implements SecurityEnhanced<PlaceEventType> {

    @Override
    public PlaceEventType enhance(SecurityLevel level) {
        return EntitySecurityEnhancer.enhanceEntity(this, level);
    }

}
