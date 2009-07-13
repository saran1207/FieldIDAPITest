package com.n4systems.util.persistence.search;

public interface Paginated {
	public int getPage();
	public int getPageSize();
	public void setTotalResults(int totalResults);	
}
