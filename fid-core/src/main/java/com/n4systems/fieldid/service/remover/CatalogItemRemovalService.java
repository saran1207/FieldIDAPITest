package com.n4systems.fieldid.service.remover;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.catalog.Catalog;
import org.springframework.transaction.annotation.Transactional;

public class CatalogItemRemovalService extends FieldIdPersistenceService {

    @Transactional
	public void cleanUp(AssetType assetType, EventType eventType) {
        final Catalog catalog = persistenceService.find(createTenantSecurityBuilder(Catalog.class));

		if (catalog != null) {
			removeElementFromCatalog(assetType, eventType, catalog);
            persistenceService.update(catalog);
		}
	}

	private void removeElementFromCatalog(AssetType assetType, EventType eventType, Catalog catalog) {
		if (eventType != null) {
			catalog.getPublishedEventTypes().remove(eventType);
		}
		if (assetType != null) {
			catalog.getPublishedAssetTypes().remove(assetType);
		}
	}

}
