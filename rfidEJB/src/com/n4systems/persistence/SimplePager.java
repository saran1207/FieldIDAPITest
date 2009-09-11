package com.n4systems.persistence;

import java.util.List;

import com.n4systems.tools.Pager;

public class SimplePager<T> implements Pager<T> {
	private final int currentPage;
	private final long totalResults;
	private final long totalPages;
	private final List<T> results;
	
	public SimplePager(int currentPage, long pageSize, long totalResults, List<T> results) {
		this.currentPage = currentPage;
		this.totalResults = totalResults;
		this.results = results;
		totalPages = (int)Math.ceil((double)totalResults / (double)pageSize);
	}
	
	public Integer getCurrentPage() {
		return currentPage;
	}

	public long getLastPage() {
		return totalPages;
	}

	public List<T> getList() {
		return results;
	}

	public Integer getNextPage() {
		return currentPage + 1;
	}

	public Integer getPreviousPage() {
		return currentPage - 1;
	}

	public int getReadableCurrentPage() {
		return getCurrentPage();
	}

	public long getTotalPages() {
		return totalPages;
	}

	public long getTotalResults() {
		return totalResults;
	}

	public boolean hasResults() {
		return totalResults > 0;
	}

	public boolean isHasNextPage() {
		return currentPage < totalPages;
	}

	public boolean isHasPreviousPage() {
		return currentPage > 1;
	}

	public boolean validPage() {
		return (currentPage >= 1 && currentPage <= totalPages) ;
	}

}
