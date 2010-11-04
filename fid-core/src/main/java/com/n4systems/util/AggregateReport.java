package com.n4systems.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregateReport {

	private List<AggregateReportRecord> eventTypeGroupsByAssetTypes;
	
	private Map<String,List<AggregateReportRecord>> countsByAssetType;
	
	private Map<String,List<AggregateReportRecord>> countsByEventTypeGroup;
	
	private List<AggregateReportRecord> distinctAssetsByAssetType;

	private Long totalEvents;
	
	private Long totalAssets;
	
	private List<String> assetTypes;
	
	private List<String> eventTypeGroups;
	
	
	public List<AggregateReportRecord> getEventTypeGroupsByAssetTypes() {
		return eventTypeGroupsByAssetTypes;
	}

	public void setEventTypeGroupsByAssetTypes(
			List<AggregateReportRecord> eventTypeGroupsByAssetTypes) {
		this.eventTypeGroupsByAssetTypes = eventTypeGroupsByAssetTypes;
		
		countsByAssetType = new HashMap<String, List<AggregateReportRecord>>();
		countsByEventTypeGroup = new HashMap<String, List<AggregateReportRecord>>();
		for( AggregateReportRecord record : eventTypeGroupsByAssetTypes) {
			List<AggregateReportRecord> records = countsByAssetType.get( record.getAssetTypeName() );
			if( records == null ) {
				records = new ArrayList<AggregateReportRecord>();
			}
			records.add( record );
			countsByAssetType.put( record.getAssetTypeName(), records );
			
			
			records = countsByEventTypeGroup.get( record.getEventTypeGroupName() );
			if( records == null ) {
				records = new ArrayList<AggregateReportRecord>();
			}
			records.add( record );
			countsByEventTypeGroup.put( record.getEventTypeGroupName(), records );
		}
		assetTypes = null;
		eventTypeGroups = null;
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
	
	
	public Long getEventsForAssetType( String assetTypeName ) {
		Long count = 0L;
		for( AggregateReportRecord record : countsByAssetType.get( assetTypeName ) ) {
			count += record.getCount();
		}
		return count;
	}
	
	public Long getEventsForEventTypeGroup( String eventTypeGroupName ) {
		Long count = 0L;
		for( AggregateReportRecord record : countsByEventTypeGroup.get( eventTypeGroupName ) ) {
			count += record.getCount();
		}
		return count;
	}
	
	

	public Long getTotalEvents() {
		if( totalEvents == null ) {
			totalEvents = 0L;
			for( AggregateReportRecord record : eventTypeGroupsByAssetTypes) {
				totalEvents += record.getCount();
			}
		}
		return totalEvents;
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

	public Map<String, List<AggregateReportRecord>> getCountsByEventTypeGroup() {
		return countsByEventTypeGroup;
	}

	public List<String> getAssetTypes() {
		if( assetTypes == null ) {
			assetTypes = new ArrayList<String>( countsByAssetType.keySet() );
			Collections.sort(assetTypes);
		}
		return assetTypes;
	}

	public List<String> getEventTypeGroups() {
		if( eventTypeGroups == null ) {
			eventTypeGroups = new ArrayList<String>( countsByEventTypeGroup.keySet() );
			Collections.sort(eventTypeGroups);
		}
		return eventTypeGroups;
	}
	
	
	
}
