package com.n4systems.ejb.legacy.impl;

import com.n4systems.ejb.legacy.SerialNumberCounter;
import com.n4systems.model.AssetType;
import com.n4systems.model.orgs.PrimaryOrg;
import rfid.ejb.entity.SerialNumberCounterBean;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;


public class SerialNumberCounterManager implements SerialNumberCounter {

	
	protected EntityManager em;
	
	

	public SerialNumberCounterManager(EntityManager em) {
		super();
		this.em = em;
	}

	public void updateSerialNumberCounter(SerialNumberCounterBean serialNumberCounter) {
		em.merge(serialNumberCounter);
	}
	
	
	@SuppressWarnings("unchecked")
	public Collection<SerialNumberCounterBean> getSerialNumberCounters() {
		return (Collection<SerialNumberCounterBean>)em.createQuery("from SerialNumberCounterBean snc").getResultList();
	}
	
	public SerialNumberCounterBean getSerialNumberCounter(Long tenantId) {
		Query query = em.createQuery("from SerialNumberCounterBean snc where snc.tenant.id = :tenantId");
		query.setParameter("tenantId", tenantId);
		
		return (SerialNumberCounterBean)query.getSingleResult();
	}
	
	/**
	 * Gets the next counter for a tenants serial number, formatted as a string
	 * based on their decimal format specified.  This is synchronized to ensure multiple
	 * users won't get the same counter value.
	 */
	public synchronized String getNextCounterValue(Long tenantId) {
		SerialNumberCounterBean serialNumberCounter = getSerialNumberCounter(tenantId);
		
		DecimalFormat decimalFormat = new DecimalFormat(serialNumberCounter.getDecimalFormat());
		
		Long counterValue = serialNumberCounter.getCounter();
		String counterValueString = decimalFormat.format(counterValue);
		
		// increment to the next one
		serialNumberCounter.setCounter(counterValue+1);
		
		return counterValueString;
	}
	
	public String generateSerialNumber(PrimaryOrg primaryOrg, AssetType assetType) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		
		DecimalFormat padTwoSpaces = new DecimalFormat("00");
		DecimalFormat padThreeSpaces = new DecimalFormat("000");
		
		String serialNumber = primaryOrg.getIdentifierFormat();

        if (assetType != null && assetType.isIdentifierOverridden()) {
            serialNumber = assetType.getIdentifierFormat();
        }

		// go through and replace the appropriate symbols with their meaning
		serialNumber = serialNumber.replaceAll("%m", padTwoSpaces.format(calendar.get(Calendar.MONTH)+1));
		serialNumber = serialNumber.replaceAll("%d", padTwoSpaces.format(calendar.get(Calendar.DAY_OF_MONTH)));
		serialNumber = serialNumber.replaceAll("%Y", new Integer(calendar.get(Calendar.YEAR)).toString());
		serialNumber = serialNumber.replaceAll("%y", new Integer(calendar.get(Calendar.YEAR)).toString().substring(2));
		serialNumber = serialNumber.replaceAll("%H", padTwoSpaces.format(calendar.get(Calendar.HOUR_OF_DAY)));
		serialNumber = serialNumber.replaceAll("%M", padTwoSpaces.format(calendar.get(Calendar.MINUTE)));
		serialNumber = serialNumber.replaceAll("%S", padTwoSpaces.format(calendar.get(Calendar.SECOND)));
		serialNumber = serialNumber.replaceAll("%L", padThreeSpaces.format(calendar.get(Calendar.MILLISECOND)));
		serialNumber = serialNumber.replaceAll("%j", deriveJergensDateCode(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)));
		
		// see if we need to do the counter
		if (serialNumber.indexOf("%g") >= 0) {
			serialNumber = serialNumber.replaceAll("%g", getNextCounterValue(primaryOrg.getTenant().getId()));
		}

		return serialNumber;
	}
	
	/**
	 * Returns a Jergens style date code given the month and 4 digit year
	 * @param month
	 * @param year
	 * @return
	 */
	private String deriveJergensDateCode(int month, int year) {
		// Map month (January = A, Feb = B, etc.)
		char monthCode = 'A';
		for (int i=1; i<=month; i++) {
			monthCode++;
		}
		
		// Map year (1993 = A, 1994 = B, 1995 = C.. etc)
		char yearCode = 'A';
		for (int i=1993; i<=year; i++) {
			yearCode++;
		}
		
		String dateCode = String.valueOf(monthCode)+String.valueOf(yearCode);
		
		return dateCode;
	}
	
}
