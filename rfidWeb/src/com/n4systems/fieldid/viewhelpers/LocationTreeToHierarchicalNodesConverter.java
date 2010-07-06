package com.n4systems.fieldid.viewhelpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.n4systems.model.location.PredefinedLocationTree;
import com.n4systems.model.location.PredefinedLocationTreeNode;
import com.n4systems.uitags.views.HierarchicalNode;

public class LocationTreeToHierarchicalNodesConverter {
	

	public List<HierarchicalNode> convert(PredefinedLocationTree tree) {
		return childList(tree.getNodes());
	}

	public HierarchicalNode convertLocationToNode(PredefinedLocationTreeNode location) {
		HierarchicalNode node = new HierarchicalNode();
		node.setName(location.getName());
		node.setId(location.getId());
		
		node.addChildren(childList(location.getChildren()));
		
		return node;
	}

	private List<HierarchicalNode> childList(Set<PredefinedLocationTreeNode> nextLevel) {
		List<HierarchicalNode> children = new ArrayList<HierarchicalNode>();
		for (PredefinedLocationTreeNode child : nextLevel) {
			children.add(convertLocationToNode(child));
		}
		
		sortNodesAlphabetically(children);
		return children;
	}

	private void sortNodesAlphabetically(List<HierarchicalNode> children) {
		Collections.sort(children, 
			new Comparator<HierarchicalNode>() {
				public int compare(HierarchicalNode o1, HierarchicalNode o2) {
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
			});
	}

}
