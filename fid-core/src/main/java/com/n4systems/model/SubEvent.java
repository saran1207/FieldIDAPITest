package com.n4systems.model;

import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.util.StringUtils;

import javax.persistence.*;

@Entity
@Table(name = "subevents")
@PrimaryKeyJoinColumn(name="event_id")
public class SubEvent extends AbstractEvent<ThingEventType> implements SecurityEnhanced<SubEvent> {
	private static final long serialVersionUID = 1L;
	public static final String[] ALL_FIELD_PATHS = { "modifiedBy.userID", "eventForm.sections", "type.supportedProofTests", "type.infoFieldNames", "attachments", "results", "results.criteriaImages", "asset", "asset.infoOptions", "infoOptionMap"};
	
	@Column( length = 255 )
	private String name;

    @ManyToOne(fetch=FetchType.EAGER, optional = false)
    @JoinColumn(name="thing_event_type_id")
    private ThingEventType type;

	@AllowSafetyNetworkAccess
	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public SubEvent() {}

	public SubEvent(Tenant tenant) {
		super(tenant);
	}

	@Override
    public String toString() {
	    return "SubEvent: " + getName() + StringUtils.indent("\n" + super.toString(), 1);
    }

	public SubEvent enhance(SecurityLevel level) {
		SubEvent enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setType(enhance(getType(), level));
		enhanced.setAsset(enhance(getAsset(), level));
		return enhanced;
	}

    public ThingEventType getType() {
        return type;
    }

    public void setType(ThingEventType type) {
        this.type = type;
    }
}
