package com.n4systems.reporting;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.search.BaseSearchDefiner;

import java.util.Date;

public interface ReportDefiner extends BaseSearchDefiner {

	public String getIdentifier();
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
