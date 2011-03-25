package com.n4systems.export.converters;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.ProofTestInfo;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ProofTestInfoConverter extends ExportConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(ProofTestInfo.class);
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		ProofTestInfo info = (ProofTestInfo) source;
		writeNode(writer, context, "Type", toFriendlyName(info.getProofTestType()));
		writeNode(writer, context, "PeakLoad", info.getPeakLoad());
		writeNode(writer, context, "Duration", info.getPeakLoad());
		writeNode(writer, context, "PeakLoadDuration", info.getPeakLoad());
	}

	private String toFriendlyName(ProofTestType type) {
		if (type == null) {
			return "";
		}
		
		switch (type) {
			case ROBERTS:
				return "Roberts";
			case NATIONALAUTOMATION:
				return "National Automation";
			case CHANT:
				return "Chant";
			case WIROP:
				return "Wirop";
			default:
				return "Unknown";
		}
	}
}
