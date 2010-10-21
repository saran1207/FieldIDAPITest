package com.n4systems.handlers.remover;

import com.n4systems.model.InspectionType;
import com.n4systems.model.AssetType;
import com.n4systems.model.Tenant;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.model.catalog.CatalogLoader;
import com.n4systems.model.catalog.CatalogSaver;
import com.n4systems.persistence.Transaction;

public class CatalogElementRemovalHandlerImpl implements CatalogElementRemovalHandler {
	private final CatalogLoader catalogLoader;
	private final CatalogSaver catalogSaver;

	private InspectionType inspectionType;
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
		if (inspectionType != null) {
			catalog.getPublishedInspectionTypes().remove(inspectionType);
		}
		if (assetType != null) {
			catalog.getPublishedProductTypes().remove(assetType);
		}
	}
	
	private Tenant getTenant() {
		if (inspectionType != null) {
			return inspectionType.getTenant();
		}
		if (assetType != null) {
			return assetType.getTenant();
		}
		return null;
	}
	
	public CatalogElementRemovalHandler setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}

	public CatalogElementRemovalHandler setProductType(AssetType assetType) {
		this.assetType = assetType;
		return this;
	}
}