package com.n4systems.webservice.server.handlers;

import com.n4systems.services.config.ConfigService;
import com.n4systems.webservice.dto.hello.HelloRequest;
import com.n4systems.webservice.dto.hello.HelloResponse;
import org.apache.log4j.Logger;

public class HelloHandler {
	
	private static Logger logger;
	private HelloRequest request;
	
	public HelloHandler() {
		logger = getLogger();		
	}
	
	public HelloResponse sayHello(HelloRequest request) {
		this.request = request;
		logger.info(getLogMessage());
		return new HelloResponse(ConfigService.getInstance());
	}
	
	private String getLogMessage() {
		return request.getDeviceId()+" : "+
			   request.getSoftwareVersion()+" : "+
			   request.getTenantName()+" ("+
			   request.getTenantId()+") : "+
			   request.getUserName()+" ("+
			   request.getUserId()+")";
	}
	
	protected Logger getLogger() {
		return Logger.getLogger(HelloHandler.class);
	}
}
