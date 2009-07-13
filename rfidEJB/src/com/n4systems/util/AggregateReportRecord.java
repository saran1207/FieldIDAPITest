package com.n4systems.util;

public class AggregateReportRecord {
	private Long count;
	
	private String productTypeName;
	
	private String inspectionTypeGroupName;
	private Long inspectionTypeGroupId;

	
	public AggregateReportRecord(Long count, String productTypeName ) {
		this( count, productTypeName, null, null );
	}
	
	public AggregateReportRecord(Long count, String productTypeName, String inspectionTypeGroupName, Long inspectionTypeGroupId) {
		super();
		this.count = count;
		this.productTypeName = productTypeName;
		this.inspectionTypeGroupName = inspectionTypeGroupName;
		this.inspectionTypeGroupId = inspectionTypeGroupId;
	}
	
	public Long getCount() {
		return count;
	}
	
	public void setCount( Long count ) {
		this.count = count;
	}
	
	public String getProductTypeName() {
		return productTypeName;
	}
	
	public void setProductTypeName( String productTypeName ) {
		this.productTypeName = productTypeName;
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
