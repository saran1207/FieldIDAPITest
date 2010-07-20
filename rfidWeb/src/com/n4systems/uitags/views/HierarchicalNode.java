package com.n4systems.uitags.views;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class HierarchicalNode {
	private String name;
	private Long id;
	private String levelName;

	private final List<HierarchicalNode> children = new ArrayList<HierarchicalNode>();

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

	public List<HierarchicalNode> getChildren() {
		return children;
	}

	public void addChild(HierarchicalNode child) {
		children.add(child);
	}

	public void addChildren(List<HierarchicalNode> children) {
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

	public HierarchicalNode findById(Long id) {
		return findById(id, this);
	}

	private HierarchicalNode findById(Long id, HierarchicalNode node) {
		if (node.getId().equals(id)) {
			return node;
		}

		HierarchicalNode findNode;
		for (HierarchicalNode childNode : node.getChildren()) {
			findNode = findById(id, childNode);
			if (findNode != null) {
				return findNode;
			}
		}
		return null;
	}
}
