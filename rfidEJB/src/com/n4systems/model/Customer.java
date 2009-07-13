package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.FilteredEntity;
import com.n4systems.util.SecurityFilter;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

// XXX - move into com.n4systems.model.customer package
@Entity
@Table(name = "customers")
public class Customer extends EntityWithTenant implements NamedEntity, Listable<Long>, FilteredEntity {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private String customerId;
	
	@Column(nullable=false)
	private String name;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private AddressInfo addressInfo = new AddressInfo();
	
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name="name", column = @Column(name="contactname")),
		@AttributeOverride(name="email", column = @Column(name="contactemail"))
	})
	private Contact contact = new Contact();

	public Customer() {}
	
	@Override
	protected void onCreate() {
		super.onCreate();
		trimNames();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		trimNames();
	}
	

	private void trimNames() {
		this.name = (name != null) ? name.trim() : null;
		this.customerId = (customerId != null) ? customerId.trim() : null;
	}
	

	public static final void prepareFilter(SecurityFilter filter) {
		filter.setTargets(TENANT_ID_FIELD, "id", null, null, null);
	}
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AddressInfo getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getDisplayName() {
		return getName();
	}
	
	@Override
    public String toString() {
	    return getName() + " (" + getId() + ")";
    }
}
