package com.n4systems.fieldid.ws.v1.resources.assettypegroup;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.AssetTypeGroup;

@Component
@Path("assetTypeGroup")
public class ApiAssetTypeGroupResource extends SetupDataResource<ApiAssetTypeGroup, AssetTypeGroup> {

	public ApiAssetTypeGroupResource() {
		super(AssetTypeGroup.class);
	}

	@Override
	protected ApiAssetTypeGroup convertEntityToApiModel(AssetTypeGroup assetTypeGroup) {
		ApiAssetTypeGroup apiAssetTypeGroup = new ApiAssetTypeGroup();
		apiAssetTypeGroup.setSid(assetTypeGroup.getId());
		apiAssetTypeGroup.setActive(true);
		apiAssetTypeGroup.setModified(assetTypeGroup.getModified());
		apiAssetTypeGroup.setName(assetTypeGroup.getName());
		apiAssetTypeGroup.setWeight(assetTypeGroup.getOrderIdx());
		return apiAssetTypeGroup;
	}

}
