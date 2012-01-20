package com.n4systems.fieldid.ws.v1.resources.model;

import java.util.ArrayList;
import java.util.List;

public class ListResponse<T> {
	private List<T> list = new ArrayList<T>();
	private int page;
	private int pageSize;
	private long total;

	public ListResponse() {}

	public ListResponse(List<T> list, int page, int pageSize, long total) {
		this.list = list;
		this.page = page;
		this.pageSize = pageSize;
		this.total = total;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

}
