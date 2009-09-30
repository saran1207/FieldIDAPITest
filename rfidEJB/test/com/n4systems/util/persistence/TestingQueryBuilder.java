package com.n4systems.util.persistence;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.security.ManualSecurityFilter;
import com.n4systems.tools.Pager;

public class TestingQueryBuilder<E> extends QueryBuilder<E> {

	
	private Pager<E> pagedResults;
	private List<E> listResults;
	private E singleResult;
	
	
	public TestingQueryBuilder(Class<?> tableClass) {
		super(tableClass, new ManualSecurityFilter(null, null, null));
	}

	@Override
	public Pager<E> getPaginatedResults(EntityManager em, int page, int pageSize) {
		return pagedResults;
	}

	@Override
	public List<E> getResultList(EntityManager em, int firstResult, int maxResults) throws InvalidQueryException {
		return listResults;
	}

	@Override
	public List<E> getResultList(EntityManager em) throws InvalidQueryException {
		return listResults;
	}

	@Override
	public E getSingleResult(EntityManager em) throws InvalidQueryException {
		return singleResult;
	}
	
	
	public void setPagedResults(Pager<E> pagedResults) {
		this.pagedResults = pagedResults;
	}

	public void setListResults(List<E> listResults) {
		this.listResults = listResults;
	}

	public void setSingleResult(E singleResult) {
		this.singleResult = singleResult;
	}

}
