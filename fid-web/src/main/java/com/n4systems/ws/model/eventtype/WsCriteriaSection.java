package com.n4systems.ws.model.eventtype;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;

@XmlRootElement
public class WsCriteriaSection extends WsModel {
	private String title;
	private boolean retired;
	private List<WsCriteria> criteria = new ArrayList<WsCriteria>();

	@XmlElement(name="Title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement(name="Criteria")
	public List<WsCriteria> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<WsCriteria> criteria) {
		this.criteria = criteria;
	}

	@XmlElement(name="Retired")
	public boolean isRetired() {
		return retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
	}
}
