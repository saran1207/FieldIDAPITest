package com.n4systems.webservice.assetdownload;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.webservice.dto.ProductServiceDTO;
import com.n4systems.webservice.dto.RequestResponse;

public class AssetListResponse extends RequestResponse {
	private List<ProductServiceDTO> assets = new ArrayList<ProductServiceDTO>();
	
	public List<ProductServiceDTO> getAssets() {
		return assets;
	}

	public void setAssets(List<ProductServiceDTO> assets) {
		this.assets = assets;
	}
}
