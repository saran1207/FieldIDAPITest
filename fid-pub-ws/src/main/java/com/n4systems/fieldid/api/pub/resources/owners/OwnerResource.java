package com.n4systems.fieldid.api.pub.resources.owners;

import com.google.protobuf.FieldOptions;
import com.n4systems.fieldid.api.pub.resources.CrudResource;
import com.n4systems.fieldid.api.pub.serialization.Ext_Extensions;
import com.n4systems.fieldid.api.pub.serialization.Ext_ListResponse;
import com.n4systems.fieldid.api.pub.serialization.ListResponse;
import com.n4systems.fieldid.api.pub.serialization.Owner;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.orgs.*;
import com.squareup.wire.Extension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.util.List;

@Path("owner")
public class OwnerResource extends CrudResource<BaseOrg, Owner> {

	@Autowired
	private OrgService orgService;

	@Override
	protected CrudService<BaseOrg> crudService() {
		return orgService;
	}

	@Override
	protected Extension<ListResponse, List<Owner>> listResponseType() {
        return Ext_ListResponse.owners;
	}

	@Override
	protected Owner marshal(BaseOrg org) {
		Owner.Builder builder = new Owner.Builder();

		builder
			.id(org.getPublicId())
			.name(org.getName())
			.code(org.getCode())
			.notes(org.getNotes());

        if(org.getParent() != null)
            builder.parentId(org.getParent().getPublicId());

		ifNotNull(org.getAddressInfo(), addressInfo -> {
            builder
                    .streetAddress(addressInfo.getStreetAddress())
                    .city(addressInfo.getCity())
                    .state(addressInfo.getState())
                    .country(addressInfo.getCountry())
                    .zip(addressInfo.getZip())
                    .phone1(addressInfo.getPhone1())
                    .phone2(addressInfo.getPhone2())
                    .fax1(addressInfo.getFax1());

            ifNotNull(addressInfo.getGpsLocation(), gps -> {
                ifNotNull(gps.getLatitude(), lat -> builder.latitude(lat.toString()));
                ifNotNull(gps.getLongitude(), lon -> builder.longitude(lon.toString()));
            });
        });

		ifNotNull(org.getContact(), contact -> {
			builder
				.contactName(contact.getName())
				.contactEmail(contact.getEmail());
		});

        Owner.FIELD_OPTIONS_ID.getExtension(Ext_Extensions.serialized_name);

		return builder.build();
	}

	@Override
	protected BaseOrg unmarshal(Owner apiModel) {
		// only customers and divisions can be created through the webservice
		ExternalOrg org = null;
		BaseOrg parent = testNotFound(orgService.findByPublicId(apiModel.parentId));
		if (parent.isInternal()) {
			org = new CustomerOrg();
			((CustomerOrg) org).setParent((InternalOrg) parent);
		} else if (parent.isCustomer()) {
			org = new DivisionOrg();
			((DivisionOrg) org).setParent((CustomerOrg) parent);
		} else {
			throw new BadRequestException("Owner parent cannot be a division");
		}
		org.setTenant(getCurrentTenant());
		org.setName(apiModel.name);
		org.setCode(apiModel.code);
		org.setNotes(apiModel.notes);

		AddressInfo address = new AddressInfo();
		address.setStreetAddress(apiModel.streetAddress);
		address.setCity(apiModel.city);
		address.setState(apiModel.state);
		address.setCountry(apiModel.country);
		address.setZip(apiModel.zip);
		address.setPhone1(apiModel.phone1);
		address.setPhone2(apiModel.phone2);
		address.setFax1(apiModel.fax1);
		if (apiModel.latitude != null && apiModel.longitude != null) {
			address.getGpsLocation().setLatitude(new BigDecimal(apiModel.latitude));
			address.getGpsLocation().setLongitude(new BigDecimal(apiModel.longitude));
		}

		org.setAddressInfo(address);

		Contact contact = new Contact();
		contact.setEmail(apiModel.contactEmail);
		contact.setName(apiModel.contactName);
		org.setContact(contact);

		return org;
	}

}
