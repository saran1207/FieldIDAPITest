package com.n4systems.fieldid.api.pub.resources;


import com.n4systems.fieldid.api.pub.exceptions.NotImplementedException;
import com.n4systems.fieldid.api.pub.mapping.Mapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.*;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.api.pub.model.Messages.AssetTypeMessage;
import com.n4systems.fieldid.api.pub.model.Messages.AssetTypeMessage.Builder;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.model.AssetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.stream.Collectors;

@Path("assetTypes")
@Component
public class AssetTypeResource extends CrudResource<AssetType, AssetTypeMessage, Builder> {

	@Autowired private AssetTypeService assetTypeService;

	public AssetTypeResource() {
		super(Messages.assetTypes);
	}

	@Override
	protected CrudService<AssetType> crudService() {
		return assetTypeService;
	}

	@Override
	protected AssetType createModel(AssetTypeMessage message) {
		return new AssetType();
	}

	@Override
	protected Builder createMessageBuilder(AssetType model) {
		return AssetTypeMessage.newBuilder();
	}

	@Override
	protected Mapper<AssetType, Builder> createModelToMessageBuilderMapper(TypeMapperBuilder<AssetType, Builder> mapperBuilder) {
		return mapperBuilder
				.add(AssetType::getPublicId, Builder::setId)
				.add(AssetType::getName, Builder::setName)
				.add(AssetType::getWarnings, Builder::setWarnings)
				.add(AssetType::getInstructions, Builder::setInstructions)
				.add(AssetType::getCautionUrl, Builder::setCautionUrl)
				.add(AssetType::getDescriptionTemplate, Builder::setDescriptionTemplate)
				.add(AssetType::getManufactureCertificateText, Builder::setManufactureCertificateText)
				.add(AssetType::isHasManufactureCertificate, Builder::setHasManufactureCertificate)
				.add(AssetType::isLinkable, Builder::setAllowAssetLinking)
				.add(AssetType::hasProcedures, Builder::setHasProcedures)
				.addDateToString(AssetType::getCreated, Builder::setCreatedDate)
				.addDateToString(AssetType::getModified, Builder::setModifiedDate)
				.addModelToMessage(AssetType::getGroup, new ApiModelWithNameToMessage<>(Builder::setAssetTypeGroupId, Builder::setAssetTypeGroupName))
				.addModelToMessage(AssetType::getCreatedBy, new UserToMessage<>(Builder::setCreatedByUserId, Builder::setCreatedByUserName))
				.addModelToMessage(AssetType::getModifiedBy, new UserToMessage<>(Builder::setModifiedByUserId, Builder::setModifiedByUserName))
				.addCollection(AssetType::getSchedules, Builder::addAllSchedules, new AssetTypeScheduleToMessage(), Collectors.toList())
				.addCollection(AssetType::getAssociatedEventTypes, Builder::addAllAssociatedEventTypes, new AssociatedEventTypeToMessage(), Collectors.toList())
				.addCollection(AssetType::getAvailableInfoFields, Builder::addAllAttributeTypes, new AssetAttributeTypeToMessage(), Collectors.toList())
				.build();
	}

	@Override
	protected Mapper<AssetTypeMessage, AssetType> createMessageToModelMapper(TypeMapperBuilder<AssetTypeMessage, AssetType> mapperBuilder) {
		return mapperBuilder.build();
	}

	@Override
	public AssetTypeMessage save(AssetTypeMessage message) {
		throw new NotImplementedException();
	}

	@Override
	public AssetTypeMessage update(String id, AssetTypeMessage message) {
		throw new NotImplementedException();
	}
}
