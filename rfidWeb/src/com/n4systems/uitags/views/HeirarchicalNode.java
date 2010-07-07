package com.n4systems.uitags.views;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class HeirarchicalNode {
	private String name;
	private Long id;
	
	private String levelName;
	
	private final List<HeirarchicalNode> children = new ArrayList<HeirarchicalNode>();
	
	
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
	public List<HeirarchicalNode> getChildren() {
		return children;
	}
	
	public void addChild(HeirarchicalNode child) {
		children.add(child);
	}
	
	public void addChildren(List<HeirarchicalNode> children) {
		this.children.addAll(children);
	}
	
	public boolean isLeaf() {
		return children.isEmpty();
	}
	
	@Override
	public String toString() {
		return "BasicNode [" + ToStringBuilder.reflectionToString(this) + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	
	
	
}
