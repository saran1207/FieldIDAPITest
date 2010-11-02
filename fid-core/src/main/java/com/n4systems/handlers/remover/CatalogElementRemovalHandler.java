package com.n4systems.handlers.remover;

import com.n4systems.model.EventType;
import com.n4systems.model.AssetType;
import com.n4systems.persistence.Transaction;

public interface CatalogElementRemovalHandler {

	public abstract void cleanUp(Transaction transaction);

	public abstract CatalogElementRemovalHandler setEventType(EventType eventType);

	public abstract CatalogElementRemovalHandler setAssetType(AssetType assetType);

}
