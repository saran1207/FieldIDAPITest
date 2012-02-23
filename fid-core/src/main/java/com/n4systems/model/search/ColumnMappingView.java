package com.n4systems.model.search;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ColumnMappingView implements Comparable<ColumnMappingView>, Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String label;
	private String pathExpression;
	private String sortExpression;
	private String outputHandler;
	private boolean sortable;
	private boolean enabled;
	private int order;
	private String requiredExtendedFeature;
    private String groupKey;
    private Long dbColumnId;
    private String joinExpression;

    private String localizedLabel;
	
	
	public ColumnMappingView(String id, String label, String pathExpression, String sortExpression, String outputHandler, boolean sortable, boolean enabled, int order, String requiredExtendedFeature, String groupKey, Long dbColumnId, String joinExpression) {
		this.id = id;
		this.label = label;
		this.pathExpression = pathExpression;
		this.sortExpression = sortExpression;
		this.outputHandler = outputHandler;
		this.sortable = sortable;
		this.enabled = enabled;
		this.order = order;
		this.requiredExtendedFeature = requiredExtendedFeature;
        this.groupKey = groupKey;
        this.dbColumnId = dbColumnId;
        this.joinExpression = joinExpression;
	}
	
	public int compareTo(ColumnMappingView mapping) {
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


	public boolean isEnabled() {
		return enabled;
	}

	public int getOrder() {
		return order;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ColumnMappingView)) {
			return false;
		}
		
		ColumnMappingView other = (ColumnMappingView)obj;
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

    public String getGroupKey() {
        return groupKey;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getDbColumnId() {
        return dbColumnId;
    }

    public void setDbColumnId(Long dbColumnId) {
        this.dbColumnId = dbColumnId;
    }

    public String getJoinExpression() {
        return joinExpression;
    }

    public void setJoinExpression(String joinExpression) {
        this.joinExpression = joinExpression;
    }

    public String getLocalizedLabel() {
        return localizedLabel;
    }

    public void setLocalizedLabel(String localizedLabel) {
        this.localizedLabel = localizedLabel;
    }
}
