package com.n4systems.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.model.Inspection;
import com.n4systems.util.AggregateReport;
import com.n4systems.util.AggregateReportRecord;

@Interceptors({TimingInterceptor.class})
@Stateless
public class AggregateReportManagerImpl implements AggregateReportManager {
	@PersistenceContext (unitName="rfidEM")
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	public AggregateReport createAggregateReport(List<Long> inspectionIds) {
		if( inspectionIds == null || inspectionIds.size() == 0 ) {
			return null;
		}
		
		AggregateReport report = new AggregateReport();
		
		String groupByQuery = "select new " + AggregateReportRecord.class.getName() + "( count(inspection.id), inspection.product.type.name, " +
				"inspection.type.group.name, inspection.type.group.id ) from " + Inspection.class.getName() + " as inspection LEFT JOIN inspection.product LEFT JOIN inspection.product.type " +
				"LEFT JOIN inspection.type.group where inspection.id IN ( :inspections ) " +
				"GROUP BY inspection.product.type.name, " +
				"inspection.type.group.name, inspection.type.group.id " +
				"ORDER BY UPPER(inspection.product.type.name), UPPER(inspection.type.group.name) ";
		try {
		Query query = em.createQuery( groupByQuery );
		query.setParameter( "inspections", inspectionIds );
		
		report.setInspectionTypeGroupsByProductTypes( query.getResultList() );
		} catch (Throwable e) {
			e.printStackTrace();// TODO: handle exception
		}
		
		String distinctProducts = "select new " + AggregateReportRecord.class.getName() + "( count( DISTINCT product.id), product.type.name ) " +
			" from " + Inspection.class.getName() + " as inspection LEFT JOIN inspection.product as product LEFT JOIN product.type" +
			" WHERE inspection.id IN ( :inspections ) " +
			" GROUP BY product.type.name  ";
		try {
		Query distinctProductQuery = em.createQuery( distinctProducts );
		distinctProductQuery.setParameter( "inspections", inspectionIds );
			report.setDistinctProductsByProductType( distinctProductQuery.getResultList() );
		} catch (Throwable e) {
			e.printStackTrace();// TODO: handle exception
		}
		return report;
	}

}
