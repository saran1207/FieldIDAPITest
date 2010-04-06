package com.n4systems.ejb.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ejb.AutoAttributeManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;


public class AutoAttributeManagerImpl implements AutoAttributeManager {

	
	private EntityManager em;

	
	private PersistenceManager persistenceManager;
	public AutoAttributeManagerImpl() { }

	public AutoAttributeManagerImpl(EntityManager em) {
		this.em = em;
	}

	public Pager<AutoAttributeDefinition> findAllPage(AutoAttributeCriteria criteria, Tenant tenant, Integer pageNumber, Integer pageSize) {

		String lookup = "from AutoAttributeDefinition def where def.criteria = :criteria and def.tenant = :tenant";
		Query query = em.createQuery(lookup + " ORDER BY def.id");
		Query countQuery = em.createQuery("SELECT count(*) " + lookup);

		countQuery.setParameter("criteria", criteria);
		query.setParameter("criteria", criteria);

		countQuery.setParameter("tenant", tenant);
		query.setParameter("tenant", tenant);

		return new Page<AutoAttributeDefinition>(query, countQuery, pageNumber, pageSize);

	}

	public AutoAttributeDefinition findTemplateToApply(ProductType productType, Collection<InfoOptionBean> selectedInfoOptions) {
		// make sure the product type is attached.
		ProductType pt = em.find(ProductType.class, productType.getId());
		return findTemplateToApply(pt.getAutoAttributeCriteria(), selectedInfoOptions);
	}

	public AutoAttributeDefinition findTemplateToApply(AutoAttributeCriteria criteria, Collection<InfoOptionBean> selectedInfoOptions) {
		if (criteria == null || criteria.getInputs().size() > selectedInfoOptions.size()) {
			return null; // no way to find a template. if the number of
							// inputs is less than the inputs in the
		}

		String queryString = "SELECT def.id from AutoAttributeDefinition def, IN (def.inputs) io WHERE def.criteria = :criteria ";

		if (selectedInfoOptions.size() > 0) {
			queryString += "AND ( ";
			for (int i = 0; i < selectedInfoOptions.size(); i++) {
				if (i != 0) {
					queryString += "OR ";
				}
				queryString += "io.uniqueID = :infoOptionId" + i + " ";
			}
			queryString += " ) ";
		}

		queryString += "group by def.id having count(io) = :inputSize ";

		Query query = em.createQuery(queryString);

		query.setParameter("criteria", criteria);
		query.setParameter("inputSize", Long.valueOf(criteria.getInputs().size()));

		int parameterIndex = 0;
		for (InfoOptionBean infoOption : selectedInfoOptions) {
			query.setParameter("infoOptionId" + parameterIndex, infoOption.getUniqueID());
			parameterIndex++;
		}

		try {
			Long definitionId = (Long) query.getSingleResult();

			AutoAttributeDefinition definition = null;
			if (definitionId != null) {
				String[] fetches = { "outputs", "criteria.outputs" };
				definition = persistenceManager.find(AutoAttributeDefinition.class, definitionId, fetches);
				return definition;
			}

		} catch (NoResultException nr) {
		} catch (NonUniqueResultException nur) {
		}
		return null;
	}

	public AutoAttributeDefinition saveDefinition(AutoAttributeDefinition definition) {

		definition = em.merge(definition);
		modifyCriteria(definition);
		return definition;
	}

	public void removeDefinition(AutoAttributeDefinition definition) {
		definition = em.find(AutoAttributeDefinition.class, definition.getId());
		em.remove(definition);
		modifyCriteria(definition);
	}

	private void modifyCriteria(AutoAttributeDefinition definition) {
		AutoAttributeCriteria criteria = em.find(AutoAttributeCriteria.class, definition.getCriteria().getId());
		criteria.touch();
		em.merge(criteria);
	}

	public void clearRetiredInfoFields(ProductType productType) {
		List<InfoFieldBean> retiredFields = new ArrayList<InfoFieldBean>();

		for (InfoFieldBean infoField : productType.getInfoFields()) {
			if (infoField.isRetired()) {
				retiredFields.add(infoField);
			}
		}

		for (InfoFieldBean field : retiredFields) {
			AutoAttributeCriteria criteria = criteriaUses(productType, field);
			if (criteria != null) {
				em.remove(criteria);
				return;
			}

		}

	}

	private AutoAttributeCriteria criteriaUses(ProductType productType, InfoFieldBean field) {
		if (productType.getAutoAttributeCriteria() != null) {
			AutoAttributeCriteria criteria = em.find(AutoAttributeCriteria.class, productType.getAutoAttributeCriteria().getId());
			if (criteria.getInputs().contains(field) || criteria.getOutputs().contains(field)) {
				return criteria;
			}
		}
		return null;
	}

	public AutoAttributeCriteria update(AutoAttributeCriteria criteria) {
		deleteExistingDefinitions(criteria);

		criteria.touch();
		return em.merge(criteria);
	}

	private void deleteExistingDefinitions(AutoAttributeCriteria criteria) {
		QueryBuilder<AutoAttributeDefinition> builder = new QueryBuilder<AutoAttributeDefinition>(AutoAttributeDefinition.class, new OpenSecurityFilter());
		builder.addSimpleWhere("criteria", criteria);
		for (AutoAttributeDefinition definition : persistenceManager.findAll(builder)) {
			persistenceManager.delete(definition);
		}
	}

	public void delete(AutoAttributeCriteria criteria) {
		deleteExistingDefinitions(criteria);
		AutoAttributeCriteria loadedCriteria = em.find(AutoAttributeCriteria.class, criteria.getId());
		persistenceManager.delete(loadedCriteria);
	}
}
