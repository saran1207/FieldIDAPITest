package com.n4systems.security;

import com.n4systems.util.LoggingHelper;

import java.lang.reflect.Method;

import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

/**
 * A Log wrapper for writing audit logs 
 */
public class AuditLogger {
	private Logger logger = Logger.getLogger(AuditLogger.class);
	private static final char SEPCHAR = '|';
	private static final char EMPTYCHAR = '-';
	private static final String MSG_SUCCESS = "Successful";
	private static final String MSG_FAIL = "Failed";
	private Logger auditLogger = Logger.getLogger("AuditLog");
	
	public AuditLogger() {}
	
	/**
	 * Writes to the audit log, a successful invocation, using information from the InvocationContext
	 * @param ctx	Current InvocationContext
	 * @see #audit(InvocationContext, Throwable)
	 */
	public void audit(InvocationContext ctx) {
		audit(ctx, null);
	}
	
	/**
	 * Writes to the audit log, using information from the InvocationContext.  Logs a success message when t is null,
	 * and failure otherwise.  Looks for a {@see CustomAuditHandler} on the method invoked and generates the message 
	 * from there.  Message is null if the method is missing a CustomAuditHandler annotation
	 * @param ctx	Current InvocationContext
	 * @param t		Optional Throwable
	 * @see #audit(Method, String, Throwable)
	 */
	public void audit(InvocationContext ctx, Throwable t) {
		String message = null;
		Method method = ctx.getMethod();
		
		try {
			// if this method defined a custom handler, then we will set the audit message
			// via that handler
			CustomAuditHandler customHandler = method.getAnnotation(CustomAuditHandler.class);
			if (customHandler != null) {
				AuditHandler handler = customHandler.value().newInstance();
				message = handler.getMessage(ctx);
			}
		} catch(Throwable throwable) {
			// we don't want any problems within the audit handler to affect the actual running of the method
			logger.warn("Failed while generating custom AuditHandler message", throwable);
		}
		
		audit(method, message, t);
	}
	
	/**
	 * Writes an audit message.  Logs a success message when t is null,
	 * and failure otherwise
	 * @param Method	The Method invoked
	 * @param message	Optional audit message
	 * @param t			Optional Throwable
	 */
	public void audit(Method method, String message, Throwable t) {
		auditLogger.info(formatMessage(method, message, SecurityContext.getContext(), t));
	}
	
	/**
	 * Formats audit data into the form:<p />
	 * &lt;time stamp&gt;|&lt;method name and signature&gt;|&lt;tenant info&gt;|&lt;user info&gt;|&lt;jesssionid&gt;|&lt;audit message&gt;|&lt;success/failure&gt;|&lt;exception message on failure&gt;
	 * 
	 * @param method			The method called
	 * @param auditMessage		Optional message
	 * @param securityContext	Current SecurityContext
	 * @param t					Optional Exception if the request failed
	 * @return 					A formatted audit log String
	 */
	private String formatMessage(Method method, String auditMessage, SecurityContext securityContext, Throwable t) {
		StringBuilder message = new StringBuilder();

		writeField(message, System.currentTimeMillis());
		writeField(message, LoggingHelper.prepareMethodName(method));
		
		if (securityContext != null) {
			writeField(message, securityContext.getUser().getTenant());
			writeField(message, securityContext.getUser());
			writeField(message, securityContext.getSessionId());
		} else {
			writeEmpty(message);
			writeEmpty(message);
			writeEmpty(message);
		}
		
		writeField(message, auditMessage);
		
		if (t != null) {
			writeField(message, MSG_FAIL);
			writeField(message, t.getMessage());
		} else {
			writeField(message, MSG_SUCCESS);
			writeEmpty(message);
		}
		
		return message.toString();
	}
	
	private void writeField(StringBuilder message, Object field) {
		if (field != null) {
			message.append(field.toString());
			message.append(SEPCHAR);
		} else {
			writeEmpty(message);
		}
	}
	
	private void writeEmpty(StringBuilder message) {
		message.append(EMPTYCHAR);
		message.append(SEPCHAR);
	}
}
