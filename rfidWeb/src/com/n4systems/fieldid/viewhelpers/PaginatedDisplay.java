package com.n4systems.fieldid.viewhelpers;

import com.n4systems.util.persistence.search.Paginated;

public interface PaginatedDisplay extends Paginated {
	public int getCurrentPage();
	public long getTotalResults();
	public void setCurrentPage(int displayPage);
	public int getTotalPages();
	public int getNextPage();
	public int getPreviousPage();
	public int getFirstPage();
	public int getLastPage();
	public boolean isHasNextPage();
	public boolean isHasPreviousPage();
	public boolean isValidPage();
}
