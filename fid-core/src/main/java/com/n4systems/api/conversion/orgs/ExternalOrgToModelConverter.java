package com.n4systems.api.conversion.orgs;

import com.n4systems.api.conversion.AbstractViewToModelConverter;
import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.orgs.ExternalOrg;
import com.n4systems.persistence.loaders.GlobalIdLoader;

public abstract class ExternalOrgToModelConverter<M extends ExternalOrg> extends AbstractViewToModelConverter<M, FullExternalOrgView> {
	
	public ExternalOrgToModelConverter(GlobalIdLoader<M> externalIdLoader) {
		super(externalIdLoader);
	}

	@Override
	public void copyProperties(FullExternalOrgView from, M to, boolean isEdit) throws ConversionException {
		to.setName(from.getName());
		to.setCode(from.getCode());
		
		copyContact(from, to);
		copyAddressInfo(from, to);
		to.setNotes(from.getNotes());
	}

	private void copyAddressInfo(FullExternalOrgView from, M to) {
		if (to.getAddressInfo() == null) {
			to.setAddressInfo(new AddressInfo());
		}
		
		AddressInfo address = to.getAddressInfo();
		address.setStreetAddress(from.getStreetAddress());
		address.setCity(from.getCity());
		address.setState(from.getState());
		address.setCountry(from.getCountry());
		address.setZip(from.getZip());
		address.setPhone1(from.getPhone1());
		address.setPhone2(from.getPhone2());
		address.setFax1(from.getFax1());
	}

	private void copyContact(FullExternalOrgView from, M to) {
		if (to.getContact() == null) {
			to.setContact(new Contact());
		}
		
		Contact contact = to.getContact();
		contact.setName(from.getContactName());
		contact.setEmail(from.getContactEmail());
	}
	
}
