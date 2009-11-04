package com.n4systems.model.orgs;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;

@Entity
@Table(name = "org_customer")
@PrimaryKeyJoinColumn(name="org_id")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class CustomerOrg extends ExternalOrg {
	private static final long serialVersionUID = 1L;
	
	/*
	 * Note, this references BaseOrg rather then InternalOrg since 
	 * InternalOrg is not an Entity (it's a MappedSuperclass).  Hibernate 
	 * has no idea what to do in that situation, so we set it directly to the BaseOrg
	 * and force casing to InternalOrg via getter/setter
	 */
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name="parent_id")
	private BaseOrg parent;
	
	public CustomerOrg() {}
	
	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public PrimaryOrg getPrimaryOrg() {
		return parent.getPrimaryOrg();
	}
	
	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public InternalOrg getInternalOrg() {
		return getParent();
	}
	
	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public SecondaryOrg getSecondaryOrg() {
		return parent.getSecondaryOrg();
	}
	
	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public CustomerOrg getCustomerOrg() {
		return this;
	}
	
	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public DivisionOrg getDivisionOrg() {
		return null;
	}
	
	@Override
	public String getFilterPath() {
		return CUSTOMER_ID_FILTER_PATH;
	}
	
	@Override
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public InternalOrg getParent() {
		// Note the type is downcast to InternalOrg (should always be the case because of forced setter)
		return (InternalOrg)parent;
	}

	// Note we force the type to be InternalOrg
	public void setParent(InternalOrg parentOrg) {
		this.parent = parentOrg;
	}
	
	@Deprecated
	@NetworkAccessLevel(SecurityLevel.DIRECT)
	public String getCustomerId() {
		return getCode();
	}
	
	public CustomerOrg enhance(SecurityLevel level) {
		CustomerOrg enhanced = EntitySecurityEnhancer.enhanceEntity(this, level);
		enhanced.setParent((InternalOrg)enhance(parent, level));
		enhanced.setContact(enhance(getContact(),level));
		return enhanced;
	}
	
	@Override
	public String getDisplayName() {
		return getCustomerOrg().getName() + " (" + getInternalOrg().getName() + ")";		
	}
}
