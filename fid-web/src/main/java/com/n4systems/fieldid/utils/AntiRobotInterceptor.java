package com.n4systems.fieldid.utils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;

public class AntiRobotInterceptor extends AbstractInterceptor {

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletResponse servletResponse = ServletActionContext.getResponse();
        servletResponse.setHeader("X-Robots-Tag", "noindex, nofollow, noarchive");
        return invocation.invoke();
    }

}
