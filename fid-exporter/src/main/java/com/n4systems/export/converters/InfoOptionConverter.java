package com.n4systems.export.converters;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import rfid.ejb.entity.InfoOptionBean;

public class InfoOptionConverter extends ExportConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(InfoOptionBean.class);
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		InfoOptionBean option = (InfoOptionBean) source;
		
		writer.startNode("Attribute");
		writer.addAttribute("name", option.getInfoField().getName());
		writer.addAttribute("value", option.getName());
		writer.endNode();
	}
	
}
