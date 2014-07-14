package com.n4systems.export.converters;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.ThingEventProofTest;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ProofTestInfoConverter extends ExportConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(ThingEventProofTest.class);
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        ThingEventProofTest proofTest = (ThingEventProofTest) source;
		writeNode(writer, context, "Type", toFriendlyName(proofTest.getProofTestType()));
		writeNode(writer, context, "PeakLoad", proofTest.getPeakLoad());
		writeNode(writer, context, "Duration", proofTest.getDuration());
		writeNode(writer, context, "PeakLoadDuration", proofTest.getPeakLoadDuration());
        writeNode(writer, context, "FileName", proofTest.getProofTestFileName());
	}

	private String toFriendlyName(ProofTestType type) {
		if (type == null) {
			return "";
		}
		
		switch (type) {
			case ROBERTS:
				return "Roberts";
			case CHANT:
				return "Chant";
			case WIROP:
				return "Wirop";
			default:
				return "Unknown";
		}
	}
}
