package com.n4systems.model.location;

import com.n4systems.model.orgs.BaseOrg;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Set;

public class PredefinedLocationTreeNode implements TreeNode {

	private final Set<PredefinedLocationTreeNode> children = new HashSet<PredefinedLocationTreeNode>();
	private final PredefinedLocation nodeValue;
	
	public PredefinedLocationTreeNode(PredefinedLocation nodeValue) {
		super();
		this.nodeValue = nodeValue;
	}

	public Long getId() {
		return nodeValue.getId();
	}

	public String getName() {
		return nodeValue.getName();
	}

	public Set<PredefinedLocationTreeNode> getChildren() {
		return children;
	}
	
	public void addChild(PredefinedLocationTreeNode child) {
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

}
