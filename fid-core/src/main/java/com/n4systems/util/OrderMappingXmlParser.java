package com.n4systems.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import rfid.ejb.entity.OrderMappingBean;

import java.util.List;

public class OrderMappingXmlParser {
	private XStream xStream;
	
	public OrderMappingXmlParser() {
		
		xStream = new XStream(new DomDriver());
		xStream.alias("OrderMapping", OrderMappingBean.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<OrderMappingBean> parse(String xml) {
		return (List<OrderMappingBean>)xStream.fromXML(xml);
	}
	
	public String format(List<OrderMappingBean> orderMappings) {
		return xStream.toXML(orderMappings);
	}
	
}
