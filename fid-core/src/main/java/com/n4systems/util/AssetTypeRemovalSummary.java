package com.n4systems.util;

import com.n4systems.model.AssetType;

public class AssetTypeRemovalSummary {

	private AssetType assetType;

	private Long assetsToDelete = 0L;
	private Long eventsToDelete = 0L;
	private Long subAssetsToDetach = 0L;
	private Long schedulesToDelete = 0L;
	private Long assetsUsedInMasterEvent = 0L;
	private Long assetsToDetachFromProjects = 0L;
	private Long assetTypesToDetachFrom = 0L;
	private Long assetCodeMappingsToDelete = 0L;
	private Long masterAssetsToDetach = 0L;

	public AssetTypeRemovalSummary(AssetType assetType) {
		this.assetType = assetType;
	}

	public boolean validToDelete() {
		return (assetsUsedInMasterEvent == 0);
	}

	public Long getAssetsToDelete() {
		return assetsToDelete;
	}

	public void setAssetsToDelete(Long assetsToDelete) {
		this.assetsToDelete = assetsToDelete;
	}

	public Long getEventsToDelete() {
		return eventsToDelete;
	}

	public void setEventsToDelete(Long eventsToDelete) {
		this.eventsToDelete = eventsToDelete;
	}

	public Long getSchedulesToDelete() {
		return schedulesToDelete;
	}

	public void setSchedulesToDelete(Long schedulesToDelete) {
		this.schedulesToDelete = schedulesToDelete;
	}

	public Long getAssetsUsedInMasterEvent() {
		return assetsUsedInMasterEvent;
	}

	public void setAssetsUsedInMasterEvent(Long assetsUsedInMasterEvent) {
		this.assetsUsedInMasterEvent = assetsUsedInMasterEvent;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public Long getSubAssetsToDetach() {
		return subAssetsToDetach;
	}

	public void setSubAssetsToDetach(Long subAssetsToDetach) {
		this.subAssetsToDetach = subAssetsToDetach;
	}

	public Long getAssetsToDetachFromProjects() {
		return assetsToDetachFromProjects;
	}

	public void setAssetsToDetachFromProjects(Long assetsToDetachFromProjects) {
		this.assetsToDetachFromProjects = assetsToDetachFromProjects;
	}

	public Long getAssetCodeMappingsToDelete() {
		return assetCodeMappingsToDelete;
	}

	public void setAssetCodeMappingsToDelete(Long assetCodeMappingsToDelete) {
		this.assetCodeMappingsToDelete = assetCodeMappingsToDelete;
	}

	public Long getAssetTypesToDetachFrom() {
		return assetTypesToDetachFrom;
	}

	public void setAssetTypesToDetachFrom(Long assetTypesToDetachFrom) {
		this.assetTypesToDetachFrom = assetTypesToDetachFrom;
	}

	public Long getMasterAssetsToDetach() {
		return masterAssetsToDetach;
	}

	public void setMasterAssetsToDetach(Long masterAssetsToDetach) {
		this.masterAssetsToDetach = masterAssetsToDetach;
	}

}
