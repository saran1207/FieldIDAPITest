package com.n4systems.fieldid.viewhelpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.n4systems.model.location.PredefinedLocationTree;
import com.n4systems.model.location.PredefinedLocationTreeNode;
import com.n4systems.uitags.views.HeirarchicalNode;

public class LocationTreeToHierarchicalNodesConverter {
	

	public List<HeirarchicalNode> convert(PredefinedLocationTree tree) {
		return childList(tree.getNodes());
	}

	public HeirarchicalNode convertLocationToNode(PredefinedLocationTreeNode location) {
		HeirarchicalNode node = new HeirarchicalNode();
		node.setName(location.getName());
		node.setId(location.getId());
		
		node.addChildren(childList(location.getChildren()));
		
		return node;
	}

	private List<HeirarchicalNode> childList(Set<PredefinedLocationTreeNode> nextLevel) {
		List<HeirarchicalNode> children = new ArrayList<HeirarchicalNode>();
		for (PredefinedLocationTreeNode child : nextLevel) {
			children.add(convertLocationToNode(child));
		}
		
		sortNodesAlphabetically(children);
		return children;
	}

	private void sortNodesAlphabetically(List<HeirarchicalNode> children) {
		Collections.sort(children, 
			new Comparator<HeirarchicalNode>() {
				public int compare(HeirarchicalNode o1, HeirarchicalNode o2) {
					return o1.getName().compareToIgnoreCase(o2.getName());
				}
			});
	}

}
