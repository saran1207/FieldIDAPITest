package com.n4systems.ws.model.autoattribute;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WsAutoAttributeCriteria {
	private long assetTypeId;
	private List<Long> inputs = new ArrayList<Long>();
	private List<WsAutoAttributeDefinition> definitions = new ArrayList<WsAutoAttributeDefinition>();

	@XmlElement(name="AssetTypeId")
	public long getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(long assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	@XmlElement(name="Inputs")
	public List<Long> getInputs() {
		return inputs;
	}

	public void setInputs(List<Long> inputs) {
		this.inputs = inputs;
	}

	@XmlElement(name="Definitions")
	public List<WsAutoAttributeDefinition> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<WsAutoAttributeDefinition> definitions) {
		this.definitions = definitions;
	}
}
