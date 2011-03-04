package com.n4systems.fieldid.viewhelpers;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ColumnMappingGroup implements Serializable, Comparable<ColumnMappingGroup> {
	private static final long serialVersionUID = 1L;
	
	private final String id;
	private final String label;
	private final int order;
	private boolean dynamic = false;
	private final SortedSet<ColumnMapping> mappings = new TreeSet<ColumnMapping>();
	
	
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

	public String getLabel() {
		return label;
	}

	public int getOrder() {
		return order;
	}
	
	public SortedSet<ColumnMapping> getMappings() {
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
	

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public boolean isStaticGroupEmpty(){
		return !dynamic && getMappings().isEmpty();
	}
}
