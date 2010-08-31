package com.n4systems.model.location;

import java.util.HashSet;
import java.util.Set;

public class PredefinedLocationTree {

	private final Set<PredefinedLocationTreeNode> nodes = new HashSet<PredefinedLocationTreeNode>();
	
	
	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	public void addNode(PredefinedLocationTreeNode node) {
		nodes.add(node);
	}

	public Set<PredefinedLocationTreeNode> getNodes() {
		return nodes;
	}
	
	
}
