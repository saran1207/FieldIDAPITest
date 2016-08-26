package com.n4systems.ws.resources;

import com.n4systems.model.AssetStatus;
import com.n4systems.ws.model.assettype.WsAssetStatus;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("/AssetStatus")
public class AssetStatusResource extends BaseResource<AssetStatus, WsAssetStatus> {

	public AssetStatusResource(@Context UriInfo uriInfo) {
		super(uriInfo, new AssetStatusResourceDefiner());
	}

}
