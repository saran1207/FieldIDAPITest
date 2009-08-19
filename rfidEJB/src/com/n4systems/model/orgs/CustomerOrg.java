package com.n4systems.model.orgs;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "org_customer")
@PrimaryKeyJoinColumn(name="org_id")
public class CustomerOrg extends ExternalOrg {
	private static final long serialVersionUID = 1L;
	
	/*
	 * Note, this references BaseOrg rather then InternalOrg since 
	 * InternalOrg is not an Entity (it's a MappedSuperclass).  Hibernate 
	 * has no idea what to do in that situation, so we set it directly to the BaseOrg
	 * and force casing to InternalOrg via getter/setter
	 */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name="parent_id")
	private BaseOrg parent;
	
	public CustomerOrg() {}
	
	@Override
	public PrimaryOrg getPrimaryOrg() {
		return parent.getPrimaryOrg();
	}
	
	// Note the type is downcast to InternalOrg (should always be the case because of forced setter)
	public InternalOrg getParent() {
		return (InternalOrg)parent;
	}

	// Note we force the type to be InternalOrg
	public void setParent(InternalOrg parentOrg) {
		this.parent = parentOrg;
	}

}
