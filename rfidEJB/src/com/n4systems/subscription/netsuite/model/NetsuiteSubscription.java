package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.Subscription;

public class NetsuiteSubscription extends Subscription {

	private Long netsuiteRecordId;
	private Long nsrecordid;

	@Override
	public Long getExternalId() {
		return getNsrecordid();
	}
	
	@Override
	public void setExternalId(Long externalId) {
		setNetsuiteRecordId(externalId);
	}
	
	public Long getNetsuiteRecordId() {
		return netsuiteRecordId;
	}

	public void setNetsuiteRecordId(Long netsuiteRecordId) {
		this.netsuiteRecordId = netsuiteRecordId;
	}

	public Long getNsrecordid() {
		return nsrecordid;
	}

	public void setNsrecordid(Long nsrecordid) {
		this.nsrecordid = nsrecordid;
	}
}
