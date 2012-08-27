package com.n4systems.fieldid.viewhelpers;

import com.n4systems.model.location.PredefinedLocationLevels;
import com.n4systems.model.location.PredefinedLocationTree;
import com.n4systems.model.location.PredefinedLocationTreeNode;
import com.n4systems.uitags.views.HierarchicalNode;

import java.util.*;

public class LocationTreeToHierarchicalNodesConverter {
	

	public List<HierarchicalNode> convert(PredefinedLocationTree tree, PredefinedLocationLevels levels) {
		return childList(tree.getNodes(), levels);
	}

	public HierarchicalNode convertLocationToNode(PredefinedLocationTreeNode location, PredefinedLocationLevels levels) {
		HierarchicalNode node = new HierarchicalNode();
		node.setName(location.getName());
		node.setId(location.getId());
		node.setLevelName(levels.getNameForLevel(location).getName());
        node.setLevelNumber(location.levelNumber());
        node.setOwner(location.getOwner());

		node.addChildren(childList(location.getChildren(), levels));
		
		return node;
	}

	private List<HierarchicalNode> childList(Set<PredefinedLocationTreeNode> nextLevel, PredefinedLocationLevels levels) {
		List<HierarchicalNode> children = new ArrayList<HierarchicalNode>();
		for (PredefinedLocationTreeNode child : nextLevel) {
			children.add(convertLocationToNode(child, levels));
		}
		sortNodesAlphabetically(children);
		return children;
	}

	private void sortNodesAlphabetically(List<HierarchicalNode> children) {
		Collections.sort(children, 
				
				
			new Comparator<HierarchicalNode>() {
				public int compare(HierarchicalNode o1, HierarchicalNode o2) {
					return NaturalOrderSort.compareNaturalIgnoreCaseAscii(o1.getName(), o2.getName());
				}
			});
	}

}
