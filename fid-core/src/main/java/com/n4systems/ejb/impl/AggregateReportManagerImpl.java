package com.n4systems.ejb.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.ejb.AggregateReportManager;
import com.n4systems.model.Event;
import com.n4systems.util.AggregateReport;
import com.n4systems.util.AggregateReportRecord;


public class AggregateReportManagerImpl implements AggregateReportManager {
	
	private EntityManager em;

	public AggregateReportManagerImpl() {
	}

	public AggregateReportManagerImpl(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	public AggregateReport createAggregateReport(List<Long> eventIds) {
		if( eventIds == null || eventIds.size() == 0 ) {
			return null;
		}
		
		AggregateReport report = new AggregateReport();
		
		String groupByQuery = "select new " + AggregateReportRecord.class.getName() + "( count(event.id), event.asset.type.name, " +
				"event.type.group.name, event.type.group.id ) from " + Event.class.getName() + " as event LEFT JOIN event.asset LEFT JOIN event.asset.type " +
				"LEFT JOIN event.type.group where event.id IN ( :eventIds ) " +
				"GROUP BY event.asset.type.name, " +
				"event.type.group.name, event.type.group.id " +
				"ORDER BY UPPER(event.asset.type.name), UPPER(event.type.group.name) ";
		Query query = em.createQuery( groupByQuery );
		query.setParameter( "eventIds", eventIds );
		
		report.setEventTypeGroupsByAssetTypes( query.getResultList() );
		
		
		String distinctAssets = "select new " + AggregateReportRecord.class.getName() + "( count( DISTINCT asset.id), asset.type.name ) " +
			" from " + Event.class.getName() + " as event LEFT JOIN event.asset as asset LEFT JOIN asset.type" +
			" WHERE event.id IN ( :eventIds ) " +
			" GROUP BY asset.type.name  ";
		Query distinctAssetQuery = em.createQuery( distinctAssets );
		distinctAssetQuery.setParameter( "eventIds", eventIds );
		report.setDistinctAssetsByAssetType( distinctAssetQuery.getResultList() );
		
		return report;
	}

}
