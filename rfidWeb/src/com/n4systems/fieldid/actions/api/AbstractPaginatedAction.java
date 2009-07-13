package com.n4systems.fieldid.actions.api;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.viewhelpers.PaginatedDisplay;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class AbstractPaginatedAction extends AbstractAction implements PaginatedDisplay {
	private static final long serialVersionUID = 1L;
	private static final int INDEX_START = 1;
	private static final int PAGE_BLOCK_SIZE = 10;
	
	private int page = 0;
	private int pageSize = 20;
	private int totalResults = 0;
	
	public AbstractPaginatedAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
		pageSize = ConfigContext.getCurrentContext().getInteger(ConfigEntry.WEB_PAGINATION_PAGE_SIZE);
	}

	public int getPageSize() {
		return pageSize;
	}
	
	protected void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getPage() {
		return page;
	}
	
	public int getTotalPages() {
		return Double.valueOf(Math.ceil((double)totalResults / (double)pageSize)).intValue();
	}
	
	public long getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}
	
	public int getCurrentPage() {
		return getPage() + INDEX_START;
	}

	public void setCurrentPage(int displayPage) {
		page = displayPage - INDEX_START;
	}
	
	public int getFirstPage() {
		return INDEX_START;
	}

	public int getLastPage() {
		return getTotalPages();
	}

	public int getNextPage() {
		return getCurrentPage() + INDEX_START;
	}

	public int getPreviousPage() {
		return getCurrentPage() - INDEX_START;
	}

	public boolean isHasNextPage() {
		return (getCurrentPage() < getLastPage());
	}

	public boolean isHasPreviousPage() {
		return (getCurrentPage() > getFirstPage());
	}

	public boolean isValidPage() {
		return ( !isHasResults() || (getCurrentPage() >= getFirstPage() && getCurrentPage() <= getLastPage()));
	}
	
	public boolean isHasResults() {
		return (getTotalResults() > 0);
	}
	
	public List<Integer> getPageBlockList() {
		// round our current page down to the nearest PAGE_BLOCK_SIZE (eg 77 -> 70) and reindex to INDEX_START
		
		int startOffset = ((int)Math.floor( PAGE_BLOCK_SIZE / 2) - 1 );
		int endOffset = PAGE_BLOCK_SIZE - 1;
		
		int startingPage =  getCurrentPage() - startOffset;
		if (startingPage < INDEX_START ) { startingPage = INDEX_START; }
		
		if (startingPage + endOffset > getTotalPages()) { startingPage = getTotalPages() - endOffset; }
		
		if (startingPage < INDEX_START) { startingPage = INDEX_START;}
		
		int endingPage = startingPage + endOffset;
		if (endingPage > getTotalPages()) { endingPage=getTotalPages(); }
		
		
		List<Integer> pageList = new ArrayList<Integer>();
		for (int i = startingPage; i <= endingPage; i++) {
			pageList.add(i);
		}
		
		return pageList;
	}
}
