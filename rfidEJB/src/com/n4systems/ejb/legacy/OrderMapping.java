package com.n4systems.ejb.legacy;

import java.util.List;
import java.util.Map;

import rfid.ejb.entity.OrderMappingBean;

import com.n4systems.model.OrderKey;
import com.n4systems.model.Tenant;

public interface OrderMapping {
	
	/**
	 * Loads all the mappings for a given organization and their source. 
	 * @param organizationID
	 * @param externalSourceID
	 * @return a hashMap who's key is the internal order key name and value is the external source orders key name
	 */
	public Map<String, OrderKey> getKeyMappings(Tenant tenant, String externalSourceID);
	
	/**
	 * Loads all the mappings  
	 * @return a hashMap who's key is the internal order key name and value is the external source orders key name
	 */
	public List<OrderMappingBean> getOrganizationMappings();
	
	/**
	 * Saves an order mapping
	 * @param orderMapping
	 */
	public void save(OrderMappingBean orderMapping);
	
	/**
	 * Updates an already existing order mapping
	 * @param orderMapping
	 */
	public void update(OrderMappingBean orderMapping);
	
	/**
	 * Deletes an already existing order mapping
	 * @param orderMapping
	 */
	public void delete(Long uniqueID);
	
	public OrderMappingBean getOrderMapping(Long uniqueID);

	/**
	 * Imports Order mappings from XML
	 * @param Xml Xml order mapping data
	 */
	public void importXmlOrderMappings(String Xml);
	
}
