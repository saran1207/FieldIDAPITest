package com.n4systems.fieldid.utils;

import java.util.Collection;
import java.util.Map;

import org.apache.struts2.StrutsStatics;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@SuppressWarnings("serial")
public class FlashScopeInterceptor extends AbstractInterceptor implements StrutsStatics {

	public static final String FLASH_MESSAGES = "flashScope_Messages";
	public static final String FLASH_ERRORS = "flashScope_Errors";

	@SuppressWarnings("unchecked")
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		final ActionContext context = invocation.getInvocationContext();

		Map session = context.getSession();
		Collection<String> errors = (Collection<String>) session.get(FLASH_ERRORS);
		Collection<String> messages = (Collection<String>) session.get(FLASH_MESSAGES);

		session.remove(FLASH_ERRORS);
		session.remove(FLASH_MESSAGES);

		Object action = invocation.getAction();

		if (action instanceof ValidationAware) {

			ValidationAware validationAwareAction = (ValidationAware) action;
			if (errors != null) {
				validationAwareAction.setActionErrors(errors);
			}

			if (messages != null) {
				validationAwareAction.setActionMessages(messages);
			}

		}

		String actionResponse = invocation.invoke();

		if (action instanceof AbstractAction) {
			AbstractAction abstractAction = (AbstractAction) action;
			errors = abstractAction.getFlashErrors();
			messages = abstractAction.getFlashMessages();

			abstractAction.getActionMessages().addAll(messages);
			abstractAction.getActionErrors().addAll(errors);

			session.put(FLASH_ERRORS, errors);
			session.put(FLASH_MESSAGES, messages);
		}

		return actionResponse;
	}

}
