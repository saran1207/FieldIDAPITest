package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.n4systems.fieldid.viewhelpers.ColumnMapping;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;

public class InfoFieldDynamicGroupGenerator {
	
	private static final int LARGE_ORDER_NUMBER_TO_PLACE_GROUP_AT_THE_END_OF_COLUMN_SELECTION = 2048;
	private static final int STARTING_ORDER_INDEX_FOR_INFO_FIELDS = 1024;
	
	private final CommonProductAttributeFinder commonAttributeFinder;
	private final String idPrefix;
	
	private List<ColumnMappingGroup> dynamigGroups;
	private final String pathPrefix;
	
	
	public InfoFieldDynamicGroupGenerator(CommonProductAttributeFinder commonAttributeFinder, String idPrefix, String pathPrefix) {
		this.commonAttributeFinder = commonAttributeFinder;
		this.idPrefix = idPrefix;
		this.pathPrefix = pathPrefix;
	}
	
	
	public InfoFieldDynamicGroupGenerator(CommonProductAttributeFinder commonAttributeFinder, String idPrefix) {
		this(commonAttributeFinder, idPrefix, null);
	}


	public List<ColumnMappingGroup> getDynamicGroups(Long productTypeId, List<Long> productTypeIds) {
		if (dynamigGroups == null) {
			List<Long> consolidateAssetTypeIds = consolidateAssetTypeIds(productTypeId, productTypeIds);
			createDynamicGroups(consolidateAssetTypeIds);
		}
		return dynamigGroups;
	}

	
	private void createDynamicGroups(List<Long> assetTypeIds) {
		dynamigGroups = new ArrayList<ColumnMappingGroup>();
		
		SortedSet<String> infoFieldNames = getCommonInfoFields(assetTypeIds);
		ColumnMappingGroup infoFieldGroup = convertInfoFiledsToColumnMappings(infoFieldNames);
		
		dynamigGroups.add(infoFieldGroup);
	}
	

	private List<Long> consolidateAssetTypeIds(Long assetTypeId, List<Long> assetTypeIds) {
		List<Long> commonAttributeassetTypeIds = new ArrayList<Long>();
		
		if (assetTypeId != null) {
			commonAttributeassetTypeIds.add(assetTypeId) ;
		} else {
			commonAttributeassetTypeIds.addAll(assetTypeIds);
		}
		return commonAttributeassetTypeIds;
	}

	
	private ColumnMappingGroup convertInfoFiledsToColumnMappings(SortedSet<String> infoFieldNames) {
		
		int order = STARTING_ORDER_INDEX_FOR_INFO_FIELDS;
		ColumnMappingGroup infoFieldGroup = createColumnMappingGroup();
		for (String fieldName: infoFieldNames) {
			infoFieldGroup.getMappings().add(createInfoFieldMapping(fieldName, order));
			order++;
		}
		
		return infoFieldGroup;
	}

	
	private ColumnMappingGroup createColumnMappingGroup() {
		ColumnMappingGroup infoFieldGroup = new ColumnMappingGroup(idPrefix + "_product_info_options", "label.productattributes", LARGE_ORDER_NUMBER_TO_PLACE_GROUP_AT_THE_END_OF_COLUMN_SELECTION);
		infoFieldGroup.setDynamic(true);
		return infoFieldGroup;
	}
	

	private SortedSet<String> getCommonInfoFields(List<Long> assetTypeIds) {
		if (assetTypeIds.isEmpty()) {
			return new TreeSet<String>();
		}
		return commonAttributeFinder.findAllCommonInfoFieldNames(assetTypeIds);
	}

	
	private ColumnMapping createInfoFieldMapping(String fieldName, int order) {
		String id = idPrefix + "_infooption_" + fieldName.toLowerCase().replaceAll(" ", "_");
		
		String path = (pathPrefix != null) ? pathPrefix + "." : "";
		path +=  "infoOptions{infoField.name=" + fieldName + "}.name";
		
		return new ColumnMapping(id, fieldName, path, null, null, false, false, order, null);
	}
}
