package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;

//XXX - move into com.n4systems.model.division package
//@Entity
//@Table(name = "divisions")
public class DivisionBroken extends EntityWithTenant implements NamedEntity, Listable<Long> {
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String divisionID;
	
//	@Embedded
//	@AttributeOverrides({ 
//		@AttributeOverride(name="name", column = @Column(name="contactname")),
//		@AttributeOverride(name="email", column = @Column(name="contactemail"))
//	})
	private Contact contact = new Contact();

//	@ManyToOne(optional = false)
//	private Customer customer;

//	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private AddressInfo addressInfo = new AddressInfo();
	
	public DivisionBroken() {}

	
	@Override
	protected void onCreate() {
		super.onCreate();
		trimName();
	}
	
	@Override
	protected void onUpdate() {
		super.onUpdate();
		trimName();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (contact == null) {
			contact = new Contact();
		}
	}

	private void trimName() {
		this.name = (name != null) ? name.trim() : null;
	}
	
//	public Customer getCustomer() {
//		return customer;
//	}
//
//	public void setCustomer(Customer customer) {
//		this.customer = customer;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return getName();
	}

	@Override
    public String toString() {
	    return getName() + " (" + getId() + ")";
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


	public String getDivisionID() {
		return divisionID;
	}


	public void setDivisionID(String divisionID) {
		this.divisionID = divisionID;
	}
	
}
