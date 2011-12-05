package com.n4systems.model.commenttemplate;


import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class CommentTemplateListableLoader extends ListLoader<CommentTemplate> {

	public CommentTemplateListableLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<CommentTemplate> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<CommentTemplate> builder = new QueryBuilder<CommentTemplate>(CommentTemplate.class, filter).setOrder("name");
		List<CommentTemplate> listables = builder.getResultList(em);
		return listables;
	}

}
