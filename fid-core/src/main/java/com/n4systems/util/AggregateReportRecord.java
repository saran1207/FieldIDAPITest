package com.n4systems.util;

public class AggregateReportRecord {
	private Long count;
	
	private String assetTypeName;
	
	private String eventTypeGroupName;
	private Long eventTypeGroupId;

	
	public AggregateReportRecord(Long count, String assetTypeName) {
		this( count, assetTypeName, null, null );
	}
	
	public AggregateReportRecord(Long count, String assetTypeName, String eventTypeGroupName, Long eventTypeGroupId) {
		this.count = count;
		this.assetTypeName = assetTypeName;
		this.eventTypeGroupName = eventTypeGroupName;
		this.eventTypeGroupId = eventTypeGroupId;
	}
	
	public Long getCount() {
		return count;
	}
	
	public void setCount( Long count ) {
		this.count = count;
	}
	
	public String getAssetTypeName() {
		return assetTypeName;
	}
	
	public void setAssetTypeName( String assetTypeName) {
		this.assetTypeName = assetTypeName;
	}
	
	public String getEventTypeGroupName() {
		return eventTypeGroupName;
	}
	
	public void setEventTypeGroupName( String eventTypeGroupName) {
		this.eventTypeGroupName = eventTypeGroupName;
	}
	
	public Long getEventTypeGroupId() {
		return eventTypeGroupId;
	}
	
	public void setEventTypeGroupId( Long eventTypeGroupId) {
		this.eventTypeGroupId = eventTypeGroupId;
	}
	
}
