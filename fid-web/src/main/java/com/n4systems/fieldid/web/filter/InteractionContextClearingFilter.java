package com.n4systems.fieldid.web.filter;


import com.n4systems.fieldid.context.ThreadLocalInteractionContext;

import javax.servlet.*;
import java.io.IOException;

public class InteractionContextClearingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            ThreadLocalInteractionContext.getInstance().clear();
        }
    }

    @Override
    public void destroy() {
    }

}
