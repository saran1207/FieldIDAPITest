package com.n4systems.util.persistence.search;

import com.n4systems.util.persistence.OrderClause;

import java.io.Serializable;

public class SortTerm implements Serializable {

	private String path;
	private SortDirection direction = SortDirection.DESC;
	
	public SortTerm() {}
	
	public SortTerm(String path) {
		this(path, null);
	}
	
	public SortTerm(String path, SortDirection direction) {
		this.path = path;
		this.direction = direction;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public SortDirection getDirection() {
		return direction;
	}

	public void setDirection(SortDirection direction) {
		this.direction = direction;
	}
	
	public OrderClause toSortField() {
		return new OrderClause(path, direction.isAscending());
	}
}
