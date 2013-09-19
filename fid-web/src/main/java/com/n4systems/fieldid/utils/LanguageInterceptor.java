package com.n4systems.fieldid.utils;

import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.model.Tenant;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import java.util.Locale;

public class LanguageInterceptor extends AbstractInterceptor {

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        ActionInvocationWrapper invocationWrapper = new ActionInvocationWrapper(invocation);
        Tenant tenant = invocationWrapper.getAction().getTenant();
        Locale language = invocationWrapper.getAction().getLanguage(invocationWrapper.getMethodName());
        Locale originalLanguage = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();
        if (tenant != null) {
            ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(language);
        }
        String result = invocation.invoke();
        ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(originalLanguage);
        return result;
    }
}
