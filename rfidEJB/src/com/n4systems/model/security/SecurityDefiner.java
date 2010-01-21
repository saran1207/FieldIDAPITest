package com.n4systems.model.security;

import java.io.Serializable;

import com.n4systems.model.api.Archivable;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.api.HasTenant;
import com.n4systems.model.api.HasUser;

/**
 * Defines the filter field paths to be used in a security filter.
 */
public class SecurityDefiner implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String DEFAULT_TENANT_PATH = "tenant.id";
	public static final String DEFAULT_OWNER_PATH = "owner";
	public static final String DEFAULT_USER_PATH = "user.uniqueID";
	public static final String DEFAULT_STATE_PATH = "state";

	private final String tenantPath;
	private final String ownerPath;
	private final String userPath;
	private final String statePath;
	
	/**
	 * Constructs a SecurityDefiner using custom fields.  Using null for a field will turn off filtering on that field.  Fields are 
	 * given in bean path format.  Note do not specify the id for the owner field or state field.
	 * <pre>
	 * 	Example:
	 * 		new SecurityDefiner("tenant.id", "other.owner", null, "state");
	 * 		will construct a SecurityDefiner filtering on "tenant.id" for the tenant field, "other.owner" for the owner field, "state"
	 * 		for the state field and user filtering disabled.
	 * </pre>
	 * @param tenantPath	path to the Tenant id
	 * @param ownerPath		path to the Owner (BaseOrg or BaseOrg subclass)
	 * @param userPath		path to the User id
	 * @param statePath		path to the State
	 */
	public SecurityDefiner(String tenantPath, String ownerPath, String userPath, String statePath) {
		this.tenantPath = tenantPath;
		this.ownerPath = ownerPath;
		this.userPath = userPath;
		this.statePath = statePath;
	}

	/**
	 * Constructs a SecurityDefiner using default fields.  The boolean arguments turn filtering for each field on when true
	 * and off when false.
	 * <pre>
	 * 	The default fields are as follows:
	 * 		Tenant = {@linkplain #DEFAULT_TENANT_PATH}
	 * 		Owner = {@linkplain #DEFAULT_OWNER_PATH}
	 * 		User = {@linkplain #DEFAULT_USER_PATH}
	 * 		State = {@linkplain #DEFAULT_STATE_PATH}
	 * </pre>
	 * @see #SecurityDefiner(String, String, String, String)
	 * @param tenantFiltered	turns Tenant filtering on/off	
	 * @param ownerFiltered		turns Owner filtering on/off
	 * @param userFiltered		turns User filtering on/off
	 * @param stateFiltered		turns State filtering on/off
	 */
	protected SecurityDefiner(boolean tenantFiltered, boolean ownerFiltered, boolean userFiltered, boolean stateFiltered) {
		this(
			tenantFiltered	? DEFAULT_TENANT_PATH	: null,
			ownerFiltered	? DEFAULT_OWNER_PATH	: null,
			userFiltered	? DEFAULT_USER_PATH		: null,
			stateFiltered	? DEFAULT_STATE_PATH	: null
		);
	}
	
	/**
	 * Constructs a SecurityDefiner using default fields based off the classes implemented interfaces.
	 * <pre>
	 * The Interfaces work as follows:
	 * 	Interface	Turns On
	 * 	{@link HasTenant}	Tenant filtering
	 * 	{@link HasOwner}	Owner filtering
	 * 	{@link HasUser}		User filtering
	 * 	{@link Archivable}	State filtering
	 * </pre>
	 * @see #SecurityDefiner(boolean, boolean, boolean, boolean) Field path defaults
	 * @param clazz	The to construct this definer for
	 */
	public SecurityDefiner(Class<?> clazz) {
		this(
			HasTenant.class.isAssignableFrom(clazz),
			HasOwner.class.isAssignableFrom(clazz),
			HasUser.class.isAssignableFrom(clazz),
			Archivable.class.isAssignableFrom(clazz)
		);
	}

	public boolean isTenantFiltered() {
		return tenantPath != null;
	}

	public boolean isOwnerFiltered() {
		return ownerPath != null;
	}

	public boolean isUserFiltered() {
		return userPath != null;
	}

	public boolean isStateFiltered() {
		return statePath != null;
	}

	public String getTenantPath() {
		return tenantPath;
	}

	public String getOwnerPath() {
		return ownerPath;
	}

	public String getUserPath() {
		return userPath;
	}

	public String getStatePath() {
		return statePath;
	}
}
