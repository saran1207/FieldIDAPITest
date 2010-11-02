/**
 * 
 */
package com.n4systems.handlers.creator;

import com.n4systems.model.Event;
import com.n4systems.security.AuditLogger;

final class NullAuditLogger implements AuditLogger {
	@Override
	public void audit(String methodName, Event event, Throwable t) {
	}
}