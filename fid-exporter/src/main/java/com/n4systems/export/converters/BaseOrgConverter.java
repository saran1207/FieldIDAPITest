package com.n4systems.export.converters;

import com.n4systems.model.orgs.BaseOrg;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class BaseOrgConverter extends ExportConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return BaseOrg.class.isAssignableFrom(type);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		BaseOrg org = (BaseOrg) source;
		
		writeOrgName(writer, "Organization", (org.getSecondaryOrg() != null) ? org.getSecondaryOrg() : org.getPrimaryOrg());
		writeOrgName(writer, "Customer", org.getCustomerOrg());
		writeOrgName(writer, "Division", org.getDivisionOrg());
	}
	
	private void writeOrgName(HierarchicalStreamWriter writer, String name, BaseOrg org) {
		writer.startNode(name);
		if (org != null) {
			writer.setValue(org.getName());
		}
		writer.endNode();
	}
	
}
