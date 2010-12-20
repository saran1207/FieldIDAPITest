package com.n4systems.ws.model.autoattribute;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.n4systems.ws.model.assettype.WsInfoOption;

@XmlRootElement
public class WsAutoAttributeDefinition {
	private List<Long> inputs = new ArrayList<Long>();
	private List<WsInfoOption> outputs = new ArrayList<WsInfoOption>();

	@XmlElement(name="Inputs")
	public List<Long> getInputs() {
		return inputs;
	}

	public void setInputs(List<Long> inputs) {
		this.inputs = inputs;
	}

	@XmlElement(name="Outputs")
	public List<WsInfoOption> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<WsInfoOption> outputs) {
		this.outputs = outputs;
	}

}
