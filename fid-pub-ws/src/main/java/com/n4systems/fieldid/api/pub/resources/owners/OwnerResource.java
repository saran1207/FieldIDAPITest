package com.n4systems.fieldid.api.pub.resources.owners;

import com.google.protobuf.Extension;
import com.n4systems.fieldid.api.pub.resources.CrudResource;
import com.n4systems.fieldid.api.pub.serialization.ListResponseMessage;
import com.n4systems.fieldid.api.pub.serialization.OwnerMessage;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.orgs.*;
import com.n4systems.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.util.List;

@Path("owner")
public class OwnerResource extends CrudResource<BaseOrg, OwnerMessage.Owner> {

	@Autowired
	private OrgService orgService;

	@Override
	protected CrudService<BaseOrg> crudService() {
		return orgService;
	}

	@Override
	protected Extension<ListResponseMessage.ListResponse, List<OwnerMessage.Owner>> listResponseType() {
        return ListResponseMessage.owners;
	}

	@Override
	protected OwnerMessage.Owner marshal(BaseOrg org) {
		OwnerMessage.Owner.Builder builder = OwnerMessage.Owner.newBuilder();

		builder
			.setId(org.getPublicId())
			.setName(org.getName())
			.setCode(org.getCode())
			.setNotes(StringUtils.stringOrEmpty(org.getNotes()));

        if(org.getParent() != null)
            builder.setParentId(org.getParent().getPublicId());

		ifNotNull(org.getAddressInfo(), addressInfo -> {
            builder
                    .setStreetAddress(addressInfo.getStreetAddress())
                    .setCity(StringUtils.stringOrEmpty(addressInfo.getCity()))
                    .setState(StringUtils.stringOrEmpty(addressInfo.getState()))
                    .setCountry(StringUtils.stringOrEmpty(addressInfo.getCountry()))
                    .setZip(StringUtils.stringOrEmpty(addressInfo.getZip()))
                    .setPhone1(StringUtils.stringOrEmpty(addressInfo.getPhone1()))
                    .setPhone2(StringUtils.stringOrEmpty(addressInfo.getPhone2()))
                    .setFax1(StringUtils.stringOrEmpty(addressInfo.getFax1()));

            ifNotNull(addressInfo.getGpsLocation(), gps -> {
                ifNotNull(gps.getLatitude(), lat -> builder.setLatitude(lat.toString()));
                ifNotNull(gps.getLongitude(), lon -> builder.setLongitude(lon.toString()));
            });
        });

		ifNotNull(org.getContact(), contact -> {
			builder
				.setContactName(StringUtils.stringOrEmpty(contact.getName()))
				.setContactEmail(StringUtils.stringOrEmpty(contact.getEmail()));
		});

		return builder.build();
	}

	@Override
	protected BaseOrg unmarshal(OwnerMessage.Owner apiModel) {
		// only customers and divisions can be created through the webservice
		ExternalOrg org = null;
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
		org.setTenant(getCurrentTenant());
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
		if (apiModel.getLatitude() != null && apiModel.getLongitude()!= null) {
			address.getGpsLocation().setLatitude(new BigDecimal(apiModel.getLatitude()));
			address.getGpsLocation().setLongitude(new BigDecimal(apiModel.getLongitude()));
		}

		org.setAddressInfo(address);

		Contact contact = new Contact();
		contact.setEmail(apiModel.getContactEmail());
		contact.setName(apiModel.getName());
		org.setContact(contact);

		return org;
	}

}
