package com.n4systems.rmi;

import java.rmi.RemoteException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.n4systems.ejb.wrapper.OrderManagerEJBContainer;

public class RemoteOrderManagerImpl implements RemoteOrderManager {
	private final Logger logger = Logger.getLogger(RemoteOrderManagerImpl.class);
	
	@Override
	public void processRemoteOrders(Map<String, Object> unmappedOrders) throws RemoteException {
		try {
			new OrderManagerEJBContainer().processOrders(unmappedOrders);
		} catch (Exception e) {
			logger.error("Failed processing unmapped orders", e);
			throw new RemoteException(e.getMessage());
		}
	}
}
