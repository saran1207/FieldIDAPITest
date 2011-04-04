package com.n4systems.model.eventtype;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.EventType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class EventTypeListLoader extends ListLoader<EventType> {
	
	private String[] postFetchFields = new String[0];
	private String nameFilter;
	private Long groupFilter;


	public EventTypeListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<EventType> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<EventType> builder = new QueryBuilder<EventType>(EventType.class, filter);
		
		if(nameFilter != null && !nameFilter.isEmpty()) {
			builder.addWhere(Comparator.LIKE, "nameFilter", "name", nameFilter, WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH);
		}
		
		if(groupFilter != null) {
			builder.addSimpleWhere("group.id", groupFilter);
		}
		
		builder.addOrder("name");
		builder.addPostFetchPaths("eventForm.sections", "infoFieldNames");
		builder.addPostFetchPaths(postFetchFields);
		return builder.getResultList(em);
	}
	
	public EventTypeListLoader setPostFetchFields(String...fields) {
		this.postFetchFields = fields;
		return this;
	}
	
	public EventTypeListLoader setNameFilter(String nameFilter) {
		this.nameFilter = nameFilter;
		return this;
	}

	public EventTypeListLoader setGroupFilter(Long groupFilter) {
		this.groupFilter = groupFilter;
		return this;
	}

	
}
