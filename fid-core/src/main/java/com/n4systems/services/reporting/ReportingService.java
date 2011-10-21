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
import com.n4systems.util.persistence.GroupByClause;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;

// CACHEABLE!!!  this is used for getting old data.

public class ReportingService extends FieldIdPersistenceService {

	
	// TODO DD : unit tests...
	
	@Autowired
	private PersistenceService persistenceService;
	
	@Transactional(readOnly = true)
    public ChartData<Calendar> getAssetsIdentified(Date from, BaseOrg org) {
		QueryBuilder<AssetsIdentifiedReportRecord> builder = new QueryBuilder<AssetsIdentifiedReportRecord>(Asset.class, securityContext.getUserSecurityFilter());
		builder.setSelectArgument(new NewObjectSelect(AssetsIdentifiedReportRecord.class, "YEAR(identified)", "QUARTER(identified)", "COUNT(*)"));
		builder.getGroupByArguments().add(new GroupByClause("YEAR(identified)", true));
		builder.getGroupByArguments().add(new GroupByClause("QUARTER(identified)", true));
		
		List<AssetsIdentifiedReportRecord> results = persistenceService.findAll(builder);
		        
        return new ChartData<Calendar>(results);
    }

}
