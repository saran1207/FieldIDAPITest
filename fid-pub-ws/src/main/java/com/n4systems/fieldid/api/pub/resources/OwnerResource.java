package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.api.pub.mapping.Mapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.ApiModelWithNameToMessage;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.UserToMessage;
import com.n4systems.fieldid.api.pub.mapping.model.unmarshal.MessageToGpsLocation;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.api.pub.model.Messages.OwnerMessage;
import com.n4systems.fieldid.api.pub.model.Messages.OwnerMessage.Builder;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.InternalOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Path;

@Path("owners")
@Component
public class OwnerResource extends CrudResource<BaseOrg, OwnerMessage, Builder> {

	@Autowired private OrgService orgService;

	public OwnerResource() {
		super(Messages.owners);
	}

	@Override
	protected CrudService<BaseOrg> crudService() {
		return orgService;
	}

	@Override
	protected BaseOrg createModel(OwnerMessage message) {
		BaseOrg parent = testNotFound(orgService.findByPublicId(message.getParentId()));
		if (parent.isInternal()) {
			return new CustomerOrg();
		} else if (parent.isCustomer()) {
			return new DivisionOrg();
		} else {
			throw new BadRequestException("Owner parent cannot be a division");
		}
	}

	@Override
	protected Builder createMessageBuilder(BaseOrg model) {
		return OwnerMessage.newBuilder();
	}

	@Override
	protected Mapper<BaseOrg, Builder> createModelToMessageBuilderMapper(TypeMapperBuilder<BaseOrg, Builder> mapperBuilder) {
		return mapperBuilder
				.add(BaseOrg::getPublicId, Builder::setId)
				.add(BaseOrg::getName, Builder::setName)
				.add(BaseOrg::getCode, Builder::setCode)
				.add(BaseOrg::getNotes, Builder::setNotes)
				.addDateToString(BaseOrg::getCreated, Builder::setCreatedDate)
				.addDateToString(BaseOrg::getModified, Builder::setModifiedDate)
				.addModelToMessage(BaseOrg::getCreatedBy, new UserToMessage<>(Builder::setCreatedByUserId, Builder::setCreatedByUserName))
				.addModelToMessage(BaseOrg::getModifiedBy, new UserToMessage<>(Builder::setModifiedByUserId, Builder::setModifiedByUserName))
				.addModelToMessage(BaseOrg::getParent, new ApiModelWithNameToMessage<>(Builder::setParentId, Builder::setParentName))
				.addModelToMessage(BaseOrg::getAddressInfo, (subBuilder) -> subBuilder
						.add(AddressInfo::getStreetAddress, Builder::setStreetAddress)
						.add(AddressInfo::getCity, Builder::setCity)
						.add(AddressInfo::getState, Builder::setState)
						.add(AddressInfo::getCountry, Builder::setCountry)
						.add(AddressInfo::getZip, Builder::setZip)
						.add(AddressInfo::getPhone1, Builder::setPhone1)
						.add(AddressInfo::getPhone2, Builder::setPhone2)
						.add(AddressInfo::getFax1, Builder::setFax1)
						.addModelToMessage(AddressInfo::getGpsLocation, (locBuilder) -> locBuilder
								.addToString(GpsLocation::getLatitude, Builder::setLatitude)
								.addToString(GpsLocation::getLongitude, Builder::setLongitude)
								.build())
						.build())
				.addModelToMessage(BaseOrg::getContact, (subBuilder) -> subBuilder
						.add(Contact::getName, Builder::setContactName)
						.add(Contact::getEmail, Builder::setContactEmail)
						.build())
				.build();
	}

	/**
	 * If functional interface has-XXX returns true, or user set the input field, system updates this field
	 * Otherwise, it stays unchanged
	 * @param mapperBuilder
	 * @return
	 */
	@Override
	protected Mapper<OwnerMessage, BaseOrg> createMessageToModelMapper(TypeMapperBuilder<OwnerMessage, BaseOrg> mapperBuilder) {
		return mapperBuilder
				.add(OwnerMessage::hasParentId, OwnerMessage::getParentId, this::setOrgParent, true)
				.add(OwnerMessage::hasName, OwnerMessage::getName, BaseOrg::setName)
				.add(OwnerMessage::hasCode, OwnerMessage::getCode, BaseOrg::setCode)
				.add(OwnerMessage::hasNotes, OwnerMessage::getNotes, BaseOrg::setNotes)
				.addMessageToModel(BaseOrg::getAddressInfo, (subBuilder) -> subBuilder
						.add(OwnerMessage::hasStreetAddress, OwnerMessage::getStreetAddress, AddressInfo::setStreetAddress)
						.add(OwnerMessage::hasCity, OwnerMessage::getCity, AddressInfo::setCity)
						.add(OwnerMessage::hasState, OwnerMessage::getState, AddressInfo::setState)
						.add(OwnerMessage::hasCountry, OwnerMessage::getCountry, AddressInfo::setCountry)
						.add(OwnerMessage::hasZip,OwnerMessage::getZip, AddressInfo::setZip)
						.add(OwnerMessage::hasPhone1, OwnerMessage::getPhone1, AddressInfo::setPhone1)
						.add(OwnerMessage::hasPhone2, OwnerMessage::getPhone2, AddressInfo::setPhone2)
						.add(OwnerMessage::hasFax1, OwnerMessage::getFax1, AddressInfo::setFax1)
						.addMessageToModel(AddressInfo::getGpsLocation, new MessageToGpsLocation<>(OwnerMessage::hasLatitude, OwnerMessage::getLatitude, OwnerMessage::hasLongitude, OwnerMessage::getLongitude))
						.build())
				.addMessageToModel(BaseOrg::getContact, (subBuilder) -> subBuilder
						.add(OwnerMessage::hasContactEmail, OwnerMessage::getContactEmail, Contact::setEmail)
						.add(OwnerMessage::hasContactName, OwnerMessage::getContactName, Contact::setName)
						.build())
				.build();
	}

	private void setOrgParent(BaseOrg org, String parentId) {
		BaseOrg parent = testNotFound(orgService.findByPublicId(parentId));
		if (org instanceof CustomerOrg && parent instanceof InternalOrg)
			((CustomerOrg) org).setParent((InternalOrg) parent);
		else if (org instanceof DivisionOrg && parent instanceof CustomerOrg)
			((DivisionOrg) org).setParent((CustomerOrg) parent);
		else
			throw new IllegalArgumentException("Parent org [" + parent + "] is wrong type for [" + org + "]");
	}

}
