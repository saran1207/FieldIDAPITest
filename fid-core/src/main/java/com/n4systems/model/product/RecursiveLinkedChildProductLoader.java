package com.n4systems.model.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.persistence.loaders.NonSecuredListLoader;

public class RecursiveLinkedChildProductLoader extends NonSecuredListLoader<Asset> {
	private final LinkedChildProductLoader linkedProductLoader;
	private Asset asset;
	
	public RecursiveLinkedChildProductLoader(LinkedChildProductLoader linkedProductLoader) {
		this.linkedProductLoader = linkedProductLoader;
	}
	
	public RecursiveLinkedChildProductLoader() {
		this(new LinkedChildProductLoader());
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
		List<Asset> linkedChildren = linkedProductLoader.setProduct(asset).load(em);
		
		allLinkedChildAssets.addAll(linkedChildren);
		
		for (Asset linkedChild: linkedChildren) {
			loadChildTree(em, linkedChild, allLinkedChildAssets);
		}
	}

	public RecursiveLinkedChildProductLoader setProduct(Asset asset) {
		this.asset = asset;
		return this;
	}
	
}
