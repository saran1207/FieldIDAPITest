package com.n4systems.services.safetyNetwork.catalog;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.TenantOrganization;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;

public abstract class CatalogImportHandler {


	protected final PersistenceManager persistenceManager;
	protected final TenantOrganization tenant;
	protected final CatalogService importCatalog;
	
	protected CatalogImportHandler(PersistenceManager persistenceManager, TenantOrganization tenant, CatalogService importCatalog) {
		super();
		this.persistenceManager = persistenceManager;
		this.tenant = tenant;
		this.importCatalog = importCatalog;
	}

	public abstract void importCatalog() throws ImportFailureException;
	public abstract void rollback();
}