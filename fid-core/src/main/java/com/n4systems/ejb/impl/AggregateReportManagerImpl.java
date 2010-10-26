package com.n4systems.ejb.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.ejb.AggregateReportManager;
import com.n4systems.model.Inspection;
import com.n4systems.util.AggregateReport;
import com.n4systems.util.AggregateReportRecord;


public class AggregateReportManagerImpl implements AggregateReportManager {
	
	private EntityManager em;
	
	
	
	public AggregateReportManagerImpl() {
		super();
	}


	public AggregateReportManagerImpl(EntityManager em) {
		super();
		this.em = em;
	}



	@SuppressWarnings("unchecked")
	public AggregateReport createAggregateReport(List<Long> inspectionIds) {
		if( inspectionIds == null || inspectionIds.size() == 0 ) {
			return null;
		}
		
		AggregateReport report = new AggregateReport();
		
		String groupByQuery = "select new " + AggregateReportRecord.class.getName() + "( count(inspection.id), inspection.asset.type.name, " +
				"inspection.type.group.name, inspection.type.group.id ) from " + Inspection.class.getName() + " as inspection LEFT JOIN inspection.asset LEFT JOIN inspection.asset.type " +
				"LEFT JOIN inspection.type.group where inspection.id IN ( :inspections ) " +
				"GROUP BY inspection.asset.type.name, " +
				"inspection.type.group.name, inspection.type.group.id " +
				"ORDER BY UPPER(inspection.asset.type.name), UPPER(inspection.type.group.name) ";
		Query query = em.createQuery( groupByQuery );
		query.setParameter( "inspections", inspectionIds );
		
		report.setInspectionTypeGroupsByAssetTypes( query.getResultList() );
		
		
		String distinctAssets = "select new " + AggregateReportRecord.class.getName() + "( count( DISTINCT asset.id), asset.type.name ) " +
			" from " + Inspection.class.getName() + " as inspection LEFT JOIN inspection.asset as asset LEFT JOIN asset.type" +
			" WHERE inspection.id IN ( :inspections ) " +
			" GROUP BY asset.type.name  ";
		Query distinctAssetQuery = em.createQuery( distinctAssets );
		distinctAssetQuery.setParameter( "inspections", inspectionIds );
		report.setDistinctAssetsByAssetType( distinctAssetQuery.getResultList() );
		
		return report;
	}

}
