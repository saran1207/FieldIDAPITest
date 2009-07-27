package com.n4systems.model.commenttemplate;

import rfid.ejb.entity.CommentTempBean;

import com.n4systems.model.api.Listable;
import com.n4systems.persistence.loaders.ListableLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.ListableSelect;
import com.n4systems.util.persistence.QueryBuilder;

public class CommentTemplateListableLoader extends ListableLoader {

	public CommentTemplateListableLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter) {
		QueryBuilder<Listable<Long>> builder = new QueryBuilder<Listable<Long>>(CommentTempBean.class, filter.prepareFor(CommentTempBean.class));
		builder.setSelectArgument(new ListableSelect("uniqueID", "templateID"));
		builder.setOrder("templateID");	
		return builder;
	}

}
