package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileNotFoundException;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;

public class DownloadUserSignature extends AbstractDownloadAction {


	private Long userId;
	private User user;
	
	public DownloadUserSignature(PersistenceManager persistenceManager) {
		super(persistenceManager);
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
		return persistenceManager.find(User.class, userId, getTenantId());
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
