package com.n4systems.handlers.remover;

import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.persistence.Transaction;

public interface CatalogElementRemovalHandler {

	public abstract void cleanUp(Transaction transaction);

	public abstract CatalogElementRemovalHandler setInspectionType(InspectionType inspectionType);

	public abstract CatalogElementRemovalHandler setProductType(ProductType productType);

}