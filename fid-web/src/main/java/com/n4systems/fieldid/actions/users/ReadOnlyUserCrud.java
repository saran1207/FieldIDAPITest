package com.n4systems.fieldid.actions.users;


import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.security.Permissions;
import com.n4systems.security.UserType;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemUsers})
public class ReadOnlyUserCrud extends UserCrud {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ReadOnlyUserCrud.class);
	
	private String reportName;
	private DownloadLink downloadLink;
	
	public ReadOnlyUserCrud( UserManager userManager, PersistenceManager persistenceManager ) {
		super(userManager, persistenceManager);
	}
	
	@Override
	protected void testRequiredEntities(boolean existing) {
		super.testRequiredEntities(existing);
		if (existing && !user.isReadOnly()) {
			throw new MissingEntityException("another user was loaded for when a read-only user was expected.");
		}
	}
	
	@Override
	public String doCreate(){
		testRequiredEntities(false);
		user.setUserType(UserType.READONLY);
		save();
		return SUCCESS;
	}
	
	@SkipValidation
	public String doExport() {
		try {
			reportName = getText("label.export_file.user");
			downloadLink = getDownloadCoordinator().generateUserExport(getReportName(), getDownloadLinkUrl(), createUserOrgListLoader(), getSecurityFilter());
		} catch (RuntimeException e) {
			logger.error("Unable to execute user export", e);
			addFlashMessage(getText("error.export_failed.user"));
			return ERROR;
		}
		return SUCCESS;
	}	

	private Loader<User> createUserOrgListLoader() {
		return getLoaderFactory().createUserFilteredLoader();
	}	

	@Override
	protected int processPermissions() {
		return Permissions.CUSTOMER;
	}

	@Override
	public boolean isFullUser(){
		return false;
	}
	
	@Override
	public boolean isEmployee() {
		return false;
	}

	@Override
	public boolean isLiteUser() {
		return false;
	}

	@Override
	public boolean isReadOnlyUser() {
		return true;
	}

	public DownloadLink getDownloadLink() {
		return downloadLink;
	}

	public String getReportName() {
		return reportName;
	}	

}
