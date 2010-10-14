package com.n4systems.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface RemoteOrderManager extends Remote {
	public void processRemoteOrders(Map<String, Object> unmappedOrders) throws RemoteException;
}
