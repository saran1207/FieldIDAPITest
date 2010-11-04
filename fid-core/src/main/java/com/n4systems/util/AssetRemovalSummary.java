package com.n4systems.util;

import com.n4systems.model.Asset;

public class AssetRemovalSummary {
	private Asset asset;
	
	private Long eventsToDelete = 0L;
	private boolean detachFromMaster = false;
	private Long subAssetsToDetach = 0L;
	private Long schedulesToDelete = 0L;
	private Long assetUsedInMasterEvent = 0L;
	private Long projectToDetachFrom = 0L;
	

	public AssetRemovalSummary( Asset asset) {
		this.asset = asset;
	}
	
	public boolean validToDelete() {
		return (assetUsedInMasterEvent == 0);
	}

	public Long getEventsToDelete() {
		return eventsToDelete;
	}

	public void setEventsToDelete(Long eventsToDelete) {
		this.eventsToDelete = eventsToDelete;
	}

	public boolean isDetachFromMaster() {
		return detachFromMaster;
	}

	public void setDetachFromMaster(boolean detachFromMaster) {
		this.detachFromMaster = detachFromMaster;
	}

	public Long getSubAssetsToDetach() {
		return subAssetsToDetach;
	}

	public void setSubAssetsToDetach(Long subAssetsToDetach) {
		this.subAssetsToDetach = subAssetsToDetach;
	}

	public Long getSchedulesToDelete() {
		return schedulesToDelete;
	}

	public void setSchedulesToDelete(Long schedulesToDelete) {
		this.schedulesToDelete = schedulesToDelete;
	}

	public Long getAssetUsedInMasterEvent() {
		return assetUsedInMasterEvent;
	}

	public void setAssetUsedInMasterEvent(Long assetUsedInMasterEvent) {
		this.assetUsedInMasterEvent = assetUsedInMasterEvent;
	}

	public Long getProjectToDetachFrom() {
		return projectToDetachFrom;
	}

	public void setProjectToDetachFrom(Long projectToDetachFrom) {
		this.projectToDetachFrom = projectToDetachFrom;
	}

	public Asset getAsset() {
		return asset;
	}

}