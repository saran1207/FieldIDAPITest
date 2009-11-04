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
		String methodName = wrapper.getMethodName();
		Class<?> actionClass = action.getClass();
		
		if (action.getTenant() != null) {
			ExtendedFeature requiredFeature = findRequiredExtendedFeature(action, methodName, actionClass);
			if (requiredFeature != null && !action.getSecurityGuard().isExtendedFeatureEnabled(requiredFeature)) {
				action.addActionErrorText("permission.require_extended_feature");
				wrapper.getRequest().setAttribute("requiredFeature", requiredFeature);
						
				return "feature_required";
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


}
