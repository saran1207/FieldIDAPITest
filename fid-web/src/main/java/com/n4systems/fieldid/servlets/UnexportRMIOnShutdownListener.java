package com.n4systems.fieldid.servlets;

import com.n4systems.rmi.RemoteOrderManager;
import com.n4systems.services.RemoteOrderManagerServiceInitializer;
import org.apache.log4j.Logger;

import java.rmi.NoSuchObjectException;
import java.rmi.server.UnicastRemoteObject;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class UnexportRMIOnShutdownListener implements ServletContextListener {

    private Logger logger = Logger.getLogger(UnexportRMIOnShutdownListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            RemoteOrderManager remoteOrderManager = RemoteOrderManagerServiceInitializer.getRemoteOrderManager();
            if (remoteOrderManager != null) {
                logger.info("Unexporting remote order manager");
                UnicastRemoteObject.unexportObject(remoteOrderManager, true);
                logger.info("Successfully unexported remote order manager");
            }
        } catch (NoSuchObjectException e) {
            logger.error("Error unexporting Remote Order Manager RMI Service", e);
        }
    }

}
