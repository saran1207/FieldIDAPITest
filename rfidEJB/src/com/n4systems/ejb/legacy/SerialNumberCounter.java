package com.n4systems.ejb.legacy;

import java.util.Collection;

import rfid.ejb.entity.SerialNumberCounterBean;

import com.n4systems.model.orgs.PrimaryOrg;

public interface SerialNumberCounter {

	public void updateSerialNumberCounter(SerialNumberCounterBean serialNumberCounter);	
	
	public Collection<SerialNumberCounterBean> getSerialNumberCounters();	
	public SerialNumberCounterBean getSerialNumberCounter(Long tenantId);
	public String getNextCounterValue(Long tenantId);
	public String generateSerialNumber(PrimaryOrg primaryOrg);
	
}
