package rfid.ejb.session;


import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import rfid.ejb.entity.FindProductOptionBean;
import rfid.ejb.entity.FindProductOptionManufactureBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.TagOption;
import com.n4systems.model.TagOption.OptionKey;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

@Interceptors({TimingInterceptor.class})
@Stateless
public class OptionManager implements Option {
	private Logger logger = Logger.getLogger(Option.class);
	
	@PersistenceContext (unitName="rfidEM")
	protected EntityManager em;

	@EJB private PersistenceManager persistenceManager;
	
	@Deprecated
	public Collection<FindProductOptionManufactureBean> getFindProductOptionsForManufacture(Long tenantId) {
		return getFindProductOptionsForTenant(tenantId);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<FindProductOptionManufactureBean>	getFindProductOptionsForTenant(Long tenantId) {
		Query query = em.createQuery("from FindProductOptionManufactureBean fom where fom.tenant.id = :tenantId");
		query.setParameter("tenantId", tenantId);
		
		return (Collection<FindProductOptionManufactureBean>)query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<FindProductOptionManufactureBean>	getAllFindProductOptionManufacture() {
		Query query = em.createQuery("from FindProductOptionManufactureBean fpomb");
		return query.getResultList();		
	}
	
	public FindProductOptionManufactureBean getFindProductOptionManufacture(Long uniqueID){
		FindProductOptionManufactureBean obj = em.find(FindProductOptionManufactureBean.class, uniqueID);
		return obj;
	}
	
	public void updateFindProductOptionManufacture(FindProductOptionManufactureBean findProductOptionManufacturer) {
		findProductOptionManufacturer.setDateModified(new Date());
		em.merge(findProductOptionManufacturer);
	}

	public Long persistFindProductOptionManufacture(FindProductOptionManufactureBean findProductOptionManufacturer) {
		findProductOptionManufacturer.setDateModified(new Date());
		em.persist(findProductOptionManufacturer);
		return findProductOptionManufacturer.getUniqueID();
	}
	
	@SuppressWarnings("unchecked")
	public Collection<FindProductOptionBean> getAllFindProductOptions() {
		return em.createQuery("from FindProductOptionBean f").getResultList();
	}
	
	public FindProductOptionBean getFindProductOption(Long uniqueID) {
		return em.find(FindProductOptionBean.class, uniqueID);
	}
	
	public FindProductOptionBean getFindProductOption(String key) {
		Query query = em.createQuery("from FindProductOptionBean f where f.key = :key");
		query.setParameter("key", key);
		
		return (FindProductOptionBean)query.getSingleResult();
	}
	
	public List<TagOption> findTagOptions(SecurityFilter filter) {
		filter.setTargets("tenant.id");
		
		QueryBuilder<TagOption> builder = new QueryBuilder<TagOption>(TagOption.class, filter);
		
		List<TagOption> tagOptions = null;
		try {
		
			tagOptions = builder.setSimpleSelect().addOrder("weight").getResultList(em);	
		
		} catch(InvalidQueryException e) {
			logger.error("Unable to load TagOptions", e);
		}

		return tagOptions;
	}
	
	public TagOption findTagOption(Long id, SecurityFilter filter) {
		filter.setTargets("tenant.id");
		
		QueryBuilder<TagOption> builder = new QueryBuilder<TagOption>(TagOption.class, filter);
		
		TagOption tagOption = null;
		try {
			
			tagOption = persistenceManager.find(builder.addSimpleWhere("id", id));
			
		} catch(InvalidQueryException e) {
			logger.error("Unable to load TagOptions", e);
		}

		return tagOption;
	}
	
	public TagOption findTagOption(OptionKey key, SecurityFilter filter) {
		filter.setTargets("tenant.id");
		
		QueryBuilder<TagOption> builder = new QueryBuilder<TagOption>(TagOption.class, filter);
		
		TagOption tagOption = null;
		try {
			
			tagOption = persistenceManager.find(builder.addSimpleWhere("key", key));
			
		} catch(InvalidQueryException e) {
			logger.error("Unable to load TagOptions", e);
		}

		return tagOption;
	}
}
