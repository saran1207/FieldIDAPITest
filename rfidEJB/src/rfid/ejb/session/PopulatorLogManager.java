package rfid.ejb.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import rfid.ejb.PopulatorCriteria;
import rfid.ejb.entity.PopulatorLogBean;

import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;

@Interceptors({TimingInterceptor.class})
@Stateless 
public class PopulatorLogManager implements PopulatorLog {
	@PersistenceContext (unitName="rfidEM")
	protected EntityManager em;
	
	@SuppressWarnings("unchecked")
	public List<PopulatorLogBean> findAllPopulatorLog() {
		return (List<PopulatorLogBean>)em.createQuery("from PopulatorLogBean pl order by pl.uniqueID desc").getResultList();
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

	public void removeAllMessages() {
		String msgType = "Message";
		em.createQuery("delete from PopulatorLogBean as pl where pl.logType = :msgType")
				.setParameter(PopulatorLog.logType.datapopulator.toString(), msgType).executeUpdate();
	}
	
	public void removeAllLogs() {
		em.createQuery("delete from PopulatorLogBean")
				.executeUpdate();
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

	public void removePopulatorLog(Long uniqueID) {
		PopulatorLogBean obj = em.find(PopulatorLogBean.class, uniqueID);
		em.remove(obj);
	}
	
	// XXX - this appears to be broken
	public ArrayList<String> findAllLogTypes(long tenantId){
		
		ArrayList<String> allMessageTypesAsString = new ArrayList<String>();
		for (logType logType : PopulatorLogManager.logType.values()){
			allMessageTypesAsString.add(logType.toString());
		}
		return allMessageTypesAsString;
	}
	
	// XXX - this appears to be broken
	public ArrayList<String> findAllLogStatuses(long tenantId){

		ArrayList<String> allStatusAsString = new ArrayList<String>();
		for (logStatus logStatus : PopulatorLogManager.logStatus.values()){
			allStatusAsString.add(logStatus.toString());
		}
		return allStatusAsString;		
	}

}
