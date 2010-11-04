package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.util.StringUtils;

@Entity
@Table(name = "subevents")
@PrimaryKeyJoinColumn(name="inspection_id")
public class SubEvent extends AbstractEvent implements SecurityEnhanced<SubEvent> {
	private static final long serialVersionUID = 1L;
	public static final String[] ALL_FIELD_PATHS = { "modifiedBy.userID", "type.sections", "type.supportedProofTests", "type.infoFieldNames", "attachments", "results", "asset", "asset.infoOptions", "infoOptionMap"};
	
	@Column( length = 255 )
	private String name;

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
	
}
