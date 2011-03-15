package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.n4systems.fieldid.viewhelpers.ColumnMappingGroupView;
import com.n4systems.fieldid.viewhelpers.ColumnMappingView;

public class InfoFieldDynamicGroupGenerator {
	
	private static final int LARGE_ORDER_NUMBER_TO_PLACE_GROUP_AT_THE_END_OF_COLUMN_SELECTION = 2048;
	private static final int STARTING_ORDER_INDEX_FOR_INFO_FIELDS = 1024;
	
	private final CommonAssetAttributeFinder commonAttributeFinder;
	private final String idPrefix;
	
	private List<ColumnMappingGroupView> dynamigGroups;
	private final String pathPrefix;
	
	
	public InfoFieldDynamicGroupGenerator(CommonAssetAttributeFinder commonAttributeFinder, String idPrefix, String pathPrefix) {
		this.commonAttributeFinder = commonAttributeFinder;
		this.idPrefix = idPrefix;
		this.pathPrefix = pathPrefix;
	}
	
	
	public InfoFieldDynamicGroupGenerator(CommonAssetAttributeFinder commonAttributeFinder, String idPrefix) {
		this(commonAttributeFinder, idPrefix, null);
	}


	public List<ColumnMappingGroupView> getDynamicGroups(Long assetTypeId, List<Long> assetTypeIds) {
		if (dynamigGroups == null) {
			List<Long> consolidateAssetTypeIds = consolidateAssetTypeIds(assetTypeId, assetTypeIds);
			createDynamicGroups(consolidateAssetTypeIds);
		}
		return dynamigGroups;
	}

	
	private void createDynamicGroups(List<Long> assetTypeIds) {
		dynamigGroups = new ArrayList<ColumnMappingGroupView>();
		
		SortedSet<String> infoFieldNames = getCommonInfoFields(assetTypeIds);
		ColumnMappingGroupView infoFieldGroup = convertInfoFiledsToColumnMappings(infoFieldNames);
		
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

	
	private ColumnMappingGroupView convertInfoFiledsToColumnMappings(SortedSet<String> infoFieldNames) {
		
		int order = STARTING_ORDER_INDEX_FOR_INFO_FIELDS;
		ColumnMappingGroupView infoFieldGroup = createColumnMappingGroup();
		for (String fieldName: infoFieldNames) {
			infoFieldGroup.getMappings().add(createInfoFieldMapping(fieldName, order));
			order++;
		}
		
		return infoFieldGroup;
	}

	
	private ColumnMappingGroupView createColumnMappingGroup() {
		ColumnMappingGroupView infoFieldGroup = new ColumnMappingGroupView(idPrefix + "_asset_info_options", "label.assetattributes", LARGE_ORDER_NUMBER_TO_PLACE_GROUP_AT_THE_END_OF_COLUMN_SELECTION, null);
		infoFieldGroup.setDynamic(true);
		return infoFieldGroup;
	}
	

	private SortedSet<String> getCommonInfoFields(List<Long> assetTypeIds) {
		if (assetTypeIds.isEmpty()) {
			return new TreeSet<String>();
		}
		return commonAttributeFinder.findAllCommonInfoFieldNames(assetTypeIds);
	}

	
	private ColumnMappingView createInfoFieldMapping(String fieldName, int order) {
		String id = idPrefix + "_infooption_" + fieldName.toLowerCase().replaceAll(" ", "_");
		
		String path = (pathPrefix != null) ? pathPrefix + "." : "";
		path +=  "infoOptions{infoField.name=" + fieldName + "}.name";
		
		return new ColumnMappingView(id, fieldName, path, null, null, false, false, order, null, null);
	}
}
