package com.n4systems.model.lastmodified;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class LastModifiedListLoader extends ListLoader<LastModified> {
	private final Class<?> clazz;
	private final String idField;
	private final String modifiedField;
	
	private Date modifiedAfter;
	
	public LastModifiedListLoader(SecurityFilter filter, Class<? extends AbstractEntity> clazz) {
		this(filter, clazz, "id", "modified");
	}
	
	private LastModifiedListLoader(SecurityFilter filter, Class<?> clazz, String idField, String modifiedField) {
		super(filter);
		this.clazz = clazz;
		this.idField = idField;
		this.modifiedField = modifiedField;
	}

	@Override
	protected List<LastModified> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<LastModified> builder = new QueryBuilder<LastModified>(clazz, filter);
		builder.setSelectArgument(new NewObjectSelect(LastModified.class, idField, modifiedField));
		
		if (modifiedAfter != null) {
			builder.addWhere(WhereClauseFactory.create(Comparator.GT, modifiedField, modifiedAfter));
		}
		
		List<LastModified> mods = builder.getResultList(em);
		return mods;
	}
	
	public LastModifiedListLoader modifiedAfter(Date modifiedAfter) {
		this.modifiedAfter = modifiedAfter;
		return this;
	}

}
