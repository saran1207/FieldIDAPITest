package com.n4systems.services.reporting;

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
    public ChartData getAssetsIdentified(Date from, BaseOrg org) {
//		String query = "select new " + AssetsIdentifiedReportRecord.class.getName() +  " ( " +   
//			"	YEAR(identified), " +  
//			"	QUARTER(identified), " +  
//			"	SUM(1) as q  ) " +
//			" from " + Asset.class.getName() + 
//			"  where tenant.id = :tenantId " + 
//			" 	and state = 'ACTIVE' " + 
//			" 	and identified > 0 " + 
////			"   and identifiedBy.id in (:users) " + 
////			" 	and owner.id in (:ownerIds) " + 
//// " order by year, quarter"		
//			" group by YEAR(identified), QUARTER(identified)";  
		

		
		// FIXME DD : put date, tenantId, ownerId's, etc... in params.
//		Map<String, Object> params = Maps.newHashMap();
//		params.put("tenantId", securityContext.getUserSecurityFilter().getTenantId());
////		params.put("ownerIds", Lists.newArrayList(15536284L,15536163L,15511493L,15526015L,15536162L));
////		params.put("users", Lists.newArrayList(310381L,310451L,310475L,310452L,311222L,311162L,15513566L,5513793L,15513031L));
//		List<Chartable> results = (List<Chartable>) persistenceService.runQuery(query, params);
    	
		QueryBuilder<AssetsIdentifiedReportRecord> builder = new QueryBuilder<AssetsIdentifiedReportRecord>(Asset.class, securityContext.getUserSecurityFilter());
		builder.setSelectArgument(new NewObjectSelect(AssetsIdentifiedReportRecord.class, "YEAR(identified)", "QUARTER(identified)", "COUNT(*)"));
		builder.getGroupByArguments().add(new GroupByClause("YEAR(identified)", true));
		builder.getGroupByArguments().add(new GroupByClause("QUARTER(identified)", true));
		
		List<AssetsIdentifiedReportRecord> results = persistenceService.findAll(builder);
		
		// just to show that lazy loading works.
		try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			// FIXME DD : handle correctly.
			e.printStackTrace();
		}
		
        return new ChartData(results).normalize(ChartDataPeriod.QUARTERLY);
    }     
    

	private ChartData makeBogusDataSet() {
		return new ChartData(10,12, 22, 33, 84,96);
	}

}
