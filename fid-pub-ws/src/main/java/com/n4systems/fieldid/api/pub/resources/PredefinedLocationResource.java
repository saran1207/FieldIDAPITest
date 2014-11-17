package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.api.pub.mapping.Mapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.ApiModelWithNameToMessage;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.UserToMessage;
import com.n4systems.fieldid.api.pub.mapping.model.unmarshal.BaseOrgResolver;
import com.n4systems.fieldid.api.pub.mapping.model.unmarshal.PredefinedLocationResolver;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.api.pub.model.Messages.PredefinedLocationMessage;
import com.n4systems.fieldid.api.pub.model.Messages.PredefinedLocationMessage.Builder;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.predefinedlocation.PredefinedLocationService;
import com.n4systems.model.location.PredefinedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Path("location")
@Component
public class PredefinedLocationResource extends CrudResource<PredefinedLocation, PredefinedLocationMessage, Builder> {

	@Autowired private PredefinedLocationService predefinedLocationService;
	@Autowired private PredefinedLocationResolver predefinedLocationResolver;
	@Autowired private BaseOrgResolver baseOrgResolver;

	public PredefinedLocationResource() {
		super(Messages.predefinedLocations);
	}

	@Override
	protected CrudService<PredefinedLocation> crudService() {
		return predefinedLocationService;
	}

	@Override
	protected PredefinedLocation createModel(PredefinedLocationMessage message) {
		return new PredefinedLocation();
	}

	@Override
	protected Builder createMessageBuilder(PredefinedLocation model) {
		return PredefinedLocationMessage.newBuilder();
	}

	@Override
	protected Mapper<PredefinedLocation, Builder> createModelToMessageBuilderMapper(TypeMapperBuilder<PredefinedLocation, Builder> mapperBuilder) {
		return mapperBuilder
				.add(PredefinedLocation::getPublicId, Builder::setId)
				.add(PredefinedLocation::getName, Builder::setName)
				.addDateToString(PredefinedLocation::getCreated, Builder::setCreatedDate)
				.addDateToString(PredefinedLocation::getModified, Builder::setModifiedDate)
				.addModelToMessage(PredefinedLocation::getCreatedBy, new UserToMessage<>(Builder::setCreatedByUserId, Builder::setCreatedByUserName))
				.addModelToMessage(PredefinedLocation::getModifiedBy, new UserToMessage<>(Builder::setModifiedByUserId, Builder::setModifiedByUserName))
				.addModelToMessage(PredefinedLocation::getOwner, new ApiModelWithNameToMessage<>(Builder::setOwnerId, Builder::setOwnerName))
				.addModelToMessage(PredefinedLocation::getParent, new ApiModelWithNameToMessage<>(Builder::setParentId, Builder::setParentName))
				.build();
	}

	@Override
	protected Mapper<PredefinedLocationMessage, PredefinedLocation> createMessageToModelMapper(TypeMapperBuilder<PredefinedLocationMessage, PredefinedLocation> mapperBuilder) {
		return mapperBuilder
				.add(PredefinedLocationMessage::getName, PredefinedLocation::setName)
				.add(PredefinedLocationMessage::getParentId, PredefinedLocation::setParent, (publicId, context) -> {
					// you can only set a parent on creation
					if (context.getTo().isNew()) {
						return predefinedLocationResolver.convert(publicId);
					} else {
						return context.getTo().getParent();
					}
				})
				.add(PredefinedLocationMessage::getOwnerId, PredefinedLocation::setOwner, (publicId, context) -> {
					// Only top level locations can define an owner
					if (context.getTo().getParent() == null) {
						return baseOrgResolver.convert(publicId);
					} else {
						return context.getTo().getOwner();
					}
				})
				.build();
	}

}
