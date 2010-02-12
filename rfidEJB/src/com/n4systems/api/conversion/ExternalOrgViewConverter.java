package com.n4systems.api.conversion;

import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.orgs.ExternalOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.GlobalIdLoader;

public abstract class ExternalOrgViewConverter<M extends ExternalOrg> extends AbstractViewModelConverter<M, FullExternalOrgView> {
	
	public ExternalOrgViewConverter(SecurityFilter filter, Class<M> modelClass) {
		super(filter, modelClass, FullExternalOrgView.class);
	}

	public ExternalOrgViewConverter(GlobalIdLoader<M> externalIdLoader, SecurityFilter filter, Class<M> modelClass) {
		super(externalIdLoader, filter, modelClass, FullExternalOrgView.class);
	}

	@Override
	public void copyProperties(FullExternalOrgView from, M to, boolean isEdit) throws ConversionException {
		to.setName(from.getName());
		to.setCode(from.getCode());
		
		copyContact(from, to);
		copyAddressInfo(from, to);
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
