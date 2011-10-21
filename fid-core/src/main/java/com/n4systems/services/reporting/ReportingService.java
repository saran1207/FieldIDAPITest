package com.n4systems.services.reporting;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.ChartData;
import com.n4systems.util.chart.ChartDataPeriod;
import com.n4systems.util.chart.Chartable;
import com.n4systems.util.persistence.GroupByClause;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;

// CACHEABLE!!!  this is used for getting old data.

public class ReportingService extends FieldIdPersistenceService {

	
	// TODO DD : unit tests...
	
	@Autowired
	private PersistenceService persistenceService;
	
    @SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
    public ChartData<Calendar> getAssetsIdentified(Date from, BaseOrg org) {
		QueryBuilder<AssetsIdentifiedReportRecord> builder = new QueryBuilder<AssetsIdentifiedReportRecord>(Asset.class, securityContext.getUserSecurityFilter());
		builder.setSelectArgument(new NewObjectSelect(AssetsIdentifiedReportRecord.class, "YEAR(identified)", "QUARTER(identified)", "COUNT(*)"));
		builder.getGroupByArguments().add(new GroupByClause("YEAR(identified)", true));
		builder.getGroupByArguments().add(new GroupByClause("QUARTER(identified)", true));
		
		List<AssetsIdentifiedReportRecord> results = persistenceService.findAll(builder);
		
		// just to show that lazy loading works.
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// FIXME DD : handle correctly.
			e.printStackTrace();
		}
		        
        return new ChartData<Calendar>(results);
//        return normalize( new ChartData<Calendar>(results), ChartDataPeriod.QUARTERLY);
    }
    
    private ChartData<Calendar> normalize(ChartData<Calendar> data , ChartDataPeriod period) {
		return normalize(data, period, 0);
	}

	private ChartData<Calendar> normalize(ChartData<Calendar> data, ChartDataPeriod period, Number value) {
    	if (data.isEmpty()) {		
    		return data;
    	}
		Calendar start = data.getFirstX(); 
		Calendar end = data.getLastX();
		
		for (Calendar time = start; time.compareTo(end)<=0; time = period.nextPeriod(time)) {
			Chartable<Calendar> p = data.get(time);
			if (p==null) { 
				data.add(new AssetsIdentifiedReportRecord(time,value.longValue()));
			}
			System.out.println(data.get(time));
		}
		return data;
    }


}
