package com.n4systems.model.orgs;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.util.SecurityFilter;

@Entity
@Table(name = "org_base")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseOrg extends EntityWithTenant implements Listable<Long>, Saveable, FilteredEntity, Comparable<BaseOrg> {
	private static final long serialVersionUID = 1L;

	@Column(name="name", nullable = false, length = 255)
	private String name;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "addressinfo_id")
	private AddressInfo addressInfo;
	
	public BaseOrg() {}
	
	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets("tenant.id", null, null, null, null);
	}

	public String getDisplayName() {
		return name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String displayName) {
		this.name = (displayName != null) ? displayName.trim() : null;
	}

	public AddressInfo getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}

	@Override
	public String toString() {
		return String.format("%s (%d) [%s]", name, id, getTenant().toString());
	}
	
	public int compareTo(BaseOrg other) {
		return name.compareToIgnoreCase(other.getName());
	}
	
	abstract public PrimaryOrg getPrimaryOrg();
}
