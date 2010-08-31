package com.n4systems.webservice.server.handlers;

import org.apache.log4j.Logger;

import com.n4systems.webservice.dto.hello.HelloRequest;
import com.n4systems.webservice.dto.hello.HelloResponse;

public class HelloHandler {
	
	private static Logger logger;
	private HelloRequest request;
	
	public HelloHandler() {
		logger = getLogger();		
	}
	
	public HelloResponse sayHello(HelloRequest request) {
		
		this.request = request;
		
		logger.info(getLogMessage());
		
		return new HelloResponse();
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
