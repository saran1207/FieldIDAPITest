package com.n4systems.util.persistence.image;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ServiceLocator;

import java.io.File;

public class FileSystemSecondaryOrgReportFileProcessor {
    private S3Service s3Service = ServiceLocator.getS3Service();
    private User user;
    private SecondaryOrg secondaryOrg;


	public FileSystemSecondaryOrgReportFileProcessor(SecondaryOrg secondaryOrg) {
        this.secondaryOrg = secondaryOrg;
	}


	public void process(UploadedImage signature) throws FileProcessingException {
        if (signature.isRemoveImage()) {
            removeExistingSignature();
        }
		if (signature.isNewImage()) {
			putNewImageInPlace(signature);
		}
	}


	private void putNewImageInPlace(UploadedImage signature) {
        s3Service.uploadUserSignature(tempFile(signature), user);
	}

	private File tempFile(UploadedImage signature) {
		return new File(PathHandler.getTempRoot().getAbsolutePath() + '/' + signature.getUploadDirectory());
	}

    private void removeExistingSignature() {
        if (s3Service.userSignatureExists(user)) {
            s3Service.removeUserSignature(user);
        }
    }
}
