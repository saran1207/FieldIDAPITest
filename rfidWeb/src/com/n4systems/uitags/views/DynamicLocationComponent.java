package com.n4systems.uitags.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;
import com.opensymphony.xwork2.util.ValueStack;

public class DynamicLocationComponent extends UIBean {

	private ArrayList<Node> nodes;
	public static final String TEMPLATE = "dynamicLocation";
	public DynamicLocationComponent(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE;
	}
	
	@Override
	public void evaluateParams() {
		super.evaluateParams();
		addParameter("nodesList", getNodes());
		
		
	}

	public List<Node> getNodes() {

		nodes = new ArrayList<Node>();

		int levels = 3;

		int childrenPerNode = 20;

		for (int i = 0; i < childrenPerNode; i++) {
			nodes.add(buildNode(levels - 1, childrenPerNode));
		}
		return nodes;
	}

	private Node buildNode(int levelsToGo, int nodesPerLevel) {
		Random generator = new Random();
		Node node = new Node();

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
	
	

}
