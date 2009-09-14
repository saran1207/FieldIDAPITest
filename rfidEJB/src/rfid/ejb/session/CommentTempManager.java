package rfid.ejb.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import rfid.dto.CommentTempDTO;
import rfid.ejb.entity.CommentTempBean;

import com.n4systems.ejb.interceptor.TimingInterceptor;

@Interceptors({TimingInterceptor.class})
@Stateless 
public class CommentTempManager implements CommentTemp {
	@PersistenceContext (unitName="rfidEM")
	protected EntityManager em;
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<CommentTempDTO> findCommentTemplateByDate(Long tenantId, Date beginDate, Date endDate) {
		
		Query query = em.createQuery("from CommentTempBean ct where ct.tenant.id = :tenantId and "+
				"ct.dateModified >= :beginDate and ct.dateModified <= :endDate");
		
		query.setParameter("tenantId", tenantId);
		query.setParameter("beginDate", beginDate);
		query.setParameter("endDate", endDate);
		
		List<CommentTempBean> ctList = query.getResultList();
		
		ArrayList<CommentTempDTO> ctArrayList = new ArrayList<CommentTempDTO>();
		for (Iterator<CommentTempBean> i = ctList.iterator(); i.hasNext(); ) {
			ctArrayList.add(createDTO(i.next()));
		}
		
		return ctArrayList;
	}

	private CommentTempDTO createDTO(CommentTempBean obj) {
	    if (obj == null) return null;
	    return new CommentTempDTO(obj.getUniqueID(), obj.getDateCreated(), obj.getDateModified(),
	                           obj.getModifiedBy(), obj.getTemplateID(), obj.getContents(), obj.getTenant().getId());
	}

	

	


}
