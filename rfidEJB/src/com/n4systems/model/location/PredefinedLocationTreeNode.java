package com.n4systems.model.location;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

public class PredefinedLocationTreeNode {

	private final Set<PredefinedLocationTreeNode> children = new HashSet<PredefinedLocationTreeNode>();
	private final PredefinedLocation nodeValue;
	
	public PredefinedLocationTreeNode(PredefinedLocation nodeValue) {
		super();
		this.nodeValue = nodeValue;
	}

	public Long getId() {
		return nodeValue.getId();
	}

	public String getName() {
		return nodeValue.getName();
	}

	public Set<PredefinedLocationTreeNode> getChildren() {
		return children;
	}
	
	public void addChild(PredefinedLocationTreeNode child) {
		children.add(child);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
}
