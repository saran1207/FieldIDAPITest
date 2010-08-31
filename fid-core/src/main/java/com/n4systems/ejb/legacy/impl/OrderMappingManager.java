package com.n4systems.ejb.legacy.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import javax.persistence.EntityManager;
import javax.persistence.Query;

import rfid.ejb.entity.OrderMappingBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.OrderMapping;
import com.n4systems.model.OrderKey;
import com.n4systems.model.Tenant;
import com.n4systems.util.OrderMappingXmlParser;


public class OrderMappingManager implements OrderMapping {
	 private PersistenceManager persistenceManager;

	public OrderMappingManager() {
	}
	
	public OrderMappingManager(EntityManager em) {
		this.persistenceManager = new PersistenceManagerImpl(em);
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<String, OrderKey> getKeyMappings(Tenant tenant, String externalSourceID) {
		Query query = persistenceManager.getEntityManager().createQuery("from OrderMappingBean om where om.organizationID = :oID and om.externalSourceID = :esID");
		query.setParameter("oID", tenant.getName());
		query.setParameter("esID", externalSourceID);
		
		List<OrderMappingBean> orderMappings = query.getResultList();
		
		Map<String, OrderKey> keyMap = new HashMap<String, OrderKey>();
		
		if (orderMappings != null) {
			for (OrderMappingBean orderMapping : orderMappings) {
				keyMap.put(orderMapping.getSourceOrderKey(), orderMapping.getOrderKey());
			}
		}
		
		return keyMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<OrderMappingBean> getOrganizationMappings() {
		Query query = persistenceManager.getEntityManager().createQuery("from OrderMappingBean om order by om.organizationID, om.externalSourceID");
		return query.getResultList();
	}

	public void save(OrderMappingBean orderMapping) {
		persistenceManager.getEntityManager().persist(orderMapping);		
	}

	public void update(OrderMappingBean orderMapping) {
		persistenceManager.getEntityManager().merge(orderMapping);
	}
	
	public void delete(Long uniqueID) {
		OrderMappingBean orderMapping = persistenceManager.getEntityManager().find(OrderMappingBean.class, uniqueID);
		persistenceManager.getEntityManager().remove(orderMapping);
	}
	
	public OrderMappingBean getOrderMapping(Long uniqueID) {
		return (OrderMappingBean)persistenceManager.getEntityManager().find(OrderMappingBean.class, uniqueID);
	}
	
	public void importXmlOrderMappings(String xml) {
		OrderMappingXmlParser parser = new OrderMappingXmlParser();
		
		List<OrderMappingBean> orderMappings = parser.parse(xml);
		for(OrderMappingBean orderMapping: orderMappings) {
			save(orderMapping);
		}
	}

}
