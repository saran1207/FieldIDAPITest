package com.n4systems.fieldid.viewhelpers;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ColumnMapping implements Comparable<ColumnMapping>, Serializable {
	private static final long serialVersionUID = 1L;

	private final String id;
	private final String label;
	private final String pathExpression;
	private final String sortExpression;
	private final String outputHandler;
	private final boolean sortable;
	private final boolean onByDefault;
	private final int order;
	private final String requiredExtendedFeature;
	
	
	public ColumnMapping(String id, String label, String pathExpression, String sortExpression, String outputHandler, boolean sortable, boolean onByDefault, int order, String requiredExtendedFeature) {
		this.id = id;
		this.label = label;
		this.pathExpression = pathExpression;
		this.sortExpression = sortExpression;
		this.outputHandler = outputHandler;
		this.sortable = sortable;
		this.onByDefault = onByDefault;
		this.order = order;
		this.requiredExtendedFeature = requiredExtendedFeature;
	}
	
	public int compareTo(ColumnMapping mapping) {
		// compare by order, only if orders are equal, fall back to the id
		return (order < mapping.order ? -1 : (order == mapping.order ? id.compareTo(mapping.id) : 1));
	}
	
	public String getId() {
		return id;
	}
	
	
	
	public String getLabel() {
		return label;
	}

	public String getPathExpression() {
		return pathExpression;
	}

	
	public String getSortExpression() {
		// sort expression falls back on path expression
		return (sortExpression != null) ? sortExpression : pathExpression;
	}


	public String getOutputHandler() {
		return outputHandler;
	}


	public boolean isSortable() {
		return sortable;
	}


	public boolean isOnByDefault() {
		return onByDefault;
	}

	public int getOrder() {
		return order;
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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public boolean needsAnExtendedFeature() {
		return requiredExtendedFeature != null;
	}

	public String getRequiredExtendedFeature() {
		return requiredExtendedFeature;
	}
	
	
	
}
