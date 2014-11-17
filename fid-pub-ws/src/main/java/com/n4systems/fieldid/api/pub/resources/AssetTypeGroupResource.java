package com.n4systems.fieldid.api.pub.resources;

import com.n4systems.fieldid.api.pub.exceptions.NotImplementedException;
import com.n4systems.fieldid.api.pub.mapping.Mapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.mapping.model.marshal.UserToMessage;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.api.pub.model.Messages.AssetTypeGroupMessage;
import com.n4systems.fieldid.api.pub.model.Messages.AssetTypeGroupMessage.Builder;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.asset.AssetTypeGroupService;
import com.n4systems.model.AssetTypeGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Path("assettypegroup")
@Component
public class AssetTypeGroupResource extends CrudResource<AssetTypeGroup, AssetTypeGroupMessage, Builder> {

	@Autowired private AssetTypeGroupService assetTypeGroupService;

	public AssetTypeGroupResource() {
		super(Messages.assetTypeGroups);
	}

	@Override
	protected CrudService<AssetTypeGroup> crudService() {
		return assetTypeGroupService;
	}

	@Override
	protected AssetTypeGroup createModel(AssetTypeGroupMessage message) {
		return new AssetTypeGroup();
	}

	@Override
	protected Builder createMessageBuilder(AssetTypeGroup model) {
		return AssetTypeGroupMessage.newBuilder();
	}

	@Override
	protected Mapper<AssetTypeGroup, Builder> createModelToMessageBuilderMapper(TypeMapperBuilder<AssetTypeGroup, Builder> mapperBuilder) {
		return mapperBuilder
				.add(AssetTypeGroup::getPublicId, Builder::setId)
				.add(AssetTypeGroup::getName, Builder::setName)
				.addDateToString(AssetTypeGroup::getCreated, Builder::setCreatedDate)
				.addDateToString(AssetTypeGroup::getModified, Builder::setModifiedDate)
				.addModelToMessage(AssetTypeGroup::getCreatedBy, new UserToMessage<>(Builder::setCreatedByUserId, Builder::setCreatedByUserName))
				.addModelToMessage(AssetTypeGroup::getModifiedBy, new UserToMessage<>(Builder::setModifiedByUserId, Builder::setModifiedByUserName))
				.build();
	}

	@Override
	protected Mapper<AssetTypeGroupMessage, AssetTypeGroup> createMessageToModelMapper(TypeMapperBuilder<AssetTypeGroupMessage, AssetTypeGroup> mapperBuilder) {
		return mapperBuilder.build();
	}

	@Override
	public AssetTypeGroupMessage save(AssetTypeGroupMessage message) {
		throw new NotImplementedException();
	}

	@Override
	public AssetTypeGroupMessage update(String id, AssetTypeGroupMessage message) {
		throw new NotImplementedException();
	}
}
