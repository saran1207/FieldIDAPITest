package com.n4systems.ws.model.unitofmeasure;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;

@XmlRootElement
public class WsUnitOfMeasure extends WsModel {
	private String type;
	private String name;
	private String shortName;
	private boolean selectable;
	private WsUnitOfMeasure child;

	@XmlElement(name="Type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@XmlElement(name="Name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="ShortName")
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@XmlElement(name="Selectable")
	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	@XmlElement(name="Child")
	public WsUnitOfMeasure getChild() {
		return child;
	}

	public void setChild(WsUnitOfMeasure child) {
		this.child = child;
	}

}
