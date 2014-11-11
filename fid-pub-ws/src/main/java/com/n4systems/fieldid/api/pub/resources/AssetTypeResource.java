package com.n4systems.fieldid.api.pub.resources;


import com.n4systems.fieldid.api.pub.mapping.Mapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.fieldid.api.pub.model.Messages.AssetTypeMessage;
import com.n4systems.fieldid.api.pub.model.Messages.AssetTypeMessage.Builder;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.model.AssetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Path("assettype")
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
		return null;
	}

	@Override
	protected Mapper<AssetTypeMessage, AssetType> createMessageToModelMapper(TypeMapperBuilder<AssetTypeMessage, AssetType> mapperBuilder) {
		return null;
	}
}
