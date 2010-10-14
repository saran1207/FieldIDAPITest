package com.n4systems.service.ordermapper;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import org.jboss.soa.esb.actions.AbstractActionLifecycle;
import org.jboss.soa.esb.helpers.ConfigTree;
import org.jboss.soa.esb.message.Message;

import com.n4systems.rmi.RemoteOrderManager;

public class OrderJMSListenerAction extends AbstractActionLifecycle {
	private static final String RMI_NAME = "RemoteOrderManager";
	private static final int RMI_PORT = 6666;
	
	protected ConfigTree	_config;

	public OrderJMSListenerAction(ConfigTree config) { _config = config; }
	
	public Message mapMessage(Message message) {
		
		System.out.println("Order JMS Listener starting..");
		
		System.out.println("Pulling map from message..");
		
		Map<String, Object> mainMessage = (Map<String, Object>)message.getBody().get();


		try {
			Registry registry = LocateRegistry.getRegistry(RMI_PORT);
			RemoteOrderManager orderManager = (RemoteOrderManager)registry.lookup(RMI_NAME);
			orderManager.processRemoteOrders(mainMessage);
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
