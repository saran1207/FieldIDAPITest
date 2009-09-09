package com.n4systems.model.inspectionschedulecount;

import java.util.Date;

import com.n4systems.model.orgs.BaseOrg;

public class InspectionScheduleCount implements Comparable<InspectionScheduleCount> {
	private Date nextInspectionDate;
	private BaseOrg owner;
	private String productTypeName;
	private String inspectionTypeName;
	private long inspectionCount;
	
	public InspectionScheduleCount(Date nextInspectionDate, BaseOrg owner, String productTypeName, String inspectionTypeName, long inspectionCount) {
		this.nextInspectionDate = nextInspectionDate;
		this.owner = owner;
		this.productTypeName = productTypeName;
		this.inspectionTypeName = inspectionTypeName;
		this.inspectionCount = inspectionCount;
	}

	public Date getNextInspectionDate() {
    	return nextInspectionDate;
    }

	public String getOrganizationName() {
		return (owner != null && owner.getInternalOrg() != null) ? owner.getInternalOrg().getName() : "";
	}
	
	public String getCustomerName() {
		return (owner != null && owner.getCustomerOrg() != null) ? owner.getCustomerOrg().getName() : "";
    }

	public String getDivisionName() {
		return (owner != null && owner.getDivisionOrg() != null) ? owner.getDivisionOrg().getName() : "";
    }

	public String getProductTypeName() {
    	return productTypeName;
    }

	public String getInspectionTypeName() {
    	return inspectionTypeName;
    }

	public long getInspectionCount() {
    	return inspectionCount;
    }

	public int compareTo(InspectionScheduleCount other) {
		int comp = getNextInspectionDate().compareTo(other.getNextInspectionDate());
		
		if (comp == 0) {
			comp = getOrganizationName().compareTo(other.getOrganizationName());
		}
		if (comp == 0) {
			comp = getCustomerName().compareTo(other.getCustomerName());
		}
		if (comp == 0) {
			comp = getDivisionName().compareTo(other.getDivisionName());
		}
		if (comp == 0) {
			comp = getProductTypeName().compareTo(other.getProductTypeName());
		}
		if (comp == 0) {
			comp = getInspectionTypeName().compareTo(other.getInspectionTypeName());
		}
		
		return comp;
	}
	
}
