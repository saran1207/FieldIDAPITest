package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;

import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.fieldid.viewhelpers.ColumnMapping;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.model.ProductType;
import com.n4systems.model.security.SecurityFilter;

public class InfoFieldDynamicGroupGenerator {
	
	private final PersistenceManager persistenceManager;
	private final ProductManager productManager;
	private List<ColumnMappingGroup> dynamigGroups;
	
	public InfoFieldDynamicGroupGenerator(final PersistenceManager persistenceManager, final ProductManager productManager) {
		this.persistenceManager = persistenceManager;
		this.productManager = productManager;
	}
	
	public List<ColumnMappingGroup> getDynamicGroups(Long productTypeId, String idPrefix, final SecurityFilter filter) {
		return getDynamicGroups(productTypeId, idPrefix, null, filter);
	}
	
	public List<ColumnMappingGroup> getDynamicGroups(Long productTypeId, String idPrefix, String pathPrefix, final SecurityFilter filter) {
		if (dynamigGroups == null) {
			dynamigGroups = new ArrayList<ColumnMappingGroup>();
			ColumnMappingGroup infoFieldGroup = new ColumnMappingGroup(idPrefix + "_product_info_options", "label.productattributes", 2048);
			infoFieldGroup.setDynamic(true);
			
			int order = 1024;
			if (productTypeId != null) {
				// when a product type has been selected, we will use all the infofields from the product type
				ProductType productType = persistenceManager.find(ProductType.class, productTypeId, filter, "infoFields");
				
				// construct and add our field mappings
				for (InfoFieldBean field: productType.getInfoFields()) {
					infoFieldGroup.getMappings().add(createInfoFieldMapping(field.getName(), idPrefix, pathPrefix, order));
					order++;
				}
			} else {
				// if no product type was selected we need to compute all the common infofields
				for (String fieldName: productManager.findAllCommonInfoFieldNames(filter)) {
					infoFieldGroup.getMappings().add(createInfoFieldMapping(fieldName, idPrefix, pathPrefix, order));
					order++;
				}
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
