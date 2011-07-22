package com.n4systems.model.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ColumnMappingGroupView implements Serializable, Comparable<ColumnMappingGroupView> {
	private static final long serialVersionUID = 1L;
	
	private final String id;
	private final String label;
	private final int order;
	private boolean dynamic = false;
	private final List<ColumnMappingView> mappings = new ArrayList<ColumnMappingView>();
    private final String groupKey;

	
	public ColumnMappingGroupView(String id, String label, int order, String groupKey) {
		this.id = id;
		this.label = label;
		this.order = order;
        this.groupKey = groupKey;
    }

	public int compareTo(ColumnMappingGroupView other) {
		// compare by order, only if orders are equal, fall back to the id
		return (order < other.order ? -1 : (order == other.order ? id.compareTo(other.id) : 1));
	}
	
	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public int getOrder() {
		return order;
	}
	
	public List<ColumnMappingView> getMappings() {
		return mappings;
	}
	
	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ColumnMappingGroupView)) {
			return false;
		}
		
		ColumnMappingGroupView other = (ColumnMappingGroupView)obj;
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

    public boolean isStaticGroupEmpty() {
        return !dynamic && getMappings().isEmpty();
    }

    public String getGroupKey() {
        return groupKey;
    }
}
