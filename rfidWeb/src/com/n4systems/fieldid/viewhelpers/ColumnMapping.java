package com.n4systems.fieldid.viewhelpers;

import java.io.Serializable;

public class ColumnMapping implements Comparable<ColumnMapping>, Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String label;
	private String pathExpression;
	private String outputHandler;
	private boolean sortable = true;
	private boolean onByDefault = false;
	private int order = 0;
	
	public ColumnMapping() {}
	
	public ColumnMapping(String id, String label, String pathExpression, String outputHandler, boolean sortable, boolean onByDefault, int order) {
		this.id = id;
		this.label = label;
		this.pathExpression = pathExpression;
		this.outputHandler = outputHandler;
		this.sortable = sortable;
		this.onByDefault = onByDefault;
		this.order = order;
	}
	
	public int compareTo(ColumnMapping mapping) {
		// compare by order, only if orders are equal, fall back to the id
		return (order < mapping.order ? -1 : (order == mapping.order ? id.compareTo(mapping.id) : 1));
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPathExpression() {
		return pathExpression;
	}

	public void setPathExpression(String beanPath) {
		this.pathExpression = beanPath;
	}

	public String getOutputHandler() {
		return outputHandler;
	}

	public void setOutputHandler(String outputHandlerClass) {
		this.outputHandler = outputHandlerClass;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public boolean isOnByDefault() {
		return onByDefault;
	}

	public void setOnByDefault(boolean onByDefault) {
		this.onByDefault = onByDefault;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int orderWeight) {
		this.order = orderWeight;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ColumnMapping)) {
			return false;
		}
		
		ColumnMapping other = (ColumnMapping)obj;
		return (id == other.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
}
