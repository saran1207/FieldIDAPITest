package com.n4systems.model.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.persistence.loaders.NonSecuredListLoader;

public class RecursiveLinkedChildAssetLoader extends NonSecuredListLoader<Asset> {
	private final LinkedChildAssetLoader linkedAssetLoader;
	private Asset asset;
	
	public RecursiveLinkedChildAssetLoader(LinkedChildAssetLoader linkedAssetLoader) {
		this.linkedAssetLoader = linkedAssetLoader;
	}
	
	public RecursiveLinkedChildAssetLoader() {
		this(new LinkedChildAssetLoader());
	}
	
	@Override
	protected List<Asset> load(EntityManager em) {
		if (asset == null) {
			throw new SecurityException("asset must be set");
		}
		
		List<Asset> allLinkedChildAssets = new ArrayList<Asset>();
		
		loadChildTree(em, asset, allLinkedChildAssets);
				
		return allLinkedChildAssets;
	}
	
	protected void loadChildTree(EntityManager em, Asset asset, List<Asset> allLinkedChildAssets) {
		List<Asset> linkedChildren = linkedAssetLoader.setProduct(asset).load(em);
		
		allLinkedChildAssets.addAll(linkedChildren);
		
		for (Asset linkedChild: linkedChildren) {
			loadChildTree(em, linkedChild, allLinkedChildAssets);
		}
	}

	public RecursiveLinkedChildAssetLoader setProduct(Asset asset) {
		this.asset = asset;
		return this;
	}
	
}
