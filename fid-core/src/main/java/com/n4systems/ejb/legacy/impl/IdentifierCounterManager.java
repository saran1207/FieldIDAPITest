package com.n4systems.ejb.legacy.impl;

import com.n4systems.ejb.legacy.IdentifierCounter;
import com.n4systems.model.AssetType;
import com.n4systems.model.orgs.PrimaryOrg;
import rfid.ejb.entity.IdentifierCounterBean;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class IdentifierCounterManager implements IdentifierCounter {

	protected EntityManager em;

	public IdentifierCounterManager(EntityManager em) {
		super();
		this.em = em;
	}

	public void updateIdentifierCounter(IdentifierCounterBean identifierCounter) {
		em.merge(identifierCounter);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<IdentifierCounterBean> getIdentifierCounters() {
		return (Collection<IdentifierCounterBean>)em.createQuery("from "+IdentifierCounterBean.class.getName()+" snc").getResultList();
	}
	
	public IdentifierCounterBean getIdentifierCounter(Long tenantId) {
		Query query = em.createQuery("from "+IdentifierCounterBean.class.getName()+" snc where snc.tenant.id = :tenantId");
		query.setParameter("tenantId", tenantId);
		
		return (IdentifierCounterBean)query.getSingleResult();
	}
	
	/**
	 * Gets the next counter for a tenants serial number, formatted as a string
	 * based on their decimal format specified.  This is synchronized to ensure multiple
	 * users won't get the same counter value.
	 */
	public synchronized String getNextCounterValue(Long tenantId) {
		IdentifierCounterBean identifierCounter = getIdentifierCounter(tenantId);
		
		DecimalFormat decimalFormat = new DecimalFormat(identifierCounter.getDecimalFormat());
		
		Long counterValue = identifierCounter.getCounter();
		String counterValueString = decimalFormat.format(counterValue);
		
		// increment to the next one
		identifierCounter.setCounter(counterValue+1);
		
		return counterValueString;
	}
	
	public String generateIdentifier(PrimaryOrg primaryOrg, AssetType assetType) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		
		DecimalFormat padTwoSpaces = new DecimalFormat("00");
		DecimalFormat padThreeSpaces = new DecimalFormat("000");
		
		String identifier = primaryOrg.getIdentifierFormat();

        if (assetType != null && assetType.isIdentifierOverridden()) {
            identifier = assetType.getIdentifierFormat();
        }

		// go through and replace the appropriate symbols with their meaning
		identifier = identifier.replaceAll("%m", padTwoSpaces.format(calendar.get(Calendar.MONTH)+1));
		identifier = identifier.replaceAll("%d", padTwoSpaces.format(calendar.get(Calendar.DAY_OF_MONTH)));
		identifier = identifier.replaceAll("%Y", new Integer(calendar.get(Calendar.YEAR)).toString());
		identifier = identifier.replaceAll("%y", new Integer(calendar.get(Calendar.YEAR)).toString().substring(2));
		identifier = identifier.replaceAll("%H", padTwoSpaces.format(calendar.get(Calendar.HOUR_OF_DAY)));
		identifier = identifier.replaceAll("%M", padTwoSpaces.format(calendar.get(Calendar.MINUTE)));
		identifier = identifier.replaceAll("%S", padTwoSpaces.format(calendar.get(Calendar.SECOND)));
		identifier = identifier.replaceAll("%L", padThreeSpaces.format(calendar.get(Calendar.MILLISECOND)));
		identifier = identifier.replaceAll("%j", deriveJergensDateCode(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)));
		
		// see if we need to do the counter
		if (identifier.indexOf("%g") >= 0) {
			identifier = identifier.replaceAll("%g", getNextCounterValue(primaryOrg.getTenant().getId()));
		}

		return identifier;
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
