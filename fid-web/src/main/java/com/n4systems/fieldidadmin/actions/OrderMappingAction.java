package com.n4systems.fieldidadmin.actions;

import com.n4systems.ejb.legacy.OrderMapping;
import com.n4systems.model.OrderKey;
import com.n4systems.util.ListHelper;
import rfid.ejb.entity.OrderMappingBean;

import java.io.*;
import java.util.Collection;
import java.util.Map;

public class OrderMappingAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;

	private OrderMapping orderMappingEJBContainer;
	
	private Long id;
	private OrderMappingBean orderMapping;
	private Collection<OrderMappingBean> orderMappings;
	
	
	private File orderMappingXml;
	private String orderMappingXmlFileName;
	private String orderMappingXmlContentType;
	
	public String doList() {
		orderMappings = orderMappingEJBContainer.getOrganizationMappings();
		
		return SUCCESS;
	}
	
	public String doFormInput() {
		if (id != null) {
			orderMapping = orderMappingEJBContainer.getOrderMapping(id);
		}
		
		return INPUT;
	}
	
	public String doFileFormInput() {
		return INPUT;
	}
	
	public String doSave() {
		if (id != null) {
			orderMapping.setUniqueID(id);
			orderMappingEJBContainer.update(orderMapping);
		} else {
			orderMappingEJBContainer.save(orderMapping);
		}
		
		return SUCCESS;
	}
	
	public String doDelete() {
		if (id != null) {
			orderMappingEJBContainer.delete(id);
		}
		
		return SUCCESS;
	}

	public OrderMapping getOrderMappingEJBContainer() {
		return orderMappingEJBContainer;
	}

	public void setOrderMappingEJBContainer(OrderMapping orderMappingManager) {
		this.orderMappingEJBContainer = orderMappingManager;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OrderMappingBean getOrderMapping() {
		return orderMapping;
	}

	public void setOrderMapping(OrderMappingBean orderMapping) {
		this.orderMapping = orderMapping;
	}

	public Collection<OrderMappingBean> getOrderMappings() {
		return orderMappings;
	}

	public void setOrderMappings(Collection<OrderMappingBean> orderMappings) {
		this.orderMappings = orderMappings;
	}
	
	public String doXmlUpload() {
		if (orderMappingXml != null && orderMappingXmlFileName.length() > 0) {
			try {
				String orderMapXml = getFileContents(orderMappingXml);
				orderMappingEJBContainer.importXmlOrderMappings(orderMapXml);
				orderMappingXml.delete();
			} catch (Exception e) {
				e.printStackTrace();
				return ERROR;
			}
		}
		
		return SUCCESS;
	}
	
	private String getFileContents(File file) throws FileNotFoundException, IOException {
		FileReader fRead = new FileReader(file);
		BufferedReader bRead = new BufferedReader(fRead);
	
		String line, contents = "";
		try {
			while((line = bRead.readLine()) != null) {
				contents += line;
			}
		} finally {
			bRead.close();
			fRead.close();
		}
		
		return contents;
	}
	
	public Map<String, String> getKeys() {
		return ListHelper.stringListableToMap(OrderKey.values());
	}

	public File getOrderMappingXml() {
		return orderMappingXml;
	}

	public void setOrderMappingXml(File orderMappingXml) {
		this.orderMappingXml = orderMappingXml;
	}

	public String getOrderMappingXmlFileName() {
		return orderMappingXmlFileName;
	}

	public void setOrderMappingXmlFileName(String orderMappingXmlFileName) {
		this.orderMappingXmlFileName = orderMappingXmlFileName;
	}

	public String getOrderMappingXmlContentType() {
		return orderMappingXmlContentType;
	}

	public void setOrderMappingXmlContentType(String orderMappingXmlContentType) {
		this.orderMappingXmlContentType = orderMappingXmlContentType;
	}
	
	public String getOrderKey() {
		return (orderMapping != null && orderMapping.getOrderKey() != null) ? orderMapping.getOrderKey().name() : null;
	}
	
	public void setOrderKey(String orderKey) {
		orderMapping.setOrderKey(OrderKey.valueOf(orderKey));
	}
	
}
