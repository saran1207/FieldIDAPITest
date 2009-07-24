package com.n4systems.fieldid.permissions;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.utils.ActionInvocationWrapper;
import com.n4systems.model.ExtendedFeature;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ExtendedFeatureInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;

	
	
	@Override
	public String intercept(ActionInvocation call) throws Exception {
		ActionInvocationWrapper wrapper = new ActionInvocationWrapper(call);
		AbstractAction action = wrapper.getAction();
		String methodName = getMethodName(call);
		Class<?> actionClass = action.getClass();
		
		if (action.getTenant() != null) {
			ExtendedFeature requiredFeature = findRequiredExtendedFeature(action, methodName, actionClass);
			if (requiredFeature != null && !action.getTenant().hasExtendedFeature(requiredFeature)) {
				action.addActionErrorText("permission.require_extended_feature");
				wrapper.getRequest().setAttribute("requiredFeature", requiredFeature);
						
				return "no_permission";
			}
		}
		return call.invoke();
	}


	private ExtendedFeature findRequiredExtendedFeature(AbstractAction action, String methodName, Class<?> actionClass) throws NoSuchMethodException {
		ExtendedFeature requiredFeature = null;
		ExtendedFeatureFilter methodFilter = actionClass.getMethod(methodName).getAnnotation(ExtendedFeatureFilter.class);
		ExtendedFeatureFilter classFilter = actionClass.getAnnotation(ExtendedFeatureFilter.class); 
		
		if (methodFilter != null) {
			requiredFeature = methodFilter.requiredFeature();
		} else if (classFilter != null) {
			requiredFeature = classFilter.requiredFeature();
		}
		return requiredFeature;
	}
	

	private String getMethodName(ActionInvocation call) {
		String methodName = call.getProxy().getMethod();
		if (!methodName.equals("execute")) {
			char firstLetter = methodName.charAt(0);
			methodName = methodName.substring(1);
			methodName = "do" + Character.toUpperCase(firstLetter) + methodName;
		}
		return methodName;
	}

}
