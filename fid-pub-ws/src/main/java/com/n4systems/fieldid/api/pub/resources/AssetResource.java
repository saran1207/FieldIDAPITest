package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.api.pub.mapping.Mapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.*;
import com.n4systems.fieldid.api.pub.mapping.model.unmarshal.*;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.api.pub.model.Messages.AssetMessage;
import com.n4systems.fieldid.api.pub.model.Messages.AssetMessage.Builder;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.stream.Collectors;

@Path("asset")
@Component
public class AssetResource extends CrudResource<Asset, AssetMessage, Builder> {

	@Autowired private AssetService assetService;
	@Autowired private BaseOrgResolver baseOrgResolver;
	@Autowired private UserResolver userResolver;
	@Autowired private AssetTypeResolver assetTypeResolver;
	@Autowired private AssetStatusResolver assetStatusResolver;
	@Autowired private PredefinedLocationResolver predefinedLocationResolver;
	@Autowired private MessageToAssetAttributes messageToAssetAttributes;

	public AssetResource() {
		super(Messages.assets);
	}

	@Override
	protected CrudService<Asset> crudService() {
		return assetService;
	}

	@Override
	protected Asset createModel(AssetMessage message) {
		return new Asset();
	}

	@Override
	protected Builder createMessageBuilder(Asset model) {
		return AssetMessage.newBuilder();
	}

	@Override
	protected Mapper<Asset, Builder> createModelToMessageBuilderMapper(TypeMapperBuilder<Asset, Builder> mapperBuilder) {
		return mapperBuilder
				.add(Asset::getPublicId, Builder::setId)
				.add(Asset::getIdentifier, Builder::setIdentifier)
				.add(Asset::getRfidNumber, Builder::setRfidNumber)
				.add(Asset::getCustomerRefNumber, Builder::setCustomerRefNumber)
				.add(Asset::getPurchaseOrder, Builder::setPurchaseOrder)
				.add(Asset::getComments, Builder::setComments)
				.add(Asset::getNonIntergrationOrderNumber, Builder::setOrderNumber)
				.add(Asset::getDescription, Builder::setDescription)
				.addDateToString(Asset::getCreated, Builder::setCreatedDate)
				.addDateToString(Asset::getModified, Builder::setModifiedDate)
				.addDateToString(Asset::getIdentified, Builder::setIdentifiedDate)
				.addModelToMessage(Asset::getOwner, new ApiModelWithNameToMessage<>(Builder::setOwnerId, Builder::setOwnerName))
				.addModelToMessage(Asset::getCreatedBy, new UserToMessage<>(Builder::setCreatedByUserId, Builder::setCreatedByUserName))
				.addModelToMessage(Asset::getModifiedBy, new UserToMessage<>(Builder::setModifiedByUserId, Builder::setModifiedByUserName))
				.addModelToMessage(Asset::getIdentifiedBy, new UserToMessage<>(Builder::setIdentifiedByUserId, Builder::setIdentifiedByUserName))
				.addModelToMessage(Asset::getAssignedUser, new UserToMessage<>(Builder::setAssignedUserId, Builder::setAssignedUserName))
				.addModelToMessage(Asset::getType, new ApiModelWithNameToMessage<>(Builder::setAssetTypeId, Builder::setAssetTypeName))
				.addModelToMessage(Asset::getAssetStatus, new ApiModelWithNameToMessage<>(Builder::setAssetStatusId, Builder::setAssetStatusName))
				.addModelToMessage(Asset::getGpsLocation, new GpsLocationToMessage<>(Builder::setLatitude, Builder::setLongitude))
				.addModelToMessage(Asset::getAdvancedLocation, new LocationToMessage<>(Builder::setFreeFormLocation, Builder::setPredefinedLocationId, Builder::setPredefinedLocationName))
				.addCollection(Asset::getOrderedInfoOptionList, Builder::addAllAttributes, new AssetAttributeToMessage(), Collectors.toList())
				.build();
	}

	@Override
	protected Mapper<AssetMessage, Asset> createMessageToModelMapper(TypeMapperBuilder<AssetMessage, Asset> mapperBuilder) {
		return mapperBuilder
				.add(AssetMessage::getIdentifier, Asset::setIdentifier)
				.add(AssetMessage::getRfidNumber, Asset::setRfidNumber)
				.add(AssetMessage::getCustomerRefNumber, Asset::setCustomerRefNumber)
				.add(AssetMessage::getPurchaseOrder, Asset::setPurchaseOrder)
				.add(AssetMessage::getComments, Asset::setComments)
				.add(AssetMessage::getOrderNumber, Asset::setNonIntergrationOrderNumber)
				.addStringToDate(AssetMessage::getIdentifiedDate, Asset::setIdentified)
				.add(AssetMessage::getOwnerId, Asset::setOwner, baseOrgResolver)
				.add(AssetMessage::getIdentifiedByUserId, Asset::setIdentifiedBy, userResolver)
				.add(AssetMessage::getAssignedUserId, Asset::setAssignedUser, userResolver)
				.add(AssetMessage::getAssetTypeId, Asset::setType, assetTypeResolver)
				.add(AssetMessage::getAssetStatusId, Asset::setAssetStatus, assetStatusResolver)
				.addMessageToModel(Asset::getGpsLocation, new MessageToGpsLocation<>(AssetMessage::getLatitude, AssetMessage::getLongitude))
				.addMessageToModel(Asset::getAdvancedLocation, new MessageToLocation<>(AssetMessage::getFreeFormLocation, AssetMessage::getPredefinedLocationId, predefinedLocationResolver))
				.addCollection(AssetMessage::getAttributesList, Asset::setInfoOptions, messageToAssetAttributes, Collectors.toSet())
				.build();
	}
}
