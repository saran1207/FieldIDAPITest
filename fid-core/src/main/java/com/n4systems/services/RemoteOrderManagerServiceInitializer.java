package com.n4systems.services;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

import com.n4systems.rmi.RemoteOrderManager;
import com.n4systems.rmi.RemoteOrderManagerImpl;

public class RemoteOrderManagerServiceInitializer implements Initializer {
	private static final String RMI_NAME = "RemoteOrderManager";
	private static final int RMI_REGISTRY_PORT = 6666;
	
	private final Logger logger = Logger.getLogger(RemoteOrderManagerServiceInitializer.class);
	
	private static RemoteOrderManager orderManager;
	
	@Override
	public void initialize() {
        try {
        	orderManager = new RemoteOrderManagerImpl();
        	RemoteOrderManager orderManagerStub = (RemoteOrderManager)UnicastRemoteObject.exportObject(orderManager, 0);
        	
            Registry registry = LocateRegistry.createRegistry(RMI_REGISTRY_PORT);
            registry.bind(RMI_NAME, orderManagerStub);

            logger.info("RemoteOrderManager RMI service initialized");
        } catch (Exception e) {
            logger.error("Unable to initialize RemoteOrderManager RMI service", e);
        }
	}

	@Override
	public void uninitialize() {
	}

}
