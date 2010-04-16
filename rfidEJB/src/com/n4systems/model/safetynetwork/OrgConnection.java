package com.n4systems.model.safetynetwork;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.n4systems.model.Tenant;
import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.tenant.HasSetupDataTenant;



@Entity
@Table(name="org_connections", uniqueConstraints = @UniqueConstraint(columnNames = {"vendor_id", "customer_id"}))
public class OrgConnection extends AbstractEntity implements UnsecuredEntity, HasSetupDataTenant {
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

	public OrgConnection() {}
	
	public OrgConnection(PrimaryOrg vendor, PrimaryOrg customer) {
		guard(vendor, customer);
		this.vendor = vendor;
		this.customer = customer;
	}
	
	private void guard(PrimaryOrg vendor, PrimaryOrg customer) {
		if (vendor.equals(customer)) {
			throw new IllegalArgumentException("The connecting orgs are the same.  they must be 2 different orgs. from 2 different tenants.");
		}
	}

	public PrimaryOrg getVendor() {
		return (PrimaryOrg)vendor;
	}


	public PrimaryOrg getCustomer() {
		return (PrimaryOrg)customer;
	}

	
	public PrimaryOrg getByConnectionType(OrgConnectionType type) {
		if (type.isCustomer()) {
			return getCustomer();
		} else {
			return getVendor();
		}
	}
	
	public Tenant getSetupDataTenant() {
		return customer.getTenant();
	}
}
