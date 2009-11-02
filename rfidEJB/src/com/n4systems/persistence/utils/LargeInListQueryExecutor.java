package com.n4systems.persistence.utils;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.util.persistence.QueryBuilder;

public class LargeInListQueryExecutor {
	protected final static int DEFAULT_MAX_IN_LIST_SIZE = 1000;
	protected final static String DEFAULT_PARAM_NAME = "ids";
	
	private final int maxInListSize;
	private final String paramName;
	
	public LargeInListQueryExecutor(int maxInListSize, String paramName) {
		this.maxInListSize = maxInListSize;
		this.paramName = paramName;
	}
	
	public LargeInListQueryExecutor(String paramName) {
		this(DEFAULT_MAX_IN_LIST_SIZE, paramName);
	}
	
	public LargeInListQueryExecutor() {
		this(DEFAULT_MAX_IN_LIST_SIZE, DEFAULT_PARAM_NAME);
	}
	
	@SuppressWarnings("unchecked")
	public List getResultList(Query query, List<?> inList) {
		List results = new ArrayList();
		
		List rs;
		for (int start = 0; start < inList.size(); start += maxInListSize) {
			updateQueryInListParameter(query, start, inList);
			
			rs = query.getResultList();
			results.addAll(rs);
		}
		
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getResultList(EntityManager em, QueryBuilder<T> queryBuilder, List<?> inList) {
		return getResultList(queryBuilder.createQuery(em), inList);
	}

	public int executeUpdate(Query query, List<?> inList) {
		int totalUpdates = 0;
		
		for (int start = 0; start < inList.size(); start += maxInListSize) {
			updateQueryInListParameter(query, start, inList);
			
			totalUpdates += query.executeUpdate();
		}
		
		return totalUpdates;
	}

	private int calcEndBound(int start, int listSize) {
		return ((start + maxInListSize) < listSize) ? start + maxInListSize : listSize;
	}

	private void updateQueryInListParameter(Query query, int start, List<?> inList) {
		int end = calcEndBound(start, inList.size());
		
		query.setParameter(paramName, inList.subList(start, end));
	}
}
