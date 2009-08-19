package com.n4systems.handlers.remover;

import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.model.catalog.CatalogLoader;
import com.n4systems.model.catalog.CatalogSaver;
import com.n4systems.persistence.Transaction;

public class CatalogElementRemovalHandlerImpl implements CatalogElementRemovalHandler {
	private final CatalogLoader catalogLoader;
	private final CatalogSaver catalogSaver;

	private InspectionType inspectionType;
	private ProductType productType;
	
	
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
		if (productType != null) {
			catalog.getPublishedProductTypes().remove(productType);
		}
	}
	
	private Tenant getTenant() {
		if (inspectionType != null) {
			return inspectionType.getTenant();
		}
		if (productType != null) {
			return productType.getTenant();
		}
		return null;
	}
	
	public CatalogElementRemovalHandler setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}

	public CatalogElementRemovalHandler setProductType(ProductType productType) {
		this.productType = productType;
		return this;
	}
}