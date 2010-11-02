package com.n4systems.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregateReport {

	private List<AggregateReportRecord> inspectionTypeGroupsByAssetTypes;
	
	private Map<String,List<AggregateReportRecord>> countsByAssetType;
	
	private Map<String,List<AggregateReportRecord>> countsByInspectionTypeGroup;
	
	private List<AggregateReportRecord> distinctAssetsByAssetType;

	private Long totalInspections;
	
	private Long totalAssets;
	
	private List<String> assetTypes;
	
	private List<String> inspectionTypeGroups;
	
	
	public List<AggregateReportRecord> getInspectionTypeGroupsByAssetTypes() {
		return inspectionTypeGroupsByAssetTypes;
	}

	public void setEventTypeGroupsByAssetTypes(
			List<AggregateReportRecord> inspectionTypeGroupsByAssetTypes) {
		this.inspectionTypeGroupsByAssetTypes = inspectionTypeGroupsByAssetTypes;
		
		countsByAssetType = new HashMap<String, List<AggregateReportRecord>>();
		countsByInspectionTypeGroup = new HashMap<String, List<AggregateReportRecord>>();
		for( AggregateReportRecord record : inspectionTypeGroupsByAssetTypes) {
			List<AggregateReportRecord> records = countsByAssetType.get( record.getAssetTypeName() );
			if( records == null ) {
				records = new ArrayList<AggregateReportRecord>();
			}
			records.add( record );
			countsByAssetType.put( record.getAssetTypeName(), records );
			
			
			records = countsByInspectionTypeGroup.get( record.getInspectionTypeGroupName() );
			if( records == null ) {
				records = new ArrayList<AggregateReportRecord>();
			}
			records.add( record );
			countsByInspectionTypeGroup.put( record.getInspectionTypeGroupName(), records );
		}
		assetTypes = null;
		inspectionTypeGroups = null;
	}

	public List<AggregateReportRecord> getDistinctAssetsByAssetType() {
		return distinctAssetsByAssetType;
	}

	public void setDistinctAssetsByAssetType(
			List<AggregateReportRecord> distinctAssetsByAssetType) {
		this.distinctAssetsByAssetType = distinctAssetsByAssetType;
	}

	
	public Long getDisinctProductsForAssetType( String productTypeName ) {
		for( AggregateReportRecord record : distinctAssetsByAssetType) {
			if( record.getAssetTypeName().equals( productTypeName ) ) {
				return record.getCount();
			}
		}
		return null;
	}
	
	
	public Long getInspectionsForAssetType( String productTypeName ) {
		Long count = 0L;
		for( AggregateReportRecord record : countsByAssetType.get( productTypeName ) ) {
			count += record.getCount();
		}
		return count;
	}
	
	public Long getInspectionsForInspectionTypeGroup( String inspectionTypeGroupName ) {
		Long count = 0L;
		for( AggregateReportRecord record : countsByInspectionTypeGroup.get( inspectionTypeGroupName ) ) {
			count += record.getCount();
		}
		return count;
	}
	
	

	public Long getTotalInspections() {
		if( totalInspections == null ) {
			totalInspections = 0L;
			for( AggregateReportRecord record : inspectionTypeGroupsByAssetTypes) {
				totalInspections += record.getCount();
			}
		}
		return totalInspections;
	}

	public Long getTotalAssets() {
		if( totalAssets == null ) {
			totalAssets = 0L;
			for( AggregateReportRecord record : distinctAssetsByAssetType) {
				totalAssets += record.getCount();
			}
		}
		return totalAssets;
	}

	public Map<String, List<AggregateReportRecord>> getCountsByAssetType() {
		return countsByAssetType;
	}

	public Map<String, List<AggregateReportRecord>> getCountsByInspectionTypeGroup() {
		return countsByInspectionTypeGroup;
	}

	public List<String> getAssetTypes() {
		if( assetTypes == null ) {
			assetTypes = new ArrayList<String>( countsByAssetType.keySet() );
			Collections.sort(assetTypes);
		}
		return assetTypes;
	}

	public List<String> getInspectionTypeGroups() {
		if( inspectionTypeGroups == null ) {
			inspectionTypeGroups = new ArrayList<String>( countsByInspectionTypeGroup.keySet() );
			Collections.sort( inspectionTypeGroups );
		}
		return inspectionTypeGroups;
	}
	
	
	
}
