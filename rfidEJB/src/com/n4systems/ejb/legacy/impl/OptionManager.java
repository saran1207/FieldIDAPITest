package com.n4systems.ejb.legacy.impl;


import java.util.Collection;
import java.util.Date;
import java.util.List;



import javax.persistence.EntityManager;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import rfid.ejb.entity.FindProductOptionBean;
import rfid.ejb.entity.FindProductOptionManufactureBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.Option;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.TagOption;
import com.n4systems.model.TagOption.OptionKey;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;


public class OptionManager implements Option {
	private Logger logger = Logger.getLogger(Option.class);
	
	
	protected EntityManager em;

	 private PersistenceManager persistenceManager;
	
	public OptionManager() {
	}
	
	public OptionManager(EntityManager em) {
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
	}
	
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
	
	public FindProductOptionBean getFindProductOption(String identifier) {
		Query query = em.createQuery("from FindProductOptionBean f where f.identifier = :identifier");
		query.setParameter("identifier", identifier);
		
		return (FindProductOptionBean)query.getSingleResult();
	}
	
	public List<TagOption> findTagOptions(SecurityFilter filter) {
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
		QueryBuilder<TagOption> builder = new QueryBuilder<TagOption>(TagOption.class, filter);
		
		TagOption tagOption = null;
		try {
			
			tagOption = persistenceManager.find(builder.addSimpleWhere("id", id));
			
		} catch(InvalidQueryException e) {
			logger.error("Unable to load TagOptions", e);
		}

		return tagOption;
	}
	
	public TagOption findTagOption(OptionKey optionKey, SecurityFilter filter) {
		QueryBuilder<TagOption> builder = new QueryBuilder<TagOption>(TagOption.class, filter);
		
		TagOption tagOption = null;
		try {
			
			tagOption = persistenceManager.find(builder.addSimpleWhere("optionKey", optionKey));
			
		} catch(InvalidQueryException e) {
			logger.error("Unable to load TagOptions", e);
		}

		return tagOption;
	}
}
