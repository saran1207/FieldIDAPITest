package com.n4systems.security;

import org.apache.log4j.Logger;

import com.n4systems.model.Inspection;

/**
 * A Log wrapper for writing audit logs 
 */
public class AuditLogger {
	private Logger logger = Logger.getLogger(AuditLogger.class);
	private static final char SEPCHAR = '|';
	private static final char EMPTYCHAR = '-';
	private static final String MSG_SUCCESS = "Successful";
	private static final String MSG_FAIL = "Failed";
	
	private final Logger auditLogger;
	private final AuditHandler handler;
	

	
	public AuditLogger(AuditHandler handler) {
		this(handler, Logger.getLogger("AuditLog"));
	}
	
	public AuditLogger(AuditHandler handler, Logger auditLogger) {
		this.handler = handler;
		this.auditLogger = auditLogger;
	}
	
	
	
	
	
	public void audit(String methodName, Inspection inspection, Throwable t) {
		String message = "";
		
		try {
			message = handler.getMessage(inspection);
		} catch(Throwable throwable) {
			// we don't want any problems within the audit handler to affect the actual running of the method
			logger.warn("Failed while generating custom AuditHandler message", throwable);
		}
		
		audit(methodName, message, t);
	}


	
	/**
	 * Writes an audit message.  Logs a success message when t is null,
	 * and failure otherwise
	 * @param Method	The Method invoked
	 * @param message	Optional audit message
	 * @param t			Optional Throwable
	 */
	private void audit(String methodName, String message, Throwable t) {
		auditLogger.info(formatMessage(methodName, message, SecurityContext.getContext(), t));
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
	private String formatMessage(String methodName, String auditMessage, SecurityContext securityContext, Throwable t) {
		StringBuilder message = new StringBuilder();

		writeField(message, System.currentTimeMillis());
		writeField(message, methodName);
		
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
