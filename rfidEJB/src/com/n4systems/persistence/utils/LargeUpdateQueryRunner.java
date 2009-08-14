package com.n4systems.persistence.utils;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;

public class LargeUpdateQueryRunner {
	protected static int MAX_IDS_IN_AN_IN_CLAUSE = 1000;
	
	private final Query query;
	private final List<Long> ids;
	
	private int to = MAX_IDS_IN_AN_IN_CLAUSE;
	private int from = 0;
	
	public LargeUpdateQueryRunner(Query query, List<Long> ids) {
		super();
		this.query = query;
		this.ids = ids;
	}
	
	
	public int executeUpdate() {
		int result = 0; 
		while (from < ids.size()) {
			query.setParameter("ids", ids.subList(from, (to < ids.size()) ? to : ids.size()));
			result += query.executeUpdate();
			from += MAX_IDS_IN_AN_IN_CLAUSE;
			to += MAX_IDS_IN_AN_IN_CLAUSE;
		}
		
		return result;
	}
	
	
}
