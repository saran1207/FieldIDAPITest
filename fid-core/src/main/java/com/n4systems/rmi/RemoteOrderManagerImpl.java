package com.n4systems.rmi;

import com.n4systems.ejb.wrapper.OrderManagerEJBContainer;
import org.apache.log4j.Logger;

import java.rmi.RemoteException;
import java.util.Map;

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
