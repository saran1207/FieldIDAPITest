package com.n4systems.tools;

import java.util.List;

public interface Pager<T> {

	
	public Integer getPreviousPage();
	public Integer getNextPage();
	public boolean isHasNextPage();
	public boolean isHasPreviousPage();
	public List<T> getList();
	public Integer getCurrentPage();
	public int getReadableCurrentPage();
	public long getTotalResults();
	public long getTotalPages();
	public boolean hasResults();
	public boolean validPage();
	public long getLastPage();
	
	
	
}
