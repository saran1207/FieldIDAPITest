package com.n4systems.services.reporting;

import java.util.Calendar;
import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.FlotData;
import com.n4systems.util.FlotDataSet;

// CACHEABLE!!!  this is used for getting old data.

public class ReportingService extends FieldIdPersistenceService {

	private int DATA_SIZE = 50;
	
    @Transactional(readOnly = true)
    public FlotDataSet getAssetsIdentified(Date from, Date to, BaseOrg org) {
    	// bogus data for now...
    	
    	Calendar yearsAgo = Calendar.getInstance();
    	yearsAgo.set(Calendar.YEAR, 2011);
    	    	
    	FlotDataSet set = new FlotDataSet();
    	for (int i = 0; i<DATA_SIZE; i++) { 
    		long x = yearsAgo.getTimeInMillis();
    		yearsAgo.add(Calendar.DATE,1);
    		double y = Math.floor(Math.random()*10);
    		set.add(new FlotData(x,y));
    	}
    	
    	return set;
    }

}
