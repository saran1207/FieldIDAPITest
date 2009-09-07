package com.n4systems.fieldid.actions.safetyNetwork;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.ProductTypeLister;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.CatalogServiceImpl;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;

public class CatalogCrud extends AbstractCrud {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CatalogCrud.class);
	
	private Map<String,Boolean> publishedProductTypeIds = new HashMap<String, Boolean>();
	private Map<String,Boolean> publishedInspectionTypeIds = new HashMap<String, Boolean>();
	private CatalogService catalogService;
	
	private List<ListingPair> productTypes;
	private List<ListingPair> inspectionTypes;

	public CatalogCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		
	}
	
	
	public String doShow() {
		return SUCCESS;
	}
	

	@SkipValidation
	public String doEdit() {
		catalogService = new CatalogServiceImpl(persistenceManager,getTenant());
		for (Long id : catalogService.getProductTypeIdsPublished()) {
			publishedProductTypeIds.put(id.toString(),true);
		}
		
		for (Long id : catalogService.getInspectionTypeIdsPublished()) {
			publishedInspectionTypeIds.put(id.toString(),true);
		}
		return SUCCESS;
	}

	public String doUpdate() {
		catalogService = new CatalogServiceImpl(persistenceManager,getTenant());
		try {
			Set<Long> publishProductTypeIds = new HashSet<Long>();
			for (Entry<String,Boolean> entry : publishedProductTypeIds.entrySet()) {
				if (entry.getValue()) {
					publishProductTypeIds.add(Long.parseLong(entry.getKey()));
				}
			}
			
			Set<Long> publishInspectionTypeIds = new HashSet<Long>();
			for (Entry<String,Boolean> entry : publishedInspectionTypeIds.entrySet()) {
				if (entry.getValue()) {
					publishInspectionTypeIds.add(Long.parseLong(entry.getKey()));
				}
			}
			
			HashSet<ProductType> publishedProductTypes = new HashSet<ProductType>();
			if (!publishProductTypeIds.isEmpty()) {
				publishedProductTypes.addAll(persistenceManager.findAll(ProductType.class, publishProductTypeIds, getTenant()));
			}
			catalogService.publishProductTypes(publishedProductTypes);
			
			
			HashSet<InspectionType> publishedInspectionTypes = new HashSet<InspectionType>();
			if (!publishInspectionTypeIds.isEmpty()) {
				publishedInspectionTypes.addAll(persistenceManager.findAll(InspectionType.class, publishInspectionTypeIds, getTenant()));
				
			}
			catalogService.publishInspectionTypes(publishedInspectionTypes);
			
			addFlashMessageText("message.catalog_published");
			return SUCCESS;
		} catch (Exception e) {
			addActionErrorText("error.publishing_catalog");
			logger.error(getLogLinePrefix() + "Could not publish the catalog", e);
		}
		return ERROR;
	}

	public Map<String,Boolean> getPublishedProductTypeIds() {
		return publishedProductTypeIds;
	}

	public List<ListingPair> getProductTypes() {
		if (productTypes == null) {
			productTypes = new ProductTypeLister(persistenceManager,getSecurityFilter()).getProductTypes();
		}

		return productTypes;
	}
	
	public Map<String,Boolean> getPublishedInspectionTypeIds() {
		return publishedInspectionTypeIds;
	}
	
	public List<ListingPair> getInspectionTypes() {
		if (inspectionTypes == null) {
			QueryBuilder<ListingPair> inspectionTypeQuery = new QueryBuilder<ListingPair>(InspectionType.class, getSecurityFilter());
			inspectionTypes = persistenceManager.findAllLP(inspectionTypeQuery, "name");
		}

		return inspectionTypes;
	}


}
