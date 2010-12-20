package com.n4systems.ws.resources;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.ws.model.assettype.WsAssetStatus;

@Path("/AssetStatus")
public class AssetStatusResource extends BaseResource<AssetStatus, WsAssetStatus> {

	public AssetStatusResource(@Context UriInfo uriInfo) {
		super(uriInfo, new AssetStatusResourceDefiner());
	}

}
