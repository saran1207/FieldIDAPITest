package com.n4systems.ejb;

import com.n4systems.exceptions.InvalidArgumentException;

public class PageHolder<K> {

	private final K pageResults;
	private final int totalsResults;
	
	
	public PageHolder(K pageResults, int totalsResults) {
		super();
		if (pageResults == null) {
			throw new InvalidArgumentException("the page results can not be null");
		}
		
		this.pageResults = pageResults;
		this.totalsResults = totalsResults;
	}


	public K getPageResults() {
		return pageResults;
	}


	public int getTotalsResults() {
		return totalsResults;
	}
	
	
	
}
