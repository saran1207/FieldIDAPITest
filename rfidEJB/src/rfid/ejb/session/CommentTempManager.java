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
import com.n4systems.util.ListingPair;

@Interceptors({TimingInterceptor.class})
@Stateless 
public class CommentTempManager implements CommentTemp {
	@PersistenceContext (unitName="rfidEM")
	protected EntityManager em;
	
	@SuppressWarnings("unchecked")
	public ArrayList<CommentTempDTO> findAllCommentTemps() {
		List<CommentTempBean> commentTemplates = (List<CommentTempBean>)em.createQuery("from CommentTempBean ct order by ct.templateID").getResultList();

		ArrayList<CommentTempDTO> commentTemplateDTOs = new ArrayList<CommentTempDTO>();
		commentTemplateDTOs.add(new CommentTempDTO(null, null, null, null, null, null, null));
		
		for(CommentTempBean commentTemplate: commentTemplates) {
			commentTemplateDTOs.add(createDTO(commentTemplate));
		}
		
		return commentTemplateDTOs;
	}
	
	public ArrayList<CommentTempDTO> findCommentTempsByRMan(Long tenantId) {
		return findCommentTempsForTenant(tenantId);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<CommentTempDTO> findCommentTempsForTenant(Long tenantId) {
		Query query = em.createQuery("from CommentTempBean ct where ct.tenant.id = :tenantId");
		query.setParameter("tenantId", tenantId);
		
		ArrayList<CommentTempDTO> ctArrayList = new ArrayList<CommentTempDTO>();
		for(CommentTempBean commentTempBean: (List<CommentTempBean>)query.getResultList()) {
			ctArrayList.add(new CommentTempDTO(
					commentTempBean.getUniqueID(), 
					commentTempBean.getDateCreated(), 
					commentTempBean.getDateModified(), 
					commentTempBean.getModifiedBy(), 
					commentTempBean.getTemplateID(), 
					commentTempBean.getContents(), 
					commentTempBean.getTenant().getId()
					));
		}

		return ctArrayList;
	}
	
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

	public CommentTempBean findCommentTemplate(Long uniqueID) {
		return em.find(CommentTempBean.class, uniqueID);
	}

	@SuppressWarnings("unchecked")
	public List<CommentTempBean> findCommentTemplates(Long tenantId) {
		Query query = em.createQuery("from CommentTempBean ct where ct.tenant.id = :tenantId order by ct.templateID");
		query.setParameter("tenantId", tenantId);
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<ListingPair> findCommentTemplatesLP(Long tenantId) {
		Query query = em.createQuery("select new com.n4systems.util.ListingPair( ct.uniqueID, ct.templateID ) from CommentTempBean ct where ct.tenant.id = :tenantId order by ct.templateID");
		query.setParameter("tenantId", tenantId);
		
		return query.getResultList();
	}

	public Long persistCommentTemplate(CommentTempBean commentTempBean) {
		commentTempBean.setDateCreated(new Date());
		commentTempBean.setDateModified(commentTempBean.getDateCreated());
		em.persist(commentTempBean);
		return commentTempBean.getUniqueID();
	}

	public void updateCommentTemplate(CommentTempBean commentTempBean) {
		commentTempBean.setDateModified(commentTempBean.getDateCreated());
		em.merge(commentTempBean);
	}
	
	public void removeCommentTemplate(CommentTempBean commentTempBean) {
		em.remove(em.merge(commentTempBean));
	}
	

}
