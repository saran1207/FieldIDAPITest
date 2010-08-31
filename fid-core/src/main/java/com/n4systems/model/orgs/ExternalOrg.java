package com.n4systems.model.orgs;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.security.NetworkAccessLevel;
import com.n4systems.model.security.SecurityLevel;


@SuppressWarnings("serial")
@MappedSuperclass
abstract public class ExternalOrg extends BaseOrg {
	
	private String code;
	
	@Embedded
	@AttributeOverrides({ 
		@AttributeOverride(name="name", column = @Column(name="contactname")),
		@AttributeOverride(name="email", column = @Column(name="contactemail"))
	})
	private Contact contact = new Contact();
	
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name="linked_id")
	private BaseOrg linkedOrg;
	
	public ExternalOrg() {}

	@Override
	protected void onCreate() {
		super.onCreate();
		updateFieldsFromOrg(getLinkedOrg());
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		updateFieldsFromOrg(getLinkedOrg());
	}

	protected void updateFieldsFromOrg(InternalOrg org) {
		if (org != null) {
			setName(org.getName());
			updateAddressInfo(org.getAddressInfo());
		}
	}
	
	protected void updateAddressInfo(AddressInfo newAddressInfo) {
		if (newAddressInfo != null) {
			if (getAddressInfo() == null) {
				setAddressInfo(new AddressInfo());
			}
			newAddressInfo.copyFieldsTo(getAddressInfo());
		} else {
			setAddressInfo(null);
		}
	}
	
	@NetworkAccessLevel(value=SecurityLevel.DIRECT, allowCustomerUsers=false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public Contact getContact() {
		return contact;
	}
	
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	@NetworkAccessLevel(SecurityLevel.ALLOWED)
	public InternalOrg getLinkedOrg() {
		return (InternalOrg)linkedOrg;
	}

	public void setLinkedOrg(InternalOrg linkedOrg) {
		this.linkedOrg = linkedOrg;
	}
	
}
