package com.n4systems.webservice.assetdownload;

import com.n4systems.webservice.dto.ProductServiceDTO;
import com.n4systems.webservice.dto.RequestResponse;

import java.util.ArrayList;
import java.util.List;

public class AssetListResponse extends RequestResponse {
	private List<ProductServiceDTO> assets = new ArrayList<ProductServiceDTO>();
	
	public List<ProductServiceDTO> getAssets() {
		return assets;
	}

	public void setAssets(List<ProductServiceDTO> assets) {
		this.assets = assets;
	}
}
