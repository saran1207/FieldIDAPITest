package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.api.pub.mapping.Mapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.UserToMessage;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.api.pub.model.Messages.AssetStatusMessage;
import com.n4systems.fieldid.api.pub.model.Messages.AssetStatusMessage.Builder;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.asset.AssetStatusService;
import com.n4systems.model.AssetStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Path("assetStatuses")
@Component
public class AssetStatusResource extends CrudResource<AssetStatus, AssetStatusMessage, Builder> {

	@Autowired
	private AssetStatusService assetStatusService;

	public AssetStatusResource() {
		super(Messages.assetStatuses);
	}

	@Override
	protected CrudService<AssetStatus> crudService() {
		return assetStatusService;
	}

	@Override
	protected AssetStatus createModel(AssetStatusMessage message) {
		return new AssetStatus();
	}

	@Override
	protected Builder createMessageBuilder(AssetStatus model) {
		return AssetStatusMessage.newBuilder();
	}

	@Override
	protected Mapper<AssetStatus, Builder> createModelToMessageBuilderMapper(TypeMapperBuilder<AssetStatus, Builder> mapperBuilder) {
		return mapperBuilder
				.add(AssetStatus::getPublicId, Builder::setId)
				.add(AssetStatus::getName, Builder::setName)
				.addDateToString(AssetStatus::getCreated, Builder::setCreatedDate)
				.addDateToString(AssetStatus::getModified, Builder::setModifiedDate)
				.addModelToMessage(AssetStatus::getCreatedBy, new UserToMessage<>(Builder::setCreatedByUserId, Builder::setCreatedByUserName))
				.addModelToMessage(AssetStatus::getModifiedBy, new UserToMessage<>(Builder::setModifiedByUserId, Builder::setModifiedByUserName))
				.build();
	}

	@Override
	protected Mapper<AssetStatusMessage, AssetStatus> createMessageToModelMapper(TypeMapperBuilder<AssetStatusMessage, AssetStatus> mapperBuilder) {
		return mapperBuilder.add(AssetStatusMessage::getName, AssetStatus::setName).build();
	}
}
