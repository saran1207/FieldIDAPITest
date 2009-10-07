package com.n4systems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.util.StringUtils;

@Entity
@Table(name = "inspectionssub")
@PrimaryKeyJoinColumn(name="inspection_id")
public class SubInspection extends AbstractInspection implements SecurityEnhanced<SubInspection> {
	private static final long serialVersionUID = 1L;
	
	@Column( length = 255 )
	private String name;

	@NetworkAccessLevel(SecurityLevel.MANY_AWAY)
	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public SubInspection() {}

	public SubInspection(Tenant tenant) {
		super(tenant);
	}

	@Override
    public String toString() {
	    return "SubInspection: " + getName() + StringUtils.indent("\n" + super.toString(), 1);
    }

	public SubInspection enhance(SecurityLevel level) {
		SubInspection enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setType(enhance(getType(), level));
		enhanced.setProduct(enhance(getProduct(), level));
		return enhanced;
	}
	
}
