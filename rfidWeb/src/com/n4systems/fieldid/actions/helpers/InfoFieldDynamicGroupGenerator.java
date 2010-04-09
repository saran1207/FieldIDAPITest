package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.fieldid.viewhelpers.ColumnMapping;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.util.ListingPair;
import com.n4systems.util.ListingPairs;

public class InfoFieldDynamicGroupGenerator {
	
	private final PersistenceManager persistenceManager;
	private final ProductManager productManager;
	private List<ColumnMappingGroup> dynamigGroups;
	
	public InfoFieldDynamicGroupGenerator(final PersistenceManager persistenceManager, final ProductManager productManager) {
		this.persistenceManager = persistenceManager;
		this.productManager = productManager;
	}
	
	public List<ColumnMappingGroup> getDynamicGroups(Long productTypeId, String idPrefix,  List<ListingPair> productTypePairs, Tenant tenant) {
		return getDynamicGroups(productTypeId, idPrefix, null, productTypePairs, tenant);
	}
	
	public List<ColumnMappingGroup> getDynamicGroups(Long productTypeId, String idPrefix, String pathPrefix, List<ListingPair> productTypePairs, Tenant tenant) {
		if (dynamigGroups == null) {
			dynamigGroups = new ArrayList<ColumnMappingGroup>();
			ColumnMappingGroup infoFieldGroup = new ColumnMappingGroup(idPrefix + "_product_info_options", "label.productattributes", 2048);
			infoFieldGroup.setDynamic(true);
			
			int order = 1024;
			List<Long> productTypeIds = new ArrayList<Long>();
			if (productTypeId != null) {
				productTypeIds.add(productTypeId) ;
			} else {
				productTypeIds.addAll(ListingPairs.convertToIdList(productTypePairs));
			}
			
			
			List<ProductType> productTypes = persistenceManager.findAll(ProductType.class, new HashSet<Long>(productTypeIds), tenant, "infoFields");
			
			
			for (String fieldName: productManager.findAllCommonInfoFieldNames(productTypes)) {
				infoFieldGroup.getMappings().add(createInfoFieldMapping(fieldName, idPrefix, pathPrefix, order));
				order++;
			}
			
			dynamigGroups.add(infoFieldGroup);
		}
		return dynamigGroups;
	}
	
	private ColumnMapping createInfoFieldMapping(String fieldName, String idPrefix, String pathPrefix, int order) {
		String id = idPrefix + "_infooption_" + fieldName.toLowerCase().replaceAll(" ", "_");
		
		String path = (pathPrefix != null) ? pathPrefix + "." : "";
		path +=  "infoOptions{infoField.name=" + fieldName + "}.name";
		
		return new ColumnMapping(id, fieldName, path, null, null, false, false, order);
	}
}
