package com.n4systems.model.location;

import java.util.SortedSet;
import java.util.TreeSet;

public class PredefinedLocationTree {

	private final SortedSet<PredefinedLocationTreeNode> nodes = new TreeSet<>();

	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	public void addNode(PredefinedLocationTreeNode node) {
		nodes.add(node);
	}

	public SortedSet<PredefinedLocationTreeNode> getNodes() {
		return nodes;
	}

}
