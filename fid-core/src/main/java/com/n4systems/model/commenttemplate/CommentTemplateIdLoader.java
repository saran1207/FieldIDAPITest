package com.n4systems.model.commenttemplate;

import javax.persistence.EntityManager;

import rfid.ejb.entity.CommentTempBean;

import com.n4systems.model.safetynetwork.IdLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class CommentTemplateIdLoader extends SecurityFilteredLoader<CommentTempBean> implements IdLoader<CommentTemplateIdLoader> {
	private Long id;
	
	public CommentTemplateIdLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected CommentTempBean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<CommentTempBean> builder = new QueryBuilder<CommentTempBean>(CommentTempBean.class, filter);
		builder.addWhere(WhereClauseFactory.create("uniqueID", id));
		
		CommentTempBean comment = builder.getSingleResult(em);
		return comment;
	}

	@Override
	public CommentTemplateIdLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
