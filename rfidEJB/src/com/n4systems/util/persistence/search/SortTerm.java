package com.n4systems.util.persistence.search;

import com.n4systems.util.persistence.OrderClause;

public class SortTerm {
	public enum Direction { 
		ASC(true, "asc"), DESC(false, "desc");
		
		private boolean ascending;
		private String displayName;
		
		Direction(boolean reverse, String externalName) {
			this.ascending = reverse;
			this.displayName = externalName;
		}
		
		public boolean isAscending() {
			return ascending;
		}
		
		public String getDisplayName() {
			return displayName;
		}
	};
	
	private String path;
	private Direction direction = Direction.DESC;
	
	public SortTerm() {}
	
	public SortTerm(String path) {
		this(path, null);
	}
	
	public SortTerm(String path, Direction direction) {
		this.path = path;
		this.direction = direction;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public OrderClause toSortField() {
		return new OrderClause(path, direction.isAscending());
	}
}
