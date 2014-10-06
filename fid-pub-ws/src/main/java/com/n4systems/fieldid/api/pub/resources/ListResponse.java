package com.n4systems.fieldid.api.pub.resources;

import java.util.List;

public class ListResponse<A> {
	private int page;
	private int pageSize;
	private long total;
	private List<A> items;

	public int getPage() {
		return page;
	}

	public ListResponse<A> setPage(int page) {
		this.page = page;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public ListResponse<A> setPageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public long getTotal() {
		return total;
	}

	public ListResponse<A> setTotal(long total) {
		this.total = total;
		return this;
	}

	public List<A> getItems() {
		return items;
	}

	public ListResponse<A> setItems(List<A> items) {
		this.items = items;
		return this;
	}
}
