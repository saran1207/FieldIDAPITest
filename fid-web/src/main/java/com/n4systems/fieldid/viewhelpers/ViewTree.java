package com.n4systems.fieldid.viewhelpers;

import com.n4systems.model.api.Listable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViewTree<T> implements Serializable, Iterable<Listable<T>> {
	private static final long serialVersionUID = 1L;
	
	private T nodeId;
	private String nodeName;
	private List<Listable<T>> elements = new ArrayList<Listable<T>>();
	private List<ViewTree<T>> childNodes = new ArrayList<ViewTree<T>>();
	
	public ViewTree() {}
	
	public ViewTree(T nodeId, String nodeName) {
		this.nodeId = nodeId;
		this.nodeName = nodeName;
	}

	public T getNodeId() {
    	return nodeId;
    }

	public void setNodeId(T nodeId) {
    	this.nodeId = nodeId;
    }

	public String getNodeName() {
    	return nodeName;
    }

	public void setNodeName(String nodeName) {
    	this.nodeName = nodeName;
    }

	public List<Listable<T>> getElements() {
    	return elements;
    }

	public void setElements(List<Listable<T>> elements) {
    	this.elements = elements;
    }

	public List<ViewTree<T>> getChildNodes() {
    	return childNodes;
    }

	public void setChildNodes(List<ViewTree<T>> childNodes) {
    	this.childNodes = childNodes;
    }

	public Iterator<Listable<T>> iterator() {
	    return elements.iterator();
    }
	
	public long size() {
		long size = elements.size();
		for (ViewTree<T> child: childNodes) {
			size += child.size();
		}
		return size;
	}
	
	public boolean isEmpty() {
		return (size() == 0);
	}
}
