package com.n4systems.model.commenttemplate;

import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.CommentTempBean;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class CommentTemplateListLoader extends ListLoader<CommentTempBean> {

	public CommentTemplateListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<CommentTempBean> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<CommentTempBean> builder = new QueryBuilder<CommentTempBean>(CommentTempBean.class, filter);
		List<CommentTempBean> templates = builder.getResultList(em);
		return templates;
	}
	
}
