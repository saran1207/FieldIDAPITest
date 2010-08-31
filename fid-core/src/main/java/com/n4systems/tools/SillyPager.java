package com.n4systems.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * this pager provides raps a list into a pager, it only will ever show page 1.
 * @author aaitken
 *
 * @param <T>
 */
public class SillyPager<T> implements Pager<T> {

	List<T> list;
	
	public SillyPager(Set<T> set) {
		this(new ArrayList<T>(set));
	}
	
	public SillyPager(List<T> list) {
		this.list = list;
	}
	
	public Integer getCurrentPage() {
		return 1;
	}

	public long getLastPage() {
		return 0;
	}

	public List<T> getList() {
		return list;
	}

	public Integer getNextPage() {
		return 1;
	}

	public Integer getPreviousPage() {
		return 1;
	}

	public int getReadableCurrentPage() {
		return 0;
	}

	public long getTotalPages() {
		return (list.isEmpty()) ? 0 : 1;
	}

	public long getTotalResults() {
		return list.size();
	}

	public boolean hasResults() {
		return !list.isEmpty();
	}

	public boolean isHasNextPage() {
		return false;
	}

	public boolean isHasPreviousPage() {
		return false;
	}

	public boolean validPage() {
		return true;
	}

	
}
