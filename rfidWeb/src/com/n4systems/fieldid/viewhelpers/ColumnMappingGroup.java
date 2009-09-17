package com.n4systems.fieldid.viewhelpers;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

public class ColumnMappingGroup implements Serializable, Comparable<ColumnMappingGroup> {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String label;
	private int order = 0;
	private boolean dynamic = false;
	private SortedSet<ColumnMapping> mappings = new TreeSet<ColumnMapping>();
	
	public ColumnMappingGroup() {}
	
	public ColumnMappingGroup(String id, String label, int order) {
		this.id = id;
		this.label = label;
		this.order = order;
	}

	public int compareTo(ColumnMappingGroup other) {
		// compare by order, only if orders are equal, fall back to the id
		return (order < other.order ? -1 : (order == other.order ? id.compareTo(other.id) : 1));
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public SortedSet<ColumnMapping> getMappings() {
		return mappings;
	}

	public void setMappings(SortedSet<ColumnMapping> mappings) {
		this.mappings = mappings;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ColumnMappingGroup)) {
			return false;
		}
		
		ColumnMappingGroup other = (ColumnMappingGroup)obj;
		return (id == other.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
