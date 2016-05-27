package com.n4systems.fieldid.servlets;

import com.n4systems.services.Initializer;
import com.n4systems.services.RemoteOrderManagerServiceInitializer;
import com.n4systems.taskscheduling.TaskSchedulerBootstrapper;
import com.n4systems.util.S3Initializer;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationBootstrap implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		for (Initializer init: new Initializer[] {
				new MigrationInitializer(),
				new TaskSchedulerBootstrapper(),
				new RemoteOrderManagerServiceInitializer(),
				new S3Initializer()
		}) {
			try {
				init.initialize();
			} catch(Exception e) {
				Logger.getLogger(ApplicationBootstrap.class).fatal("Field ID Initialization Failed", e);
				throw new Error("Field ID Initialization Failed", e);
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {}
}
