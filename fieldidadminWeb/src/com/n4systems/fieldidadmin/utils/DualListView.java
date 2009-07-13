package com.n4systems.fieldidadmin.utils;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.model.api.Listable;

public class DualListView implements Listable<Long> {
	
	private Long id;
	private String displayName;
	
	private List<Listable<Long>> subList = new ArrayList<Listable<Long>>();

	public DualListView() {}
	
	public DualListView(Long id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}
	
	public DualListView(Long id, String displayName, List<Listable<Long>> subList) {
		this.id = id;
		this.displayName = displayName;
		this.subList = subList;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<Listable<Long>> getSubList() {
		return subList;
	}

	public void setSubList(List<Listable<Long>> subList) {
		this.subList = subList;
	}
}
