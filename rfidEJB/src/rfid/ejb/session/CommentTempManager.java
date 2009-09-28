package rfid.ejb.session;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import rfid.ejb.entity.CommentTempBean;

import com.n4systems.ejb.interceptor.TimingInterceptor;

@Interceptors({TimingInterceptor.class})
@Stateless 
public class CommentTempManager implements CommentTemp {
	@PersistenceContext (unitName="rfidEM")
	protected EntityManager em;
	
	
	@SuppressWarnings("unchecked")
	public List<CommentTempBean> findCommentTemplateByDate(Long tenantId, Date beginDate, Date endDate) {
		
		Query query = em.createQuery("from CommentTempBean ct where ct.tenant.id = :tenantId and "+
				"ct.dateModified >= :beginDate and ct.dateModified <= :endDate");
		
		query.setParameter("tenantId", tenantId);
		query.setParameter("beginDate", beginDate);
		query.setParameter("endDate", endDate);
		
		List<CommentTempBean> ctList = query.getResultList();
		
		
		
		return ctList;
	}


	


}
