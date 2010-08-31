package com.n4systems.api.conversion.orgs;

import com.n4systems.api.conversion.AbstractModelToViewConverter;
import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.orgs.ExternalOrg;

public abstract class ExternalOrgToViewConverter<M extends ExternalOrg> extends AbstractModelToViewConverter<M, FullExternalOrgView> {
	
	public ExternalOrgToViewConverter() {
		super(FullExternalOrgView.class);
	}

	@Override
	public void copyProperties(M from, FullExternalOrgView to) throws ConversionException {
		to.setName(from.getName());
		to.setCode(from.getCode());
		to.setGlobalId(from.getGlobalId());
		
		copyContact(from, to);
		copyAddressInfo(from, to);
	}

	private void copyAddressInfo(M from, FullExternalOrgView to) {
		AddressInfo address = from.getAddressInfo();
		if (address != null) {
			to.setStreetAddress(address.getStreetAddress());
			to.setCity(address.getCity());
			to.setState(address.getState());
			to.setCountry(address.getCountry());
			to.setZip(address.getZip());
			to.setPhone1(address.getPhone1());
			to.setPhone2(address.getPhone2());
			to.setFax1(address.getFax1());
		}
	}

	private void copyContact(M from, FullExternalOrgView to) {
		Contact contact = from.getContact();
		if (contact != null) {
			to.setContactName(contact.getName());
			to.setContactEmail(contact.getEmail());
		}
	}
	
}
