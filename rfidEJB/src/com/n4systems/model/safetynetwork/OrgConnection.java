package com.n4systems.model.safetynetwork;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.security.SecurityDefiner;

@Entity
@Table(name="org_connections", uniqueConstraints = @UniqueConstraint(columnNames = {"vendor_id", "customer_id"}))
public class OrgConnection extends AbstractEntity implements UnsecuredEntity {
	private static final long serialVersionUID = 1L;

	public static SecurityDefiner createSecurityDefiner() {
		throw new SecurityException("createSecurityDefiner called on an UnsecuredEntity");
	}
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="vendor_id", nullable=false)
	private BaseOrg vendor;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="customer_id", nullable=false)	
	private BaseOrg customer;

	public InternalOrg getVendor() {
		return (InternalOrg)vendor;
	}

	public void setVendor(InternalOrg vendor) {
		this.vendor = vendor;
	}

	public InternalOrg getCustomer() {
		return (InternalOrg)customer;
	}

	public void setCustomer(InternalOrg customer) {
		this.customer = customer;
	}
	
	public InternalOrg getByConnectionType(OrgConnectionType type) {
		if (type.isCustomer()) {
			return getCustomer();
		} else {
			return getVendor();
		}
	}
}
