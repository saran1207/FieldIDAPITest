package com.n4systems.ejb.interceptor;

import com.n4systems.util.LoggingHelper;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

public class TimingInterceptor {
	static private final char SEP = '|';
	static private final char START = 'B';
	static private final char END = 'E';
	private final Logger logger = Logger.getLogger(TimingInterceptor.class);
	
	@AroundInvoke
	public Object printTiming(InvocationContext ctx) throws Exception {
		final long startTime = System.currentTimeMillis();
		final String className = ctx.getMethod().getDeclaringClass().getName();
		final String method = LoggingHelper.prepareMethodName(ctx.getMethod());
		
		logger.debug(format(START, startTime, className, method, -1L));
		try {
			return ctx.proceed();
		} finally {
			logger.debug(format(END, startTime, className, method, System.currentTimeMillis() - startTime));
		}
	}
	
	private String format(final char startOrEnd, final long startTime, final String className, final String methodName, final long execTime) {
		return (new StringBuilder(128)).append(startOrEnd).append(SEP).append(startTime).append(SEP).append(className).append(SEP).append(methodName).append(SEP).append(execTime).toString();
	}
}
