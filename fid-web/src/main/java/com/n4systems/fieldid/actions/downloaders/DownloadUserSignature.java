package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ServiceLocator;

import java.io.File;
import java.io.FileNotFoundException;

public class DownloadUserSignature extends AbstractLegacyDownloadAction {


	private Long userId;
	private User user;
    private S3Service s3Service = ServiceLocator.getS3Service();
	
	public DownloadUserSignature(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	public File getFile() {
        File signature = PathHandler.getSignatureImage(user);
        if(!signature.exists() && s3Service.userSignatureExists(user)){
            signature = s3Service.downloadUserSignature(user);
        }
        return signature;

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
