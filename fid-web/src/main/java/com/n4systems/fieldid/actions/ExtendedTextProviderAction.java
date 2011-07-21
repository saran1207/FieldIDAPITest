package com.n4systems.fieldid.actions;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.n4systems.fieldid.actions.utils.WebSession;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.ValueStack;

public abstract class ExtendedTextProviderAction extends ActionSupport implements WebAction {
	private static final long serialVersionUID = 1L;

	private WebSession session;
	private Map<String, String> langOverrides;
	
	public ActionContext getActionContext() {
		return ActionContext.getContext();
	}
	
	public WebSession getSession() {
		if( session == null ) {
			session = new WebSession(getServletRequest().getSession(false));
		}
		return session;
	}
	
	public HttpServletRequest getServletRequest() {
		return ServletActionContext.getRequest();
	}
	
	public HttpServletResponse getServletResponse() {
		return ServletActionContext.getResponse();
	}
	
	private Map<String, String> getLangOverrides() {
		if (langOverrides == null) {
			 langOverrides = getSession().getTenantLanguageOverrides();
			 
			 // if it's still null, just return an empty map so we don't have to deal with nulls
			 if (langOverrides == null) {
				 langOverrides = new HashMap<String, String>(0);
			 }
		}
		return langOverrides;
	}
	
	@Override
	public String getText(String key) {
		return (hasTenantOverride(key)) ? getMessage(key, null, (Object[])null) : super.getText(key);
	}
	
	@Override
	public String getText(String key, String defaultValue) {
		return (hasTenantOverride(key)) ? getMessage(key, null, (Object[])null) : super.getText(key, defaultValue);
	}
	
	@Override
	public String getText(String key, String defaultValue, String obj) {
		return (hasTenantOverride(key)) ? getMessage(key, null, new Object[] {obj}) : super.getText(key, defaultValue, obj);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public String getText(String key, List args) {
		return (hasTenantOverride(key)) ? getMessage(key, null, args) : super.getText(key, args);
	}
	
	@Override
	public String getText(String key, String[] args) {
		return (hasTenantOverride(key)) ? getMessage(key, null, args) : super.getText(key, args);
	}
		
	@Override
	@SuppressWarnings("unchecked")
	public String getText(String key, String defaultValue, List args) {
		return (hasTenantOverride(key)) ? getMessage(key, null, args) : super.getText(key, defaultValue, args);
	}
	
	@Override
	public String getText(String key, String defaultValue, String[] args) {
		return (hasTenantOverride(key)) ? getMessage(key, null, args) : super.getText(key, defaultValue, args);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public String getText(String key, String defaultValue, List args, ValueStack stack) {
		return (hasTenantOverride(key)) ? getMessage(key, stack, args) : super.getText(key, defaultValue, args, stack);
	}
	
	@Override
	public String getText(String key, String defaultValue, String[] args, ValueStack stack) {
		return (hasTenantOverride(key)) ? getMessage(key, stack, args) : super.getText(key, defaultValue, args, stack);
	}
	
	private boolean hasTenantOverride(String key) {
		return getLangOverrides().containsKey(key);
	}
	
	@SuppressWarnings("unchecked")
	private String getMessage(String key, ValueStack valueStack, List args) {
		return getMessage(key, valueStack, args.toArray());
	}
	
	private String getMessage(String key, ValueStack valueStack, Object[] args) {
		String message = null;
		try {
			String pattern = (valueStack == null) ? getLangOverrides().get(key) : TextParseUtil.translateVariables(getLangOverrides().get(key), valueStack);
			
			message =  (new MessageFormat(pattern, getLocale())).format(args);
		} catch (MissingResourceException e) {}
		
		return message;
	}
}
