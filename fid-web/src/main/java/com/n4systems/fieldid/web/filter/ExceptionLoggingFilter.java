package com.n4systems.fieldid.web.filter;

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

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
