package rfid.ejb.session;

import java.util.Collection;

import com.n4systems.model.Organization;

import rfid.ejb.entity.SerialNumberCounterBean;

public interface SerialNumberCounter {

	public void updateSerialNumberCounter(SerialNumberCounterBean serialNumberCounter);	
	public void persistSerialNumberCounter(SerialNumberCounterBean serialNumberCounter);
	public Collection<SerialNumberCounterBean> getSerialNumberCounters();	
	public SerialNumberCounterBean getSerialNumberCounter(Long tenantId);
	public String getNextCounterValue(Long tenantId);
	public String generateSerialNumber(Long tenantId);
	public String generateSerialNumber(Organization tenant);
	
}
