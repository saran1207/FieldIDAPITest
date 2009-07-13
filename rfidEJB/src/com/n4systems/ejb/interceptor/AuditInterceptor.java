package com.n4systems.ejb.interceptor;

import com.n4systems.security.AuditLogger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class AuditInterceptor {
	
	@AroundInvoke
	public Object audit(InvocationContext ctx) throws Exception {
		AuditLogger logger = new AuditLogger();
		
		Exception ex = null;
		try {
			return ctx.proceed();
		} catch(Exception e) {
			// capture the exception so that it can be logged in the finally block and re-throw it 
			ex = e;
			throw e;
		} finally {
			logger.audit(ctx, ex);
		}
	}
	
}
