package com.n4systems.fieldid.wicket.model.asset;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.n4systems.fieldid.viewhelpers.handlers.PublishedState;
import com.n4systems.model.Asset;

public class MassUpdateAssetModel implements Serializable{
	
	private Asset asset;
	
	private Map<String,Boolean> select;
	
	public MassUpdateAssetModel() {
		asset = new Asset();
		asset.setIdentified(null);
		select = new HashMap<String,Boolean>();
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Map<String, Boolean> getSelect() {
		return select;
	}

	public void setSelect(Map<String, Boolean> select) {
		this.select = select;
	}

	public PublishedState getPublishedState() {
		return PublishedState.resolvePublishedState(asset.isPublished());
	}

	public void setPublishedState(PublishedState publishedState) {
		asset.setPublished(publishedState.equals(PublishedState.PUBLISHED));
	}

}
