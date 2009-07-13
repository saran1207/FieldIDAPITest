package com.n4systems.service.ordermapper;

import java.util.Map;

import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

import com.n4systems.ejb.OrderManager;
import com.n4systems.util.ServiceLocator;

public class OrderJMSListenerAction extends AbstractActionLifecycle {
	
	protected ConfigTree	_config;

	public OrderJMSListenerAction(ConfigTree config) { _config = config; }
	
	public Message mapMessage(Message message) {
		
		System.out.println("Order JMS Listener starting..");
		
		System.out.println("Pulling map from message..");
		
		Map<String, Object> mainMessage = (Map<String, Object>)message.getBody().get();

		
		try {
			OrderManager orderManager = ServiceLocator.getOrderManager();

			System.out.println("Processing message..");
			
			orderManager.processOrders(mainMessage);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		System.out.println("JMS Listener done..");
		
		return message;
	}
	
	public Message storeMessage(Message message) {
		
		//System.out.println("I'm in STORE MESSAGE!!!");		
		
		
		return message;
	}

}
