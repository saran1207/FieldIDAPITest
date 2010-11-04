package com.n4systems.services.safetyNetwork.catalog.summary;

public class CatalogImportSummary {

	private AssetTypeGroupImportSummary assetTypeGroupImportSummary = new AssetTypeGroupImportSummary();
	private AssetTypeImportSummary assetTypeImportSummary = new AssetTypeImportSummary();
	private EventTypeImportSummary eventTypeImportSummary = new EventTypeImportSummary();
	private EventTypeGroupImportSummary eventTypeGroupImportSummary = new EventTypeGroupImportSummary();
	private StateSetImportSummary stateSetImportSummary = new StateSetImportSummary();
	private AssetTypeRelationshipsImportSummary assetTypeRelationshipsImportSummary = new AssetTypeRelationshipsImportSummary();
	
		
	public AssetTypeImportSummary getAssetTypeImportSummary() {
		return assetTypeImportSummary;
	}
	
	public void setAssetTypeImportSummary(AssetTypeImportSummary assetTypeImportSummary) {
		this.assetTypeImportSummary = assetTypeImportSummary;
	}

	public EventTypeImportSummary getEventTypeImportSummary() {
		return eventTypeImportSummary;
	}

	public void setEventTypeImportSummary(EventTypeImportSummary eventTypeImportSummary) {
		this.eventTypeImportSummary = eventTypeImportSummary;
	}

	public EventTypeGroupImportSummary getEventTypeGroupImportSummary() {
		return eventTypeGroupImportSummary;
	}

	public void setEventTypeGroupImportSummary(EventTypeGroupImportSummary eventTypeGroupImportSummary) {
		this.eventTypeGroupImportSummary = eventTypeGroupImportSummary;
	}

	public StateSetImportSummary getStateSetImportSummary() {
		return stateSetImportSummary;
	}

	public void setStateSetImportSummary(StateSetImportSummary stateSetImportSummary) {
		this.stateSetImportSummary = stateSetImportSummary;
	}

	public AssetTypeGroupImportSummary getAssetTypeGroupImportSummary() {
		return assetTypeGroupImportSummary;
	}

	public void setAssetTypeGroupImportSummary(AssetTypeGroupImportSummary assetTypeGroupImportSummary) {
		this.assetTypeGroupImportSummary = assetTypeGroupImportSummary;
	}

	public AssetTypeRelationshipsImportSummary getAssetTypeRelationshipsImportSummary() {
		return assetTypeRelationshipsImportSummary;
	}

	
}
