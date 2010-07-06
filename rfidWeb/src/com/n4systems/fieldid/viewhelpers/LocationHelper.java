package com.n4systems.fieldid.viewhelpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.n4systems.uitags.views.HierarchicalNode;

public class LocationHelper {

	private ArrayList<HierarchicalNode> nodes;
	
	private HierarchicalNode buildNode(int levelsToGo, int nodesPerLevel) {
		Random generator = new Random();
		HierarchicalNode node = new HierarchicalNode();

		String[] names = { "Locker", "Level", "Building", "Box", "Shelf", "Table", "Floor", "Room", "Unit", "Sector" };
		String[] levels = { "Job Site", "Sub-Area", "Division", "Area", "Level" };
		String[] numbers = { "3", "4", "5", "1", "2", "6", "5", "9", "8", "7", "7g" };
		int rndNames = generator.nextInt(names.length);
		int rndNumbers = generator.nextInt(numbers.length);
		int rndLevels = generator.nextInt(levels.length);
		node.setName(names[rndNames] + " " + numbers[rndNumbers]);

		if (levelsToGo > 0) {
			for (int i = 0; i < nodesPerLevel; i++) {
				node.addChild(buildNode(levelsToGo - 1, nodesPerLevel));

				node.setLevelName(names[rndLevels]);
			}
		}

		return node;
	}

	public List<HierarchicalNode> createNodes() {
		nodes = new ArrayList<HierarchicalNode>();

		int levels = 3;
		
		int childrenPerNode = 10;

		for (int i = 0; i < childrenPerNode; i++) {
			nodes.add(buildNode(levels - 1, childrenPerNode));
		}
		return nodes;
	}
	
}
