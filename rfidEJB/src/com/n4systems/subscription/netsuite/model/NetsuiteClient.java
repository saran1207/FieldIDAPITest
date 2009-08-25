package com.n4systems.subscription.netsuite.model;

import com.n4systems.subscription.Client;

public class NetsuiteClient extends Client {
	
	private Long nsrecordid;

	@Override
	public Long getExternalId() {
		return getNsrecordid();
	}

	public Long getNsrecordid() {
		return nsrecordid;
	}

	public void setNsrecordid(Long nsrecordid) {
		this.nsrecordid = nsrecordid;
	}
}
