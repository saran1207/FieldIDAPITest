package com.n4systems.fieldid.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.n4systems.services.Initializer;
import com.n4systems.services.RemoteOrderManagerServiceInitializer;
import com.n4systems.taskscheduling.TaskSchedulerBootstraper;
import com.n4systems.taskscheduling.task.SignUpPackageSyncTaskInitializer;

public class ApplicationBootstrap extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/** Array of startup services. Will be initialized in order.*/
	
	// FIXME DD : note that this technique of initializing will not work in clustered environment because this servlet will be initialized > 1 times. 
	//   an alternate approach might be to make a collection of initializing beans that do their stuff in afterPropertySet(). 
	//
	//   e.g. in @Config file....
	//   @Bean public TaskSchedulerBootstraper foo()...
	//
	//   public class TaskSchedulerBootstraper implements Initializer, InitializingBean { 
	//     void afterPropertiesSet() { initialize(); }
	//	 }
	
	private static final Initializer[] initializers = {
		new TaskSchedulerBootstraper(),
		new SignUpPackageSyncTaskInitializer(),
		new RemoteOrderManagerServiceInitializer()
	};
	
	private static Logger logger = Logger.getLogger(ApplicationBootstrap.class);

	
	@Override
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
