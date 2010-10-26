package com.n4systems.model.product;

import java.util.List;

import javax.persistence.EntityManager;


import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.Asset;
import com.n4systems.model.user.User;
import com.n4systems.persistence.savers.Saver;

public class AssetSaver extends Saver<Asset> {
	private final RecursiveLinkedChildAssetLoader linkedAssetLoader;
	private User modifiedBy;
	
	public AssetSaver(RecursiveLinkedChildAssetLoader linkedAssetLoader) {
		this.linkedAssetLoader = linkedAssetLoader;
	}
	
	public AssetSaver() {
		this(new RecursiveLinkedChildAssetLoader());
	}
	
	@Override
	public void save(EntityManager em, Asset asset) {
		// Due to the way Asset is setup right now, we can call persist on it
		// or we'll get lazy loads, until this is fixed, we'll just always call update 
		update(em, asset);
	}

	@Override
	public Asset update(EntityManager em, Asset asset) {
		// we need to capture this now, as it will be lost after merge
		boolean linkedProductChanged = asset.linkedAssetHasChanged();

		setModifiedByOnProduct(asset);
		Asset managedAsset = super.update(em, asset);
		
		managedAsset = resave(em, managedAsset);
		
		if (linkedProductChanged && !asset.isNew()) {
			forceNetworkIdRecalc(em, asset);
		}
		
		return managedAsset;
	}
	

	private Asset resave(EntityManager em, Asset asset) {
		// if the networkid is null, we need to update which will force the network Id to get setup
		// the one case where this doesn't need to happen is if it was connected on create
		if (asset.getNetworkId() == null) {
			asset.touch();
			asset = super.update(em, asset);
		}
		return asset;
	}
	
	private void forceNetworkIdRecalc(EntityManager em, Asset asset) {
		List<Asset> linkedAssets = linkedAssetLoader.setProduct(asset).load(em);
		
		for (Asset linkedAsset : linkedAssets) {
			linkedAsset.touch();
			super.update(em, linkedAsset);
		}
	}
	
	private void setModifiedByOnProduct(Asset asset) {
		if (modifiedBy != null) {
			asset.setModifiedBy(modifiedBy);
		}
	}

	@Override
	protected void remove(EntityManager em, Asset asset) {
		throw new NotImplementedException();
	}
	
	public AssetSaver setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
		return this;
	}
}
