package com.n4systems.model.lastmodified;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;

public class LastModifiedListLoader extends ListLoader<LastModified> {
	private final Class<?> clazz;
	
	public LastModifiedListLoader(SecurityFilter filter, Class<? extends AbstractEntity> clazz) {
		super(filter);
		this.clazz = clazz;
	}

	@Override
	protected List<LastModified> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<LastModified> builder = new QueryBuilder<LastModified>(clazz, filter);
		builder.setSelectArgument(new NewObjectSelect(LastModified.class, "id", "modified"));
		
		List<LastModified> mods = builder.getResultList(em);
		return mods;
	}

}
