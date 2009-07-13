package com.n4systems.model.inspectionschedulecount;

import java.util.Date;

public class InspectionScheduleCount {
	private Date nextInspectionDate;
	private String ownerName;
	private String divisionName;
	private String productTypeName;
	private String inspectionTypeName;
	private long inspectionCount;
	
	public InspectionScheduleCount() {}
	
	public InspectionScheduleCount(Date nextInspectionDate, String ownerName, String productTypeName, String inspectionTypeName, long inspectionCount) {
		this.nextInspectionDate = nextInspectionDate;
		this.ownerName = ownerName;
		this.productTypeName = productTypeName;
		this.inspectionTypeName = inspectionTypeName;
		this.inspectionCount = inspectionCount;
	}
	
	public InspectionScheduleCount(Date nextInspectionDate, String ownerName, String divisionName, String productTypeName, String inspectionTypeName, long inspectionCount) {
		this.nextInspectionDate = nextInspectionDate;
		this.ownerName = ownerName;
		this.divisionName = divisionName;
		this.productTypeName = productTypeName;
		this.inspectionTypeName = inspectionTypeName;
		this.inspectionCount = inspectionCount;
	}

	public Date getNextInspectionDate() {
    	return nextInspectionDate;
    }

	public void setNextInspectionDate(Date nextInspectionDate) {
    	this.nextInspectionDate = nextInspectionDate;
    }

	public String getOwnerName() {
    	return (ownerName != null) ? ownerName : "";
    }

	public void setOwnerName(String ownerName) {
    	this.ownerName = ownerName;
    }

	public String getDivisionName() {
		return (divisionName != null) ? divisionName : "";
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getProductTypeName() {
    	return productTypeName;
    }

	public void setProductTypeName(String productTypeName) {
    	this.productTypeName = productTypeName;
    }

	public String getInspectionTypeName() {
    	return inspectionTypeName;
    }

	public void setInspectionTypeName(String inspectionTypeName) {
    	this.inspectionTypeName = inspectionTypeName;
    }

	public long getInspectionCount() {
    	return inspectionCount;
    }

	public void setInspectionCount(long inspectionCount) {
    	this.inspectionCount = inspectionCount;
    }
	
}
