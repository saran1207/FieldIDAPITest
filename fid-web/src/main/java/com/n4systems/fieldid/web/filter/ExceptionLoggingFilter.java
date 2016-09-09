package com.n4systems.fieldid.web.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

public class ExceptionLoggingFilter implements Filter {

    private static Logger logger = Logger.getLogger(ExceptionLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (ServletException e) {
            logger.error("Servlet exception occurred processing request", e);
            throw e;
        }
    }

    @Override
    public void destroy() {
    }

}
