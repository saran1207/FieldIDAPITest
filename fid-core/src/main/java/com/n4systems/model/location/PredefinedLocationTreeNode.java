package com.n4systems.model.location;

import com.n4systems.model.orgs.BaseOrg;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PredefinedLocationTreeNode implements TreeNode, Iterable<PredefinedLocationTreeNode>, Comparable<PredefinedLocationTreeNode> {

	private PredefinedLocationTreeNode parent;
	private final SortedSet<PredefinedLocationTreeNode> children = new TreeSet<>();
	private final PredefinedLocation nodeValue;
	
	public PredefinedLocationTreeNode(PredefinedLocation nodeValue) {
		this.nodeValue = nodeValue;
	}

	public Long getId() {
		return nodeValue.getId();
	}

	public PredefinedLocationTreeNode getParent() {
		return parent;
	}

	public Long getParentId() {
		return (parent != null) ? parent.getId() : null;
	}

	public String getName() {
		return nodeValue.getName();
	}

	public SortedSet<PredefinedLocationTreeNode> getChildren() {
		return children;
	}
	
	public void addChild(PredefinedLocationTreeNode child) {
		child.parent = this;
		children.add(child);
	}
	
	public int levelNumber() {
		return nodeValue.levelNumber();
	}

    public BaseOrg getOwner() {
        return nodeValue.getOwner();
    }

    public PredefinedLocation getNodeValue() {
        return nodeValue;
    }

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int compareTo(PredefinedLocationTreeNode o) {
		return this.nodeValue.getId().compareTo(o.nodeValue.getId()); // children are ordered by id
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PredefinedLocationTreeNode)) return false;
		PredefinedLocationTreeNode that = (PredefinedLocationTreeNode) o;
		if (!nodeValue.equals(that.nodeValue)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return nodeValue.hashCode();
	}

	// Iterates the tree breadth-first (BFS).  Each tree level is visited in order of depth.  Action can return true to stop the search.
	private Optional<PredefinedLocationTreeNode> breadthFirstSearch(Predicate<PredefinedLocationTreeNode> predicate) {
		Set<PredefinedLocationTreeNode> visited = new HashSet<>();
		Queue<PredefinedLocationTreeNode> queue = new LinkedBlockingDeque<>();
		visited.add(this);
		queue.add(this);
		PredefinedLocationTreeNode current;
		while (!queue.isEmpty()) {
			current = queue.remove();
			if (predicate.test(current)) {
				return Optional.of(current);
			}
			//Note: do not refactor to stream or forEach as both depend on this method
			for(PredefinedLocationTreeNode child: current.getChildren()) {
				if (!visited.contains(child)) {
					visited.add(child);
					queue.add(child);
				}
			}
		}
		return Optional.empty();
	}

	@Override
	public void forEach(Consumer<? super PredefinedLocationTreeNode> action) {
		breadthFirstSearch(n -> { action.accept(n); return false; });
	}

	private List<PredefinedLocationTreeNode> toList() {
		List<PredefinedLocationTreeNode> allNodes = new ArrayList<>();
		forEach(allNodes::add);
		return allNodes;
	}

	@Override
	public Spliterator<PredefinedLocationTreeNode> spliterator() {
		return toList().spliterator();
	}

	@Override
	public Iterator<PredefinedLocationTreeNode> iterator() {
		return toList().iterator();
	}

	public Stream<PredefinedLocationTreeNode> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	public Optional<PredefinedLocationTreeNode> findFirst(Predicate<PredefinedLocationTreeNode> predicate) {
		return breadthFirstSearch(predicate);
	}

	public PredefinedLocationTreeNode findTopNode() {
		Set<PredefinedLocationTreeNode> visited = new HashSet<>();
		PredefinedLocationTreeNode node = this;
		while (node.parent != null) {
			// this to protects against circular references which would lead to an infinite loop
			if (!visited.add(node)) {
				throw new IllegalStateException("Circular reference detected in PredefinedLocationTree");
			}
			node = node.parent;
		}
		return node;
	}

	public void placeNodeInTree(PredefinedLocation location) {
		// traverses to the top node then back down to find the parent of node
		Optional<PredefinedLocationTreeNode> parentNode = findTopNode().findFirst(n -> n.getNodeValue().equals(location.getParent()));
		if (parentNode.isPresent()) {
			parentNode.get().addChild(new PredefinedLocationTreeNode(location));
		} else {
			throw new IllegalArgumentException("Parent not found in tree for: " + location);
		}
	}


}
