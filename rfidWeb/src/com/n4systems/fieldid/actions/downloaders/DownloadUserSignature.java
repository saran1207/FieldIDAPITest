package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileNotFoundException;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;

public class DownloadUserSignature extends AbstractDownloadAction {

	private final UserManager userManager;

	private Long userId;
	private User user;
	
	public DownloadUserSignature(PersistenceManager persistenceManager, UserManager userManager) {
		super(persistenceManager);
		this.userManager = userManager;
	}

	@Override
	public File getFile() {
		return PathHandler.getSignatureImage(user);
	}

	@Override
	public String getFileName() {
		return getFile().getName();
	}


	protected User loadUser() {
		return userManager.findUser(userId, getTenantId());
	}
	
	@Override
	protected boolean initializeDownload() {
		user = loadUser();

		if (user == null) {
			addActionErrorText("error.loading_user");
			setFailActionResult(MISSING);
			return false;
		}
		
		return true;
	}

	@Override
	protected String onFileNotFoundException(FileNotFoundException e) {
		addActionErrorText("error.no_signature_found");
		return MISSING;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	

	
	
}
