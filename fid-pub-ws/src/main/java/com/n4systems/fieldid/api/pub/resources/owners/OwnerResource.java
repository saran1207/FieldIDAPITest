package com.n4systems.fieldid.api.pub.resources.owners;

import com.n4systems.fieldid.api.pub.resources.CrudResource;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.orgs.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Path;
import java.math.BigDecimal;

@Path("owner")
public class OwnerResource extends CrudResource<BaseOrg, Owner> {

	@Autowired
	private OrgService orgService;

	@Override
	protected CrudService<BaseOrg> crudService() {
		return orgService;
	}

	@Override
	protected Owner marshal(BaseOrg org) {
		Owner owner = new Owner();
		owner.setId(org.getPublicId());
        BaseOrg parent = org.getParent();
		owner.setParentId(parent == null ? "" : parent.getPublicId());
		owner.setName(org.getName());
		owner.setCode(org.getCode());
		owner.setNotes(org.getNotes());

		ifNotNull(org.getAddressInfo(), addressInfo -> {
			owner.setStreetAddress(addressInfo.getStreetAddress());
			owner.setCity(addressInfo.getCity());
			owner.setState(addressInfo.getState());
			owner.setCountry(addressInfo.getCountry());
			owner.setZip(addressInfo.getZip());
			owner.setPhone1(addressInfo.getPhone1());
			owner.setPhone2(addressInfo.getPhone2());
			owner.setFax1(addressInfo.getFax1());

			ifNotNull(addressInfo.getGpsLocation(), gps -> {
				owner.setLatitude(gps.getLatitude().toString());
				owner.setLongitude(gps.getLongitude().toString());
			});
		});

		ifNotNull(org.getContact(), contact -> {
			owner.setContactName(contact.getName());
			owner.setContactEmail(contact.getEmail());
		});

		return owner;
	}

	@Override
	protected BaseOrg unmarshal(Owner apiModel) {
		// only customers and divisions can be created through the webservice
		ExternalOrg org;
		BaseOrg parent = testNotFound(orgService.findByPublicId(apiModel.getParentId()));
		if (parent.isInternal()) {
			org = new CustomerOrg();
			((CustomerOrg) org).setParent((InternalOrg) parent);
		} else if (parent.isCustomer()) {
			org = new DivisionOrg();
			((DivisionOrg) org).setParent((CustomerOrg) parent);
		} else {
			throw new BadRequestException("Owner parent cannot be a division");
		}
		org.setName(apiModel.getName());
		org.setCode(apiModel.getCode());
		org.setNotes(apiModel.getNotes());

		AddressInfo address = new AddressInfo();
		address.setStreetAddress(apiModel.getStreetAddress());
		address.setCity(apiModel.getCity());
		address.setState(apiModel.getState());
		address.setCountry(apiModel.getCountry());
		address.setZip(apiModel.getZip());
		address.setPhone1(apiModel.getPhone1());
		address.setPhone2(apiModel.getPhone2());
		address.setFax1(apiModel.getFax1());
		if (apiModel.getLatitude() != null && apiModel.getLongitude() != null) {
			address.getGpsLocation().setLatitude(new BigDecimal(apiModel.getLatitude()));
			address.getGpsLocation().setLongitude(new BigDecimal(apiModel.getLongitude()));
		}
		org.setAddressInfo(address);

		Contact contact = new Contact();
		contact.setEmail(apiModel.getContactEmail());
		contact.setName(apiModel.getContactName());
		org.setContact(contact);
		return org;
	}

}
