package com.n4systems.reporting;

import java.util.Date;

import com.n4systems.util.persistence.search.BaseSearchDefiner;

public interface ReportDefiner extends BaseSearchDefiner {

	public String getSerialNumber();
	public String getRfidNumber();
	public String getOrderNumber();
	public String getPurchaseOrder();
	public Date getToDate();
	public Date getFromDate();
	public Long getProductType();
	public Long getInspectionBook();
	public Long getJobSite();
	public Long getInspectionTypeGroup();
	public Long getInspector();
	public Long getOwner();
}
