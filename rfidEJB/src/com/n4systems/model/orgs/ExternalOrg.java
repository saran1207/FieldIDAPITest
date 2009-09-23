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


@SuppressWarnings("serial")
@MappedSuperclass
abstract public class ExternalOrg extends BaseOrg {
	
	private String code;
	
	@Column(name="legacy_id", nullable=true)
	private Long legacyId;
	
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
			// address info is it's own entity, if this is the first save we need to wipe
			// the id so it creates a new one.  If this is a merge, we'll capture the id
			// and set it back on the addressinfo after it's been moved to this org
			Long addressInfoId = (getAddressInfo() != null) ? getAddressInfo().getId() : null;
			setAddressInfo(newAddressInfo);
			getAddressInfo().setId(addressInfoId);
		} else {
			setAddressInfo(null);
		}
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	public InternalOrg getLinkedOrg() {
		return (InternalOrg)linkedOrg;
	}

	public void setLinkedOrg(InternalOrg linkedOrg) {
		this.linkedOrg = linkedOrg;
	}

	public Long getLegacyId() {
		return legacyId;
	}

	@Override
	public String getDisplayName() {
		return super.getDisplayName() + " <->";
	}

	@Override
	public String toString() {
		return super.toString() + " <->";
	}
	
	
}
