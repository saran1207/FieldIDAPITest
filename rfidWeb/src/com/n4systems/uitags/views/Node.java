package com.n4systems.uitags.views;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private List<Node> children = new ArrayList<Node>();
	
	private String name;
	
	private String levelName;
	
	private boolean isLevel=false; 
	
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Node> getChildren() {
		return children;
	}
	
	public void addChild(Node child) {
		children.add(child);
	}
	
	public boolean isLeaf() {
		return children.isEmpty();
	}
	
	public boolean isLevel(){
		return isLevel;
	}
	
}
