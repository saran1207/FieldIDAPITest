package com.n4systems.reporting;

import java.util.Date;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.search.BaseSearchDefiner;

public interface ReportDefiner extends BaseSearchDefiner {

	public String getSerialNumber();
	public String getRfidNumber();
	public String getOrderNumber();
	public String getPurchaseOrder();
	public Date getToDate();
	public Date getFromDate();
	public Long getAssetType();
	public Long getEventBook();
	public Long getEventTypeGroup();
	public Long getPerformedBy();
	public BaseOrg getOwner();
}
