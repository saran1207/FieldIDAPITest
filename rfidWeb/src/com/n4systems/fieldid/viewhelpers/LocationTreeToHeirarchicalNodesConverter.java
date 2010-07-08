package com.n4systems.fieldid.viewhelpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.n4systems.model.location.PredefinedLocationLevels;
import com.n4systems.model.location.PredefinedLocationTree;
import com.n4systems.model.location.PredefinedLocationTreeNode;
import com.n4systems.uitags.views.HeirarchicalNode;

public class LocationTreeToHeirarchicalNodesConverter {
	

	public List<HeirarchicalNode> convert(PredefinedLocationTree tree, PredefinedLocationLevels levels) {
		return childList(tree.getNodes(), levels);
	}

	public HeirarchicalNode convertLocationToNode(PredefinedLocationTreeNode location, PredefinedLocationLevels levels) {
		HeirarchicalNode node = new HeirarchicalNode();
		node.setName(location.getName());
		node.setId(location.getId());
		node.setLevelName(levels.getNameForLevel(location).getName());
		
		node.addChildren(childList(location.getChildren(), levels));
		
		return node;
	}

	private List<HeirarchicalNode> childList(Set<PredefinedLocationTreeNode> nextLevel, PredefinedLocationLevels levels) {
		List<HeirarchicalNode> children = new ArrayList<HeirarchicalNode>();
		for (PredefinedLocationTreeNode child : nextLevel) {
			children.add(convertLocationToNode(child, levels));
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
