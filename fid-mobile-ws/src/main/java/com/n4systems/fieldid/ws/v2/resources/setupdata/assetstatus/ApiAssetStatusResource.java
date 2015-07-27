package com.n4systems.fieldid.ws.v2.resources.setupdata.assetstatus;

import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.AssetStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("assetStatus")
public class ApiAssetStatusResource extends SetupDataResourceReadOnly<ApiAssetStatus, AssetStatus> {

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
