package com.n4systems.ws.model.autoattribute;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.WsModel;

@XmlRootElement
public class WsAutoAttributeCriteria extends WsModel {
	private long assetTypeId;
	private List<Long> inputs = new ArrayList<Long>();

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
}
