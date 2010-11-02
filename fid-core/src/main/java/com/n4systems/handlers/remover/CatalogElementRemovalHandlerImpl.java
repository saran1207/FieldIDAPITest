package com.n4systems.handlers.remover;

import com.n4systems.model.EventType;
import com.n4systems.model.AssetType;
import com.n4systems.model.Tenant;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.model.catalog.CatalogLoader;
import com.n4systems.model.catalog.CatalogSaver;
import com.n4systems.persistence.Transaction;

public class CatalogElementRemovalHandlerImpl implements CatalogElementRemovalHandler {
	private final CatalogLoader catalogLoader;
	private final CatalogSaver catalogSaver;

	private EventType eventType;
	private AssetType assetType;
	
	
	public CatalogElementRemovalHandlerImpl(CatalogLoader catalogLoader, CatalogSaver catalogSaver) {
		super();
		this.catalogLoader = catalogLoader;
		this.catalogSaver = catalogSaver;
	}

	public void cleanUp(Transaction transaction) {
		Catalog catalog = catalogLoader.setTenant(getTenant()).load(transaction);
		if (catalog != null) {
			removeElementFromCatalog(catalog);
			catalogSaver.save(transaction, catalog);
		}
	}

	private void removeElementFromCatalog(Catalog catalog) {
		if (eventType != null) {
			catalog.getPublishedInspectionTypes().remove(eventType);
		}
		if (assetType != null) {
			catalog.getPublishedAssetTypes().remove(assetType);
		}
	}
	
	private Tenant getTenant() {
		if (eventType != null) {
			return eventType.getTenant();
		}
		if (assetType != null) {
			return assetType.getTenant();
		}
		return null;
	}
	
	public CatalogElementRemovalHandler setEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}

	public CatalogElementRemovalHandler setAssetType(AssetType assetType) {
		this.assetType = assetType;
		return this;
	}
}
