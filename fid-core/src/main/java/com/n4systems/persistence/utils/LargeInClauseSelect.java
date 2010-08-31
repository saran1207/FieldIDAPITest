package com.n4systems.persistence.utils;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class LargeInClauseSelect<T> {
	protected static int MAX_IDS_IN_AN_IN_CLAUSE = 1000;
	
	private final QueryBuilder<T> query;
	private final List<Long> ids;
	private final EntityManager em;
	
	private int to = 1000;
	private int from = 0;
	
	public LargeInClauseSelect(QueryBuilder<T> query, List<Long> ids, EntityManager em) {
		super();
		this.query = query;
		this.ids = ids;
		this.em = em;
	}
	
	
	public List<T> getResultList() {
		List<T> results = new ArrayList<T>(); 
		while (from < ids.size()) {
			query.addWhere(Comparator.IN, "ids", "id", ids.subList(from, (to < ids.size()) ? to : ids.size()));
			results.addAll(query.getResultList(em));
			from += MAX_IDS_IN_AN_IN_CLAUSE;
			to += MAX_IDS_IN_AN_IN_CLAUSE;
		}
		
		return results;
	}
	
}
