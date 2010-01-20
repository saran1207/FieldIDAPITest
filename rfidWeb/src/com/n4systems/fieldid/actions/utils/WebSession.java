package com.n4systems.fieldid.actions.utils;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import rfid.web.helper.SessionEulaAcceptance;
import rfid.web.helper.SessionUser;

import com.n4systems.fieldid.actions.search.InspectionReportAction;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.permissions.UserSecurityGuard;
import com.n4systems.fieldid.ui.seenit.SeenItRegistry;
import com.n4systems.fieldid.ui.seenit.SeenItRegistryDatabaseDataSource;
import com.n4systems.fieldid.ui.seenit.SeenItRegistryImpl;
import com.n4systems.fieldid.viewhelpers.SearchContainer;
import com.n4systems.handlers.creator.signup.model.SignUpRequest;
import com.n4systems.util.HashCode;

@SuppressWarnings("unchecked")
public class WebSession extends AbstractMap<String, Object> implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String KEY_SESSION_USER = "sessionUser";
	private static final String KEY_SECURITY_GUARD = "securityGaurd";
	private static final String KEY_USER_SECURITY_GUARD = "userSecurityGuard";
	private static final String KEY_TENANT_LANG_OVERRIDES = "TENANT_LANG_OVERRIDES";
	private static final String KEY_SIGNUP = "signUp";
	private static final String KEY_EULA_ACCEPTANCE = "eula_acceptance";
	private static final String KEY_SEEN_IT_REGISTRY = "seenItRegistry";
	private static final String VENDOR_CONTEXT = "vendor_context";
	private static final String KEY_QUICK_SETUP_WIZARD_IMPORTS = "qsw_import";
	
	
	private final HttpSession session;
	
	public WebSession(HttpSession session) {
		this.session = session;
	}
	
	public HttpSession getHttpSession() {
		return session;
	}
	
	public String getId() {
		if (session == null) {
			return null;
		}
		return session.getId();
	}
	
	private <T> T get(String key, Class<T> clazz) {
		return (T)get(key);
	}
	
	public SystemSecurityGuard getSecurityGuard() {
		return get(KEY_SECURITY_GUARD, SystemSecurityGuard.class);
	}
	
	public void setSecurityGuard(SystemSecurityGuard securityGuard) {
		put(KEY_SECURITY_GUARD, securityGuard);
	}
	
	public UserSecurityGuard getUserSecurityGuard() {
		return get(KEY_USER_SECURITY_GUARD, UserSecurityGuard.class);
	}
	
	public void setUserSecurityGuard(UserSecurityGuard userSecurityGuard) {
		put(KEY_USER_SECURITY_GUARD, userSecurityGuard);
	}
	
	public void clearSecurityGuard() {
		remove(KEY_SECURITY_GUARD);
	}
	
	public SessionUser getSessionUser() {
		return get(KEY_SESSION_USER, SessionUser.class);
	}
	
	public void setSessionUser(SessionUser sessionUser) {
		put(KEY_SESSION_USER, sessionUser);
	}
	
	public void clearSessionUser() {
		remove(KEY_SESSION_USER);
	}
	
	
	public SessionEulaAcceptance getEulaAcceptance() {
		return get(KEY_EULA_ACCEPTANCE, SessionEulaAcceptance.class);
	}
	
	
	public void setEulaAcceptance(SessionEulaAcceptance sessionEulaAcceptance) {
		put(KEY_EULA_ACCEPTANCE, sessionEulaAcceptance);
	}
	
	
	public SearchContainer getReportCriteria() {
		// TODO: the report criteria key should be moved here
		return get(InspectionReportAction.REPORT_CRITERIA, SearchContainer.class);
	}
	
	public void setReportCriteria(SearchContainer container) {
		put(InspectionReportAction.REPORT_CRITERIA, container);
	}
	
	public void clearReportCriteria() {
		remove(InspectionReportAction.REPORT_CRITERIA);
	}
	
	public Map<String, String> getTenantLanguageOverrides() {
		return get(KEY_TENANT_LANG_OVERRIDES, Map.class);
	}

	public void setTenantLanguageOverrides(Map<String, String> language) {
		put(KEY_TENANT_LANG_OVERRIDES, language);
	}
	
	public void clearTenantLanguageOverrides() {
		remove(KEY_TENANT_LANG_OVERRIDES);
	}
	
	public SignUpRequest getSignUpRequest() {
		return get(KEY_SIGNUP, SignUpRequest.class);
	}
	
	public void setSignUpRequest(SignUpRequest request) {
		put(KEY_SIGNUP, request);
	}
	
	public void clearSignUpRequest() {
		remove(KEY_SIGNUP);
	}
	
	public void setVendorContext(Long vendorContext) {
		put(VENDOR_CONTEXT, vendorContext);
	}
	
	public Long getVendorContext() {
		return get(VENDOR_CONTEXT, Long.class);
	}
	
	@Override
	public Set<Map.Entry<String, Object>> entrySet() {
		if (session == null) {
			return Collections.EMPTY_SET;
		}
		
		Set<Map.Entry<String, Object>> entrySet = new HashSet<Map.Entry<String, Object>>();
		synchronized (session) {
			Enumeration keys = session.getAttributeNames();
			
			while (keys.hasMoreElements()) {
				final String key = keys.nextElement().toString();
				
				entrySet.add(new Map.Entry<String, Object>() {
					
					public String getKey() {
						return key;
					}

					public Object getValue() {
						return get(key);
					}

					public Object setValue(Object value) {
						return put(key, value);
					}
					
					public boolean equals(Object obj) {
						if (obj == null) {
							return false;
						}
						Map.Entry<String, Object> other = (Map.Entry<String, Object>)obj;
						String myKey = key;
						String otherKey = other.getKey();
						Object myValue = getValue();
						Object otherValue = other.getValue();
                        
						// both keys and values must be the same
						return (myKey == null) ? (otherKey == null) : myKey.equals(otherKey) && (myValue == null) ? (otherValue == null) : myValue.equals(otherValue);
                    }

                    public int hashCode() {
                        return HashCode.newHash().add(key).add(getValue()).toHash();
                    }
				});
			}
		}
				
		return entrySet;
	}

	@Override
	public Object get(Object key) {
		if (session == null) {
			return null;
		}
		
		synchronized (session) {
			return session.getAttribute(String.valueOf(key));
		}
	}
	
	@Override
	public Object put(String key, Object value) {
		if (session == null) {
			return null;
		}
		
		Object oldValue = null;
		synchronized (session) {
			oldValue = get(key);
			session.setAttribute(key, value);
        }
		return oldValue;
	}

	@Override
	public Object remove(Object key) {
		if (session == null) {
			return null;
		}
		
		Object oldValue = null;
		synchronized (session) {
			oldValue = get(key);
			session.removeAttribute(String.valueOf(key));
		}
		return oldValue;
	}
	
	@Override
	public void clear() {
		if (session == null ) {
			return;
		}
		
		synchronized (session) {
			Enumeration<String> keys = session.getAttributeNames();
			while(keys.hasMoreElements()) {
				session.removeAttribute(keys.nextElement());
			}
		}
	}

	@Override
	public boolean containsKey(Object key) {
		if (session == null) {
			return false;
		}
		
		boolean found = false;
		synchronized (session) {
			Enumeration<String> keys = session.getAttributeNames();
			String currentKey;
			while(keys.hasMoreElements() && !found) {
				currentKey = keys.nextElement();
				found = (currentKey == null) ? (key == null) : currentKey.equals(key);
			}
		}
		return found;
	}

	@Override
	public boolean containsValue(Object value) {
		if (session == null) {
			return false;
		}
		
		boolean found = false;
		synchronized (session) {
			Enumeration<String> keys = session.getAttributeNames();
			Object currentValue;
			while(keys.hasMoreElements() && !found) {
				currentValue = session.getAttribute(keys.nextElement());
				found = (currentValue == null) ? (value == null) : currentValue.equals(value);
			}
		}
		return found;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Set<String> keySet() {
		if (session == null) {
			return Collections.EMPTY_SET;
		}
		
		Set<String> keySet = new HashSet<String>();
		synchronized (session) {
			Enumeration<String> keys = session.getAttributeNames();
			while(keys.hasMoreElements()) {
				keySet.add(keys.nextElement());
			}
		}
		return keySet;
	}

	@Override
	public int size() {
		if (session == null) {
			return 0;
		}
		int count = 0;
		synchronized (session) {
			Enumeration<String> keys = session.getAttributeNames();
			while(keys.hasMoreElements()) {
				count++;
			}
		}
		return count;
	}

	@Override
	public Collection<Object> values() {
		if (session == null) {
			return Collections.EMPTY_LIST;
		}
		Collection<Object> values = new ArrayList<Object>();
		synchronized (session) {
			Enumeration<String> keys = session.getAttributeNames();
			while(keys.hasMoreElements()) {
				values.add(session.getAttribute(keys.nextElement()));
			}
		}
		return values;
	}

	public SeenItRegistry getSeenItRegistry() {
		SeenItRegistry seenItRegistry = (SeenItRegistry)get(KEY_SEEN_IT_REGISTRY);
		if (seenItRegistry == null) { 
			seenItRegistry = new SeenItRegistryImpl(new SeenItRegistryDatabaseDataSource(getSessionUser().getId()));
			put(KEY_SEEN_IT_REGISTRY, seenItRegistry);
		}
		
		return seenItRegistry;
	}
	
	
	public Set<Long> getQuickSetupWizardImports() {
		Set<Long> imports = (Set<Long>)get(KEY_QUICK_SETUP_WIZARD_IMPORTS);
		if (imports == null) {
			imports = new HashSet<Long>();
			put(KEY_QUICK_SETUP_WIZARD_IMPORTS, imports);
		}
		return imports;
	}
	
	public void clearQuickSetupWizardImports() {
		remove(KEY_QUICK_SETUP_WIZARD_IMPORTS);
	}


}
