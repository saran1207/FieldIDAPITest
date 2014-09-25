package com.n4systems.fieldid.api.pub.resources;

import java.util.List;

public class ListResponse<A> {
	private final int page;
	private final int pageSize;
	private final long total;
	private final List<A> items;

	public ListResponse(int page, int pageSize, long total, List<A> items) {
		this.page = page;
		this.pageSize = pageSize;
		this.total = total;
		this.items = items;
	}

	public int getPage() {
		return page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public long getTotal() {
		return total;
	}

	public List<A> getItems() {
		return items;
	}
}
