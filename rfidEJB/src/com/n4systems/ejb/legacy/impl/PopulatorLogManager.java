package com.n4systems.ejb.legacy.impl;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import rfid.ejb.PopulatorCriteria;
import rfid.ejb.entity.PopulatorLogBean;

import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;

 
public class PopulatorLogManager implements PopulatorLog {
	
	protected EntityManager em;
	
	

	public PopulatorLogManager(EntityManager em) {
		this.em = em;
	}

	
	public Pager<PopulatorLogBean> findPopulatorLogBySearch(Long tenantId, PopulatorCriteria criteria, int pageNumber, int pageSize) {

		StringBuffer sqlString = new StringBuffer();
		sqlString.append("from PopulatorLogBean pl where pl.tenant.id = :tenantId ");
		StringBuffer countString = new StringBuffer();
		countString.append("SELECT count(*) from PopulatorLogBean pl where pl.tenant.id = :tenantId ");
		
		// Now add on each appropriate filter based on the criteria
		if (criteria.getLogType() != null) {
			sqlString.append(" AND logType = :logType ");
			countString.append(" AND logType = :logType ");
		} 
		if (criteria.getLogStatus() != null) {
			sqlString.append(" AND logStatus = :logStatus ");
			countString.append(" AND logStatus = :logStatus ");
		} 
		if (criteria.getFromDate() != null) {
			sqlString.append(" AND pl.timeLogged >= :fromDate ");
			countString.append(" AND pl.timeLogged >= :fromDate ");
		}
		if (criteria.getToDate() != null) {
			sqlString.append(" AND pl.timeLogged <= :toDate ");
			countString.append(" AND pl.timeLogged <= :toDate ");
		}		
		sqlString.append(" ORDER BY pl.timeLogged DESC");
		
		Query query = em.createQuery(sqlString.toString());
		query.setParameter("tenantId", tenantId);
		
		Query countQuery = em.createQuery(countString.toString());
		countQuery.setParameter("tenantId", tenantId);

		// now fill in the appropriate values
		if (criteria.getLogType() != null) {
			query.setParameter("logType", criteria.getLogType());
			countQuery.setParameter("logType", criteria.getLogType());
		} 
		if (criteria.getLogStatus() != null) {
			query.setParameter("logStatus", criteria.getLogStatus());
			countQuery.setParameter("logStatus", criteria.getLogStatus());
		} 
		if (criteria.getFromDate() != null) {
			query.setParameter("fromDate", criteria.getFromDate());
			countQuery.setParameter("fromDate", criteria.getFromDate());
		}
		if (criteria.getToDate() != null) {
			query.setParameter("toDate", criteria.getToDate());
			countQuery.setParameter("toDate", criteria.getToDate());
		}
		
		return new Page<PopulatorLogBean>(query, countQuery, pageNumber, pageSize); 		
	}

	

	public Long createPopulatorLog(PopulatorLogBean bean) {
		PopulatorLogBean obj = new PopulatorLogBean();
		obj.setTenant(bean.getTenant());
		obj.setLogMessage(bean.getLogMessage());
		obj.setLogStatus(bean.getLogStatus());
		obj.setLogType(bean.getLogType());
		obj.setTimeLogged(new Date());
		em.persist(obj);
		return obj.getUniqueID();
	}



}
