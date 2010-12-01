package com.n4systems.ejb.legacy.impl;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.Option;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.TagOption;
import com.n4systems.model.TagOption.OptionKey;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;

import java.util.List;

import javax.persistence.EntityManager;


public class OptionManager implements Option {
	private Logger logger = Logger.getLogger(Option.class);
	
	
	protected EntityManager em;

	private PersistenceManager persistenceManager;
	
	
	public OptionManager(EntityManager em) {
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
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
