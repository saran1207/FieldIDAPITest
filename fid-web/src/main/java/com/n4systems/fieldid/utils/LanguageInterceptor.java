package com.n4systems.fieldid.utils;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import java.util.Locale;

public class LanguageInterceptor extends AbstractInterceptor {

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        ActionInvocationWrapper invocationWrapper = new ActionInvocationWrapper(invocation);
        AbstractAction action = invocationWrapper.getAction();

        Locale originalLanguage = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();

        ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(action.getSessionUser().getLanguage());

        String result = invocation.invoke();

        ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(originalLanguage);
        return result;
    }
}
