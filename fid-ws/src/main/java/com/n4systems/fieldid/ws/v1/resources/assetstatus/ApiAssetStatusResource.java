package com.n4systems.fieldid.ws.v1.resources.assetstatus;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.AssetStatus;

@Component
@Path("assetStatus")
public class ApiAssetStatusResource extends SetupDataResource<ApiAssetStatus, AssetStatus> {

	public ApiAssetStatusResource() {
		super(AssetStatus.class, true);
	}

	@Override
	protected ApiAssetStatus convertEntityToApiModel(AssetStatus status) {
		ApiAssetStatus apiStatus = new ApiAssetStatus();
		apiStatus.setSid(status.getId());
		apiStatus.setModified(status.getModified());
		apiStatus.setActive(status.isActive());
		apiStatus.setName(status.getName());
		return apiStatus;
	}

}
