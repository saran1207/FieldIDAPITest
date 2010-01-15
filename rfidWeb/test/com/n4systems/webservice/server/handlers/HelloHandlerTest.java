package com.n4systems.webservice.server.handlers;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.n4systems.webservice.dto.hello.HelloRequest;
import com.n4systems.webservice.dto.hello.HelloResponse;

public class HelloHandlerTest {

	@Test
	public void sending_a_valid_request_returns_a_hello_response() {
		HelloHandler helloHandler = new HelloHandler();
		HelloResponse response = helloHandler.sayHello(validHelloRequest());
		assertNotNull(response);
	}
	
	@Test
	public void properly_logs_information_in_request() {
		
		HelloRequest validHelloRequest = validHelloRequest();
		
		final Logger mockLogger = createMock(Logger.class);
		mockLogger.info(getLogMessage(validHelloRequest));
		replay(mockLogger);		
		
		HelloHandler helloHandler = new HelloHandler() {
			@Override
			protected Logger getLogger() {
				return mockLogger;
			}
		};
		
		helloHandler.sayHello(validHelloRequest);
		
		verify(mockLogger);				
	}	
	
	private String getLogMessage(HelloRequest request) {
		return request.getDeviceId()+" : "+
			   request.getSoftwareVersion()+" : "+
			   request.getTenantName()+" ("+
			   request.getTenantId()+") : "+
			   request.getUserName()+" ("+
			   request.getUserId()+")";
	}
		
	private HelloRequest validHelloRequest() {
		HelloRequest helloRequest = new HelloRequest();
		helloRequest.setDeviceId("123456");
		helloRequest.setSoftwareVersion("1.17");
		helloRequest.setTenantId(1);
		helloRequest.setTenantName("Some Tenant");
		helloRequest.setUserId(2);
		helloRequest.setUserName("username");
		
		return helloRequest;
	}
	
	
}
