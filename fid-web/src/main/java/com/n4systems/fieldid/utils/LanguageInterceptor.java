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

        boolean override = action.isOverrideLanguage(invocationWrapper.getMethodName());
        Locale originalLanguage = ThreadLocalInteractionContext.getInstance().getUserThreadLanguage();
        boolean originalOverride = ThreadLocalInteractionContext.getInstance().isForceDefaultLanguage();

//        ThreadLocalInteractionContext.getInstance().setForceDefaultLanguage(override);
        ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(null);

        String result = invocation.invoke();

//        ThreadLocalInteractionContext.getInstance().setUserThreadLanguage(originalLanguage);
//        ThreadLocalInteractionContext.getInstance().setForceDefaultLanguage(originalOverride);
        return result;
    }
}
