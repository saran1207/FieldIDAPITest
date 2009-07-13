package com.n4systems.fieldid.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.n4systems.services.Initializer;
import com.n4systems.services.SetupDataLastModUpdateServiceInitializer;
import com.n4systems.taskscheduling.TaskSchedulerBootstraper;

public class ApplicationBootstrap extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/** Array of startup services. Will be initialized in order.*/
	private static final Initializer[] initializers = { 
		new TaskSchedulerBootstraper(), 
		new SetupDataLastModUpdateServiceInitializer() 
	}; 
	
	private static Logger logger = Logger.getLogger(ApplicationBootstrap.class);
	
	public void init() throws ServletException {
		
		for (Initializer init: initializers) {
			try {
				init.initialize();
			} catch(Throwable t) {
				logger.error(init.getClass() + ": Failed initialization", t);
			}
		}
	}


}
