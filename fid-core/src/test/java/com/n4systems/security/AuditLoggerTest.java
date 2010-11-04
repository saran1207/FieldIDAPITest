package com.n4systems.security;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import com.n4systems.model.Event;
import org.apache.log4j.Logger;
import org.easymock.Capture;
import org.junit.Test;

import com.n4systems.model.builders.EventBuilder;


public class AuditLoggerTest {

	
	private Event event = EventBuilder.anEvent().build();


	@Test
	public void should_call_handler_for_message() throws Exception {
		AuditHandler auditHandler = createMock(AuditHandler.class);
		expect(auditHandler.getMessage(event)).andReturn("log Message");
		replay(auditHandler);
		
		Log4JAuditLogger sut = new Log4JAuditLogger(auditHandler);
		
		sut.audit("method", event, null);
		
		verify(auditHandler);
	}
	
	
	@Test
	public void should_send_method_name_in_log_message() throws Exception {
		Capture<String> logMessage = new Capture<String>();
		Logger auditLog = createMock(Logger.class);
		auditLog.info(capture(logMessage));
		replay(auditLog);
		
		
		Log4JAuditLogger sut = new Log4JAuditLogger(new NullAuditHandler(), auditLog);
		
		sut.audit("methodName", event, null);
		assertThat(logMessage.getValue(), containsString("methodName"));
	}
	
	@Test
	public void should_report_success_when_throwable_is_null() throws Exception {
		Capture<String> logMessage = new Capture<String>();
		Logger auditLog = createMock(Logger.class);
		auditLog.info(capture(logMessage));
		replay(auditLog);
		
		
		Log4JAuditLogger sut = new Log4JAuditLogger(new NullAuditHandler(), auditLog);
		
		sut.audit("methodName", event, null);
		assertThat(logMessage.getValue(), containsString("Success"));
	}
	
	@Test
	public void should_report_failure_when_throwable_is_not_null() throws Exception {
		Capture<String> logMessage = new Capture<String>();
		Logger auditLog = createMock(Logger.class);
		auditLog.info(capture(logMessage));
		replay(auditLog);
		
		
		Log4JAuditLogger sut = new Log4JAuditLogger(new NullAuditHandler(), auditLog);
		
		sut.audit("methodName", event, new Exception());
		assertThat(logMessage.getValue(), containsString("Failed"));
	}
	
	
	@Test
	public void should_include_the_audit_handler_message_in_the_log() throws Exception {
		Capture<String> logMessage = new Capture<String>();
		Logger auditLog = createMock(Logger.class);
		auditLog.info(capture(logMessage));
		replay(auditLog);
		
		AuditHandler auditHandler = new AuditHandler() {
			
			@Override
			public String getMessage(Event event) {
				return "Audit Handler Message ----____----";
			}
		};
		
		
		Log4JAuditLogger sut = new Log4JAuditLogger(auditHandler, auditLog);
		
		sut.audit("methodName", event, new Exception());
		assertThat(logMessage.getValue(), containsString("Audit Handler Message ----____----"));
	}
	
	
	
	@Test
	public void should_still_log_if_audit_handler_throws_an_exception() throws Exception {
		Logger auditLog = createMock(Logger.class);
		auditLog.info((String)anyObject());
		replay(auditLog);
		
		AuditHandler auditHandler = new AuditHandler() {
			@Override
			public String getMessage(Event event)  {
				throw new RuntimeException();
			}
		};
		
		
		Log4JAuditLogger sut = new Log4JAuditLogger(auditHandler, auditLog);
		
		sut.audit("methodName", event, new Exception());
		
		verify(auditLog);
	}
	
	
}
