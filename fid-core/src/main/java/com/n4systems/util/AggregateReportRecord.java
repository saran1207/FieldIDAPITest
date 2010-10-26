package com.n4systems.util;

public class AggregateReportRecord {
	private Long count;
	
	private String assetTypeName;
	
	private String inspectionTypeGroupName;
	private Long inspectionTypeGroupId;

	
	public AggregateReportRecord(Long count, String assetTypeName) {
		this( count, assetTypeName, null, null );
	}
	
	public AggregateReportRecord(Long count, String assetTypeName, String inspectionTypeGroupName, Long inspectionTypeGroupId) {
		this.count = count;
		this.assetTypeName = assetTypeName;
		this.inspectionTypeGroupName = inspectionTypeGroupName;
		this.inspectionTypeGroupId = inspectionTypeGroupId;
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
	
	public String getInspectionTypeGroupName() {
		return inspectionTypeGroupName;
	}
	
	public void setInspectionTypeGroupName( String inspectionTypeGroupName ) {
		this.inspectionTypeGroupName = inspectionTypeGroupName;
	}
	
	public Long getInspectionTypeGroupId() {
		return inspectionTypeGroupId;
	}
	
	public void setInspectionTypeGroupId( Long inspectionTypeGroupId ) {
		this.inspectionTypeGroupId = inspectionTypeGroupId;
	}
	
}
