package com.n4systems.model.orgs;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.security.AllowSafetyNetworkAccess;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;


@SuppressWarnings("serial")
@MappedSuperclass
abstract public class ExternalOrg extends BaseOrg {


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

	@AllowSafetyNetworkAccess
	public InternalOrg getLinkedOrg() {
		return (InternalOrg)linkedOrg;
	}

	public void setLinkedOrg(InternalOrg linkedOrg) {
		this.linkedOrg = linkedOrg;
	}
	
}
